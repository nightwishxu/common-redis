package core.common.redis;


public class Constants {
    public static String LOG_FORMATTER = "ServerName:[{}] ACTION:{},PASSPORT:{},PARAMS:{}.";
    public static final String _INDEX_UUID = ":index_UUID";
    public static final String REDIS_INDEX_MOD = "redis_index_hosts";
    public static final int CACHE_LIST_TIME = 3600;
    public static final int CACHE_PAGE_TIME = 600;
    public static final int CACHE_TIME = 3600;
    public static final int LONGSIZE = 8;

    public Constants() {
    }

    public static void main(String[] args) {
        JsonCacheClient client = new JsonCacheClient("redis_ref_hosts");
        System.out.println(client.get("__"));
    }
}
