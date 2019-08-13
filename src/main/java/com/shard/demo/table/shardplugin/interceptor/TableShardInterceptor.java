package com.shard.demo.table.shardplugin.interceptor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.shard.demo.table.shardplugin.ReflectionUtils;
import com.shard.demo.table.shardplugin.ShardHelper;
import com.shard.demo.table.shardplugin.ShardModel;
import com.shard.demo.table.shardplugin.annotation.FlexibleShardTable;
import com.shard.demo.table.shardplugin.annotation.TableShard;
import com.shard.demo.table.shardplugin.strategy.IStrategy;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

@Intercepts
        ({      //请注意这里存在的问题,低版本 StatementHandler 的 prepare 函数只有一个参数 Connection 那么args = {Connection.class} 即可
                //高版本中,StatementHandler 的 prepare 函数存在二个参数(不知道开发者为啥不选择重载而选择重写) 所以这里的args = {Connection.class,Integer.class}
                @Signature(type = StatementHandler.class,method = "prepare",args = { Connection.class,Integer.class})
        })
@Component
public class TableShardInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(TableShardInterceptor.class);

    private static String dbType = JdbcConstants.MYSQL;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            //Target包含了Mybatis线程中的上下文实例
            RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler)invocation.getTarget();
            // 四大对象中最重要的一个,用语于数据库直接交互的接口,从中我们可以获取到sql语句与传递参数
            StatementHandler statementHandler = (StatementHandler) ReflectionUtils.getFieldValue(routingStatementHandler, "delegate");
            // 获取Mapper接口上的注解
            TableShard tableShard = parserClassShard(statementHandler);
            FlexibleShardTable flexibleShardTable = parserMethodShard(statementHandler);
            //如果不存在分表标记,则直接放行
            if(tableShard == null && flexibleShardTable == null){
                return invocation.proceed();
            }

            //获取线程变量
            ShardModel shardModel = ShardHelper.getLocalShardModel();
            if(shardModel == null){
                throw new Exception("shard table model must not empty");
            }

            //这儿使用id进行分表,具体可以按照自己的业务设计
            Long id = shardModel.getId();

            if(id == null){
                throw new Exception("shard table column must not null -->{id}");
            }

            // 如果存在定制化的分表策略,与Class级别的分表策略合并
            Map<String,Class<? extends IStrategy>> strategyMap = mergeTableInfo(tableShard,flexibleShardTable,statementHandler);

            logger.info("执行分表策略参数: {}",id);
//            logger.info(printlnMap(strategyMap));

            //循环执行,将sql中的表名替换为我们重新计算后的表名
            strategyMap.keySet().forEach(tableName -> {
                Class<? extends IStrategy> strategyClazz = strategyMap.get(tableName);
                IStrategy<Long,String> strategy;
                try {
                    strategy = strategyClazz.newInstance();
                    String newTableName = strategy.strategyHandler(id);

                    logger.info("原始表名：({})   新表名：({})   策略：({})",tableName,(tableName + newTableName),strategy.getClass().getName());

                    replaceTableName(tableName,newTableName,statementHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            logger.error("【mybatis Interceptor】 {}" , e.getMessage());
            throw e;
        }

        return invocation.proceed();
    }

    private Map<String,Class<? extends IStrategy>> mergeTableInfo(TableShard tableShard, FlexibleShardTable flexibleShardTable,StatementHandler statementHandler) throws Exception {

        Map<String,Class<? extends IStrategy>> strategyMap = new HashMap<>();

        if(tableShard != null){
            //Class级别的注解不为空 解析策略类构造器
            strategyMap.putAll(parserClassShardTableStrategy(tableShard,statementHandler));
        }

        if(flexibleShardTable != null){
            //Method级别的注解不为空,解析策略类构造器
            strategyMap.putAll(parserClassMethodShardTableStrategy(flexibleShardTable));
        }

        //合并之后的集合
        return strategyMap;
    }

    private Map<? extends String, Class<? extends IStrategy>> parserClassMethodShardTableStrategy(FlexibleShardTable flexibleShardTable) throws Exception {

        Map<String,Class<? extends IStrategy>> result = new HashMap<>();

        TableShard[] tableShards = flexibleShardTable.moreTableStrategy();

        for (TableShard tableShard : tableShards) {
            result.put(tableShard.tableName(),tableShard.shardStrategy());
        }
        return result;
    }

    private Map<? extends String, Class<? extends IStrategy>> parserClassShardTableStrategy(TableShard tableShard,StatementHandler statementHandler) throws Exception {
        Map<String,Class<? extends IStrategy>> result = new HashMap<>();

        String[] tableNames = tableShard.tableNames();
        if(tableShard.tableNames().length == 0){
            //如果没指定表名,则解析sql中所有的表名称
            tableNames = parserOriginalTableNames(statementHandler);
        }

        for (String tableName : tableNames) {
            result.put(tableName,tableShard.shardStrategy());
        }

        if(!tableShard.tableName().isEmpty()){
            //继续解析tableName
            result.put(tableShard.tableName(),tableShard.shardStrategy());
        }

        return result;
    }

    /**
     * 获取类的分表注解
     */
    private TableShard parserClassShard(StatementHandler statementHandler) throws Exception {
        Class classProxy = getClassProxy(statementHandler);
        return (TableShard)classProxy.getAnnotation(TableShard.class);
    }

    /**
     * 获取函数的分表注解
     */
    private FlexibleShardTable parserMethodShard(StatementHandler statementHandler) throws Exception {
        Class classProxy = getClassProxy(statementHandler);

        String classId = getClassId(statementHandler);
        //获取Method名称
        String methodName = classId.substring(classId.lastIndexOf(".") + 1);
        //获取Method句柄
        Method method = ReflectionUtils.getAccessibleMethod(classProxy,methodName,ReflectionUtils.getMethodParamTypes(classProxy,methodName));
        if(method == null){
            return null;
        }
        return method.getAnnotation(FlexibleShardTable.class);
    }

    /**
     * 获取访问者代理 就是我们的Mapper类构造器
     */
    private Class getClassProxy(StatementHandler statementHandler) throws Exception {
        String id = getClassId(statementHandler);
        id = id.substring(0, id.lastIndexOf('.'));

        return Class.forName(id);
    }
    /**
     * Mapper类的相对路径
     */
    private String getClassId(StatementHandler statementHandler) throws Exception {
        MappedStatement mappedStatement = (MappedStatement)getObjFiled(statementHandler.getParameterHandler(),"mappedStatement").get(statementHandler.getParameterHandler());

        if(mappedStatement == null){
            return null;
        }

        return mappedStatement.getId();
    }

    /**
     * 解析当前执行的原始sql语句
     */
    private String parserSql(StatementHandler statementHandler) throws Exception {
        BoundSql boundSql = (BoundSql)getObjFiled(statementHandler.getParameterHandler(),"boundSql").get(statementHandler.getParameterHandler());
        return boundSql.getSql();
    }

    /**
     * 替换原始表名
     */
    private void replaceTableName(String oldTableName,String newTableName, StatementHandler statementHandler) throws Exception {

        //获取原始sql语句
        String sql = parserSql(statementHandler);

        // 获取boundSql对象,这个对象是存储sql语句的,最重要的就是将这个对象中的sql更换
        Field boundField = getObjFiled(statementHandler.getParameterHandler(),"boundSql");
        BoundSql boundSql = (BoundSql)boundField.get(statementHandler.getParameterHandler());

        //获取sql属性
        Field sqlField = getObjFiled(boundSql,"sql");
        //替换sql中的表名称,并且设置到获取boundSql对象中
        sqlField.set(boundSql,sql.replaceAll(oldTableName,oldTableName + newTableName));
    }

    private String[] parserOriginalTableNames(StatementHandler statementHandler) throws Exception {
        //获取sql语句
        String sql = parserSql(statementHandler);
        //获取sql中的所有表名 这儿使用了alibaba.druid.sql 的工具类,对比了很多工具类,这个最优
        String[] tableNames = getTableNameBySql(sql);

        if(tableNames.length == 0 ){
            throw new Exception("Not Parser Anywhere TableName");
        }
        //返回所有表名
        return tableNames;
    }

    private static String[] getTableNameBySql(String sql){

        String result = SQLUtils.format(sql, dbType);
        logger.info("格式化后输出：\n" + result);
        logger.info("*********************");
        List<SQLStatement> sqlStatementList = getSQLStatementList(sql);
        //默认为一条sql语句
        SQLStatement stmt = sqlStatementList.get(0);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmt.accept(visitor);
        logger.info("数据库类型\t\t" + visitor.getDbType());
        logger.info("查询的字段\t\t" + visitor.getColumns());
        logger.info("表名\t\t\t" + visitor.getTables().keySet());
        logger.info("条件\t\t\t" + visitor.getConditions());
        logger.info("group by\t\t" + visitor.getGroupByColumns());
        logger.info("order by\t\t" + visitor.getOrderByColumns());


        Set<TableStat.Name> tableNames = visitor.getTables().keySet();
        String[] tableArray = new String[tableNames.size()];
        int i = 0;
        for (TableStat.Name tableName : tableNames) {
            tableArray[i] = tableName.getName();
            i ++ ;
        }

        return tableArray;
    }

    private String printlnMap(Map map){

        StringBuilder sb = new StringBuilder();

        map.keySet().forEach(key -> {
            sb.append("\n")
                    .append("表名：")
                    .append(key)
                    .append("\t")
                    .append("策略：")
                    .append(map.get(key).toString());
        });
        return sb.toString();

    }

    private static List<SQLStatement> getSQLStatementList(String sql) {
        return SQLUtils.parseStatements(sql, dbType);
    }

    private Field getObjFiled(Object statementHandler,String fieldName) throws NoSuchFieldException {
        Field field = statementHandler.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
