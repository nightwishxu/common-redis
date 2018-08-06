package core.common.redis;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import core.common.util.JsonUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;

public class HashCacheClient extends BaseCacheClient {
    private final Logger log = Logger.getLogger(this.getClass());

    public HashCacheClient(String mod_name) {
        super(mod_name);
    }

    public boolean set(final String key, final String field, final Object value, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long status = jedis.hset(key, field, JsonUtils.ObjectToJson(value));
                    if (seconds > 0) {
                        jedis.expire(key, seconds);
                    }

                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        HashCacheClient.this.log(key, tick, "HashCacheClient set");
                    }

                    return status >= 0L ? true : false;
                }
            });
            boolean var8 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var8;
        } catch (Exception var11) {
            this.log.error("set error!", var11);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public Object get(final String key, final String field) {
        final ShardedJedis jedis = this.getClient();

        Boolean var6;
        try {
            Future<Object> future = executorService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String str = jedis.hget(key, field);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        HashCacheClient.this.log(key, tick, "HashCacheClient get");
                    }

                    return str != null && str.length() > 0 ? JsonUtils.JsonToObject(str) : null;
                }
            });
            Object var11 = future.get(1000L, TimeUnit.MILLISECONDS);
            return var11;
        } catch (Exception var9) {
            this.log.error("get error!", var9);
            this.returnBrokenResource(jedis);
            var6 = false;
        } finally {
            this.returnClient(jedis);
        }

        return var6;
    }

    public boolean hsetnx(final String key, final String field, final Object value, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long status = jedis.hsetnx(key, field, JsonUtils.ObjectToJson(value));
                    if (seconds > 0) {
                        jedis.expire(key, seconds);
                    }

                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        HashCacheClient.this.log(key, tick, "HashCacheClient hsetnx");
                    }

                    return status >= 0L ? true : false;
                }
            });
            boolean var8 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var8;
        } catch (Exception var11) {
            this.log.error("hsetnx error!", var11);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public boolean hmset(final String key, Map<String, Object> hash, final int seconds) {
        if (hash.isEmpty()) {
            return false;
        } else {
            Map<String, String> map = new HashMap();
            Iterator it = hash.keySet().iterator();

            while(it.hasNext()) {
                String k = (String)it.next();
                Object value = hash.get(k);
                map.put(k, JsonUtils.ObjectToJson(value));
            }

            final Map<String, String> param = map;
            final ShardedJedis jedis = this.getClient();

            try {
                Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        long tick = System.currentTimeMillis();
                        String ret = jedis.hmset(key, param);
                        if (seconds > 0) {
                            jedis.expire(key, seconds);
                        }

                        tick = System.currentTimeMillis() - tick;
                        if (tick > 50L) {
                            HashCacheClient.this.log(key, tick, "HashCacheClient hmset");
                        }

                        return ret.equalsIgnoreCase("OK");
                    }
                });
                boolean var9 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
                return var9;
            } catch (Exception var12) {
                this.log.error("hmset error!", var12);
                this.returnBrokenResource(jedis);
            } finally {
                this.returnClient(jedis);
            }

            return false;
        }
    }

    public long hdel(final String key, int seconds, final String... fields) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.hdel(key, fields);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        HashCacheClient.this.log(key, tick, "HashCacheClient hsetnx");
                    }

                    return ret;
                }
            });
            long var7 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var11) {
            this.log.error("hsetnx error!", var11);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return 0L;
    }

    public long hlen(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.hlen(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        HashCacheClient.this.log(key, tick, "HashCacheClient hsetnx");
                    }

                    return ret;
                }
            });
            long var5 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var9) {
            this.log.error("hsetnx error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return 0L;
    }

    public boolean hexists(final String key, final String field) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    boolean ret = jedis.hexists(key, field);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        HashCacheClient.this.log(key, tick, "HashCacheClient hexists");
                    }

                    return ret;
                }
            });
            boolean var6 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var6;
        } catch (Exception var9) {
            this.log.error("hexists error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }
}