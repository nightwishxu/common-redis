package core.common.redis;


import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;

public class SimpleCacheClient extends BaseCacheClient {
    private static final Logger log = Logger.getLogger(SimpleCacheClient.class);

    public SimpleCacheClient(String mod_name) {
        super(mod_name);
    }

    public Object getAndSet(final String key, final Object value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Object> future = executorService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    long tick = System.currentTimeMillis();
                    byte[] bytes = jedis.getSet(SimpleCacheClient.this.getByteKey(key), SimpleCacheClient.this.codec.encode(value));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SimpleCacheClient.this.log(key, tick, "SimpleCacheClient getAndSet");
                    }

                    return bytes != null && bytes.length > 0 ? SimpleCacheClient.this.codec.decode(bytes) : null;
                }
            });
            Object var6 = future.get(1000L, TimeUnit.MILLISECONDS);
            return var6;
        } catch (Exception var9) {
            log.error("getAndSet error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public Object get(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Object> future = executorService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    long tick = System.currentTimeMillis();
                    byte[] ret = jedis.get(SimpleCacheClient.this.getByteKey(key));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SimpleCacheClient.this.log(key, tick, "SimpleCacheClient get");
                    }

                    return ret != null && ret.length > 0 ? SimpleCacheClient.this.codec.decode(ret) : null;
                }
            });
            Object var5 = future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("get error!", var8);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public boolean set(String key, Object value) {
        return this.set(key, value, -1);
    }

    public boolean set(final String key, final Object value, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String ret = jedis.set(SimpleCacheClient.this.getByteKey(key), SimpleCacheClient.this.codec.encode(value));
                    if (seconds > 0) {
                        jedis.expire(key, seconds);
                    }

                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SimpleCacheClient.this.log(key, tick, "SimpleCacheClient set");
                    }

                    return ret.equalsIgnoreCase("OK");
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("set error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public boolean setnx(final String key, final Object value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.setnx(SimpleCacheClient.this.getByteKey(key), SimpleCacheClient.this.codec.encode(value));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SimpleCacheClient.this.log(key, tick, "SimpleCacheClient setnx");
                    }

                    return ret > 0L ? true : false;
                }
            });
            boolean var6 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var6;
        } catch (Exception var9) {
            log.error("setnx error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public boolean setex(final String key, final int seconds, final Object value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String ret = jedis.setex(SimpleCacheClient.this.getByteKey(key), seconds, SimpleCacheClient.this.codec.encode(value));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SimpleCacheClient.this.log(key, tick, "SimpleCacheClient setex");
                    }

                    return ret.equalsIgnoreCase("OK");
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("setex error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }
}
