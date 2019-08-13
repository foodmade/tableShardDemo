package com.shard.demo.table.shardplugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作用于是Method  针对单独的Mapper接口定制化分表策略
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FlexibleShardTable {

    TableShard[] moreTableStrategy() default {};
}