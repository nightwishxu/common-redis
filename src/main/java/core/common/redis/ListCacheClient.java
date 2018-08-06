package core.common.redis;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.BinaryClient.LIST_POSITION;

public class ListCacheClient extends BaseCacheClient {
    private static final Logger log = Logger.getLogger(ListCacheClient.class);

    public ListCacheClient(String mod_name) {
        super(mod_name);
    }

    public boolean lpush(String key, String value) {
        return this.lpush((String)key, (String)value, -1);
    }

    public boolean lpush(final String key, final String value, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long length = jedis.lpush(key, new String[]{value});
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient lpush");
                    }

                    if (seconds > 0) {
                        jedis.expire(key, seconds);
                    }

                    return length > 0L ? true : false;
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("lpush error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public boolean lpush(final String[] keys, final String[] values, final int seconds) {
        if (values != null && values.length != 0) {
            final ShardedJedis jedis = this.getClient();

            try {
                Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        int i = 0;

                        for(int len = keys.length; i < len; ++i) {
                            long tick = System.currentTimeMillis();
                            jedis.lpush(keys[i], new String[]{values[i]});
                            if (seconds > 0) {
                                jedis.expire(keys[i], seconds);
                            }

                            tick = System.currentTimeMillis() - tick;
                            if (tick > 50L) {
                                ListCacheClient.this.log(keys[i], tick, "ListCacheClient lpush");
                            }
                        }

                        return true;
                    }
                });
                int time_out = values.length / 10;
                boolean var8 = (Boolean)future.get(1000L * (long)time_out, TimeUnit.MILLISECONDS);
                return var8;
            } catch (Exception var11) {
                log.error("lpush error!", var11);
                this.returnBrokenResource(jedis);
            } finally {
                this.returnClient(jedis);
            }

            return false;
        } else {
            return false;
        }
    }

    public boolean lpush(final String key, final List<String> values, final int seconds) {
        if (values != null && values.size() != 0) {
            final ShardedJedis jedis = this.getClient();

            try {
                Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        int i = 0;

                        for(int len = values.size(); i < len; ++i) {
                            long tick = System.currentTimeMillis();
                            String value = (String)values.get(i);
                            jedis.lpush(key, new String[]{value});
                            if (seconds > 0) {
                                jedis.expire(key, seconds);
                            }

                            tick = System.currentTimeMillis() - tick;
                            if (tick > 50L) {
                                ListCacheClient.this.log(key, tick, "ListCacheClient lpush");
                            }
                        }

                        return true;
                    }
                });
                int time_out = values.size() / 10;
                boolean var8 = (Boolean)future.get(1000L * (long)time_out, TimeUnit.MILLISECONDS);
                return var8;
            } catch (Exception var11) {
                log.error("lpush error!", var11);
                this.returnBrokenResource(jedis);
            } finally {
                this.returnClient(jedis);
            }

            return false;
        } else {
            return false;
        }
    }

    public boolean rpush(String key, String value) {
        return this.rpush(key, (String)value, -1);
    }

    public boolean rpush(final String key, final String value, final int seconds) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long length = jedis.rpush(key, new String[]{value});
                    if (seconds > 0) {
                        jedis.expire(key, seconds);
                    }

                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient rpush");
                    }

                    return length > 0L ? true : false;
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("rpush error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public boolean rpush(final String key, final List<String> values, final int seconds) {
        if (values != null && values.size() != 0) {
            final ShardedJedis jedis = this.getClient();

            try {
                Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        int i = 0;

                        for(int len = values.size(); i < len; ++i) {
                            long tick = System.currentTimeMillis();
                            String value = (String)values.get(i);
                            jedis.rpush(key, new String[]{value});
                            tick = System.currentTimeMillis() - tick;
                            if (tick > 50L) {
                                ListCacheClient.this.log(key, tick, "ListCacheClient rpush");
                            }
                        }

                        if (seconds > 0) {
                            jedis.expire(key, seconds);
                        }

                        return true;
                    }
                });
                int time_out = values.size() / 10;
                boolean var8 = (Boolean)future.get(1000L * (long)time_out, TimeUnit.MILLISECONDS);
                return var8;
            } catch (Exception var11) {
                log.error("rpush error!", var11);
                this.returnBrokenResource(jedis);
            } finally {
                this.returnClient(jedis);
            }

            return false;
        } else {
            return false;
        }
    }

    public String lpop(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<String> future = executorService.submit(new Callable<String>() {
                public String call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String result = jedis.lpop(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient lpop");
                    }

                    return result;
                }
            });
            String var5 = (String)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("lpop error!", var8);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public Object rpop(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Object> future = executorService.submit(new Callable<Object>() {
                public String call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String result = jedis.rpop(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient rpop");
                    }

                    return result;
                }
            });
            Object var5 = future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var8) {
            log.error("rpop error!", var8);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public long llen(final String key) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long result = jedis.llen(key);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient llen");
                    }

                    return result;
                }
            });
            long var5 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var5;
        } catch (Exception var9) {
            log.error("llen error!", var9);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return -1L;
    }

    public List<String> lrange(final String key, final int start, final int end) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<List<String>> future = executorService.submit(new Callable<List<String>>() {
                public List<String> call() throws Exception {
                    long tick = System.currentTimeMillis();
                    List<String> list = jedis.lrange(key, (long)start, (long)end);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient lrange");
                    }

                    return list;
                }
            });
            List var7 = (List)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("lrange error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return null;
    }

    public long lrem(final String key, final int count, final String value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long ret = jedis.lrem(key, (long)count, value);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient lrem");
                    }

                    return ret;
                }
            });
            long var7 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var11) {
            log.error("lrem error!", var11);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return 0L;
    }

    public boolean lset(final String key, final int index, final String value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String ret = jedis.lset(key, (long)index, value);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient lset");
                    }

                    return ret.equalsIgnoreCase("ok");
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("lset error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public boolean ltrim(final String key, final int start, final int stop) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String ret = jedis.ltrim(key, (long)start, (long)stop);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient ltrim");
                    }

                    return ret.equalsIgnoreCase("ok");
                }
            });
            boolean var7 = (Boolean)future.get(1000L, TimeUnit.MILLISECONDS);
            return var7;
        } catch (Exception var10) {
            log.error("ltrim error!", var10);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return false;
    }

    public Object lindex(final String key, final int index) {
        final ShardedJedis jedis = this.getClient();

        Boolean var6;
        try {
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    long tick = System.currentTimeMillis();
                    String ret = jedis.lindex(key, (long)index);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient lindex");
                    }

                    return ret.equalsIgnoreCase("ok");
                }
            });
            Object var11 = future.get(1000L, TimeUnit.MILLISECONDS);
            return var11;
        } catch (Exception var9) {
            log.error("lindex error!", var9);
            this.returnBrokenResource(jedis);
            var6 = false;
        } finally {
            this.returnClient(jedis);
        }

        return var6;
    }

    public long linsert(final String key, final LIST_POSITION position, final String pivot, final String value) {
        final ShardedJedis jedis = this.getClient();

        try {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long tick = System.currentTimeMillis();
                    long length = jedis.linsert(key, position, pivot, value);
                    tick = System.currentTimeMillis() - tick;
                    if (tick > 50L) {
                        ListCacheClient.this.log(key, tick, "ListCacheClient linsert");
                    }

                    return length;
                }
            });
            long var8 = (Long)future.get(1000L, TimeUnit.MILLISECONDS);
            return var8;
        } catch (Exception var12) {
            log.error("linsert error!", var12);
            this.returnBrokenResource(jedis);
        } finally {
            this.returnClient(jedis);
        }

        return -1L;
    }
}