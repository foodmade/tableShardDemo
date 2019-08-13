package com.shard.demo.table.shardplugin.strategy;

public class DefaultShardStrategy implements IStrategy<Long,String> {
    @Override
    public String strategyHandler(Long aLong) {
        return "_"+(aLong % 10) + "";
    }
}
