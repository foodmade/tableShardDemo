package com.shard.demo.table.shardplugin;

public class ShardModel {

    public ShardModel(Long shopId) {
        this.id = shopId;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
