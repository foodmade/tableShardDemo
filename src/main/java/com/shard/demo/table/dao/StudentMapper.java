package com.shard.demo.table.dao;

import com.shard.demo.table.entity.Student;
import java.util.List;

import com.shard.demo.table.shardplugin.annotation.TableShard;
import org.apache.ibatis.annotations.Param;

//添加分表注解
@TableShard
public interface StudentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Student record);

    int insertOrUpdate(Student record);

    int insertOrUpdateSelective(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    int updateBatch(List<Student> list);

    int batchInsert(@Param("list") List<Student> list);
}