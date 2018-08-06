package core.common.redis;


import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;

public class StringCacheClient extends BaseCacheClient {
    private static final Logger log = Logger.getLogger(StringCacheClient.class);

    public StringCacheClient(String mod_name) {
        super(mod_name);
    }

    public static void main(String[] args) {
        new StringCacheClient("redis_ref_hosts");
    }

    public Long incr(final String key) {
        final ShardedJedis jedis = this.getClient();

        Long var5;
        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long res = jedis.incr(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        StringCacheClient.this.log(key, tick, "StringCacheClient incr");
                    }

                    return res;
                }
            });
            var5 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("incr error!", var8);
            this.returnBrokenResource(jedis);
            var5 = 0L;
        } finally {
            this.returnClient(jedis);
        }

        return var5;
    }

    public Long incrBy(final String key, final long value) {
        final ShardedJedis jedis = this.getClient();

        Long var7;
        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long res = jedis.incrBy(StringCacheClient.this.getByteKey(key), value);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        StringCacheClient.this.log(key, tick, "StringCacheClient incrBy");
                    }

                    return res;
                }
            });
            var7 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("incrBy error!", var10);
            this.returnBrokenResource(jedis);
            var7 = 0L;
        } finally {
            this.returnClient(jedis);
        }

        return var7;
    }

    public String getAndSet(final String key, final String value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<String> future = executorService.submit(new Callable<String>() {
                public String call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String res = jedis.getSet(key, value);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        StringCacheClient.this.log(key, tick, "StringCacheClient getAndSet");
                    }

                    return res;
                }
            });
            String var6 = (String)future.get(1000L, TimeUnit.MILLISECONDS);
            return var6;
        } catch (Exception var9) {
            log.error("getAndSet error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public String get(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<String> future = executorService.submit(new Callable<String>() {
                public String call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String res = jedis.get(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        StringCacheClient.this.log(key, tick, "StringCacheClient get");
                    }

                    return res;
                }
            });
            String var5 = (String)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("get error!", var8);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public boolean set(final String key, final String value, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String ret = jedis.set(key, value);
                    if (seconds > 0) {
                        jedis.expire(key, seconds);
                    }

                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        StringCacheClient.this.log(key, tick, "StringCacheClient set");
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

    public boolean set(String key, String value) {
        return this.set(key, value, 0);
    }

    public boolean setnx(final String key, final String value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.setnx(key, value);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        StringCacheClient.this.log(key, tick, "StringCacheClient setnx");
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

    public boolean setex(final String key, final int seconds, final String value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String ret = jedis.setex(key, seconds, value);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        StringCacheClient.this.log(key, tick, "StringCacheClient setex");
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
