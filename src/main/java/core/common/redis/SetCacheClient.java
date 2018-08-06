package core.common.redis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;

public class SetCacheClient extends BaseCacheClient {
    private static final Logger log = Logger.getLogger(SetCacheClient.class);

    public SetCacheClient(String mod_name) {
        super(mod_name);
    }

    public boolean sadd(String key, Object value) {
        return this.sadd(key, value, -1);
    }

    public boolean sadd(final String key, final Object value, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.sadd(SetCacheClient.this.getByteKey(key), new byte[][]{SetCacheClient.this.codec.encode(value)});
                    if (seconds > 0) {
                        jedis.expire(SetCacheClient.this.getByteKey(key), seconds);
                    }

                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SetCacheClient.this.log(key, tick, "SetCacheClient setex");
                    }

                    return ret > 0L ? true : false;
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("sadd error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public boolean srem(final String key, final Object member) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.srem(SetCacheClient.this.getByteKey(key), new byte[][]{SetCacheClient.this.codec.encode(member)});
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SetCacheClient.this.log(key, tick, "SetCacheClient srem");
                    }

                    return ret > 0L ? true : false;
                }
            });
            boolean var6 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var6;
        } catch (Exception var9) {
            log.error("srem error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public Set<Object> smembers(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Set<Object>> future = executorService.submit(new Callable<Set<Object>>() {
                public Set<Object> call() throws Exception {
                    long tick = System.currentTimeMillis();
                    Set<byte[]> set = jedis.smembers(SetCacheClient.this.getByteKey(key));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SetCacheClient.this.log(key, tick, "SetCacheClient smembers");
                    }

                    Set<Object> result = null;
                    if (CollectionUtils.isNotEmpty(set)) {
                        result = new HashSet();
                        Iterator var6 = set.iterator();

                        while(var6.hasNext()) {
                            byte[] bytes = (byte[])var6.next();
                            result.add(SetCacheClient.this.codec.decode(bytes));
                        }
                    }

                    return result;
                }
            });
            Set var5 = (Set)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("smembers error!", var8);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public boolean sismember(final String key, final Object member) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    boolean res = jedis.sismember(SetCacheClient.this.getByteKey(key), SetCacheClient.this.codec.encode(member));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SetCacheClient.this.log(key, tick, "SetCacheClient sismember");
                    }

                    return res;
                }
            });
            boolean var6 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var6;
        } catch (Exception var9) {
            log.error("sismember error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public long scard(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long size = jedis.scard(SetCacheClient.this.getByteKey(key));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SetCacheClient.this.log(key, tick, "SetCacheClient scard");
                    }

                    return size;
                }
            });
            long var5 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var9) {
            log.error("scard error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return 0L;
    }

    public Object spop(final String key) {
        final ShardedJedis jedis = this.getClient();

        Integer var5;
        try {
            Future<Object> future = executorService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    long tick = System.currentTimeMillis();
                    byte[] bytes = jedis.spop(SetCacheClient.this.getByteKey(key));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SetCacheClient.this.log(key, tick, "SetCacheClient spop");
                    }

                    return SetCacheClient.this.codec.decode(bytes);
                }
            });
            Object var10 = future.get(1000L, TimeUnit.MILLISECONDS);
            return var10;
        } catch (Exception var8) {
            log.error("spop error!", var8);
            this.returnBrokenResource(jedis);
            var5 = -1;
        } finally {
            this.returnClient(jedis);
        }

        return var5;
    }

    public Object srandmember(final String key) {
        final ShardedJedis jedis = this.getClient();

        Integer var5;
        try {
            Future<Object> future = executorService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    long tick = System.currentTimeMillis();
                    byte[] bytes = jedis.srandmember(SetCacheClient.this.getByteKey(key));
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        SetCacheClient.this.log(key, tick, "SetCacheClient srandmember");
                    }

                    return SetCacheClient.this.codec.decode(bytes);
                }
            });
            Object var10 = future.get(1000L, TimeUnit.MILLISECONDS);
            return var10;
        } catch (Exception var8) {
            log.error("srandmember error!", var8);
            this.returnBrokenResource(jedis);
            var5 = -1;
        } finally {
            this.returnClient(jedis);
        }

        return var5;
    }
}