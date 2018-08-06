package core.common.redis;


import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;

public class SortedSetCacheClient extends BaseCacheClient {
    private static final Logger log = Logger.getLogger(SortedSetCacheClient.class);

    public SortedSetCacheClient(String mod_name) {
        super(mod_name);
    }

    public boolean zadd(final String key, final Map<byte[], Double> params, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.zadd(SortedSetCacheClient.this.getByteKey(key), params);
                    if (seconds > 0) {
                        jedis.expire(SortedSetCacheClient.this.getByteKey(key), seconds);
                    }

                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SortedSetCacheClient.this.log(key, tick, "SortedSetCacheClient zadd");
                    }

                    return ret > 0L ? true : false;
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("zadd error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }
}