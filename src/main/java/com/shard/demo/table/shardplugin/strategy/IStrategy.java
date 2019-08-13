package com.shard.demo.table.shardplugin.strategy;

public interface IStrategy<T,R> {

    R strategyHandler(T t);

}
