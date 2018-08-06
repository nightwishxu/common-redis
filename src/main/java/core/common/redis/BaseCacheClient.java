
package core.common.redis;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import core.common.util.LogFormat;
import core.common.util.LogUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;

public abstract class BaseCacheClient {
    private static final Logger log = Logger.getLogger(BaseCacheClient.class);
    protected static final ExecutorService executorService = Executors.newCachedThreadPool();
    protected static final long TIMEOUT_MILL_SEC = 1000L;
    protected static final long limit = 50L;
    protected DefaultCodec codec = new DefaultCodec();
    private String mod_name;

    public BaseCacheClient(String mod_name) {
        this.mod_name = mod_name;
    }

    protected ShardedJedis getClient() {
        return RedisClientPool.getJedisClient(this.mod_name);
    }

    protected void returnClient(ShardedJedis shardedJedis) {
        RedisClientPool.returnJedisClient(this.mod_name, shardedJedis);
    }

    protected void returnBrokenResource(ShardedJedis shardedJedis) {
        RedisClientPool.returnBrokenResource(this.mod_name, shardedJedis);
    }

    protected byte[] getByteKey(String key) {
        return key == null ? null : key.getBytes();
    }

    public Long del(final String key) {
        final ShardedJedis jedis = this.getClient();

        Long var5;
        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long result = jedis.del(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        BaseCacheClient.this.log(key, tick, "del");
                    }

                    return result;
                }
            });
            var5 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("del error!", var8);
            this.returnBrokenResource(jedis);
            var5 = 0L;
        } finally {
            this.returnClient(jedis);
        }

        return var5;
    }

    public boolean exists(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    boolean result = jedis.exists(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        BaseCacheClient.this.log(key, tick, "exists");
                    }

                    return result;
                }
            });
            boolean var5 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("exists error!", var8);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    protected void log(String key, long tick, String act) {
        try {
            LogFormat.log("redis", act, this.mod_name, key + "\tms:" + tick);
        } catch (Exception var6) {
            LogUtil.error(var6);
        }

    }
}
