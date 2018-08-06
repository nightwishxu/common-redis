package core.common.redis;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import core.common.redis.HostAndPortUtil.HostAndPort;
import core.common.util.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClientPool {
    private static final Logger log = Logger.getLogger(RedisClientPool.class);
    private static Map<String, List<HostAndPort>> hostAndPortMap;
    private static Map<String, ShardedJedisPool> redisPools = new ConcurrentHashMap();

    static {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(5000);
        config.setMaxWaitMillis(10000L);
        config.setMaxIdle(200);
        config.setMinIdle(50);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(false);
        hostAndPortMap = HostAndPortUtil.getRedisServers();
        if (hostAndPortMap.isEmpty()) {
            throw new RuntimeException(" redis config error !!! ");
        } else {
            Iterator it = hostAndPortMap.keySet().iterator();

            while(it.hasNext()) {
                String mod_name = (String)it.next();
                List<HostAndPort> hostList = (List)hostAndPortMap.get(mod_name);
                List<JedisShardInfo> shards = new ArrayList();

                JedisShardInfo jedisShardInfo;
                for(Iterator var6 = hostList.iterator(); var6.hasNext(); shards.add(jedisShardInfo)) {
                    HostAndPort hnp = (HostAndPort)var6.next();
                    log.info(StringUtils.format("[redis - list],host:{0},port:{1}", new Object[]{hnp.host, hnp.port}));
                    jedisShardInfo = new JedisShardInfo(hnp.host, hnp.port);
                    if (hnp.pwd != null && hnp.pwd.length() > 0) {
                        jedisShardInfo.setPassword(hnp.pwd);
                    }
                }

                ShardedJedisPool pool = new ShardedJedisPool(config, shards);
                redisPools.put(mod_name, pool);
            }

        }
    }

    private RedisClientPool() {
    }

    public static ShardedJedis getJedisClient(String mod_name) {
        return ((ShardedJedisPool)redisPools.get(mod_name)).getResource();
    }

    public static void returnJedisClient(String mod_name, ShardedJedis shardedJedis) {
        ((ShardedJedisPool)redisPools.get(mod_name)).returnResource(shardedJedis);
    }

    public static void returnBrokenResource(String mod_name, ShardedJedis shardedJedis) {
        ((ShardedJedisPool)redisPools.get(mod_name)).returnBrokenResource(shardedJedis);
    }

    public static void main(String[] args) {
    }
}