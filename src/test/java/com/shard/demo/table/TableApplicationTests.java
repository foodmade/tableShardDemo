package com.shard.demo.table;

import com.shard.demo.table.dao.StudentMapper;
import com.shard.demo.table.entity.Student;
import com.shard.demo.table.shardplugin.ShardHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableApplicationTests {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void contextLoads() {

        /**
         * <select id="selectByPrimaryKey" parameterType="java.lang.Long" resultMap="BaseResultMap">
         *     select
         *     <include refid="Base_Column_List" />
         *     from student
         *     where id = #{id,jdbcType=BIGINT}
         *   </select>
         */

        //我的mapper.xml文件中的sql如上 表名为 student

        //设置分表ID
        ShardHelper.setShardId(154154L);

        Student student = studentMapper.selectByPrimaryKey(1L);
        System.out.println(student.toString());
        //清理
        ShardHelper.clear();

    }

}
