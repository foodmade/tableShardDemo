package com.shard.demo.table.shardplugin.annotation;

import com.shard.demo.table.shardplugin.strategy.DefaultShardStrategy;
import com.shard.demo.table.shardplugin.strategy.IStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分表标记 针对类上的注解,作用域是所有接口
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableShard {

    /**
     * 要替换的表名列表 默认替换所有表名 如果存在替换部分 请填写需要替换的表名在数组中
     */
    String[] tableNames() default {};

    // 对应的分表策略类
    Class<? extends IStrategy> shardStrategy() default DefaultShardStrategy.class;

    String tableName() default "";
}
