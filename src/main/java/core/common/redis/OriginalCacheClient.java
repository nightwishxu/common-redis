package core.common.redis;

import redis.clients.jedis.ShardedJedis;

public class OriginalCacheClient extends BaseCacheClient {
    public OriginalCacheClient(String mod_name) {
        super(mod_name);
    }

    public ShardedJedis getClient() {
        return super.getClient();
    }

    public void returnClient(ShardedJedis shardedJedis) {
        super.returnClient(shardedJedis);
    }

    public void returnBrokenResource(ShardedJedis shardedJedis) {
        super.returnBrokenResource(shardedJedis);
    }
}
