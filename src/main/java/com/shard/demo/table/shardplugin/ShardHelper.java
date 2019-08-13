package com.shard.demo.table.shardplugin;

public class ShardHelper {

    private static ThreadLocal<ShardModel> localShardModel = new ThreadLocal<>();

    /**
     * 获取线程变量
     */
    public static ShardModel getLocalShardModel() {
        return localShardModel.get();
    }

    /**
     * 设置分表参数
     * @param id
     */
    public static void setShardId(Long id){
        localShardModel.set(new ShardModel(id));
    }

    /**
     * 清理
     */
    public static void clear(){
        localShardModel.remove();
    }
}
