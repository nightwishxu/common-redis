package core.common.redis;

import core.common.util.PropertieUtils;
import core.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HostAndPortUtil {
    private static Map<String, List<HostAndPortUtil.HostAndPort>> hostAndPortMap = new ConcurrentHashMap();
    private static final String REDIS_CONFIG_FILE = "redis-host.properties";
    private static final PropertieUtils propertieUtils = new PropertieUtils("redis-host.properties");

    static {
        propertieUtils.close();
        Set set = propertieUtils.stringPropertyNames();
        if (set.size() == 0)
            throw new IllegalArgumentException("args is null !");
//        String hostDef;
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String value = propertieUtils.getProperty(key);
            String[] envHosts = value.split(",|，");
            if (envHosts.length != 0) {
                List hostList = new ArrayList();
                for (String hostDef:envHosts) {
                    String[] hostAndPort = hostDef.split(":");
                    if ((hostAndPort != null) && (hostAndPort.length > 1)) {
                        HostAndPortUtil.HostAndPort hnp = new HostAndPortUtil.HostAndPort();
                        hnp.host = hostAndPort[0];
                        try {
                            hnp.port = Integer.parseInt(hostAndPort[1]);
                        } catch (NumberFormatException nfe) {
                            hnp.port = 6379;
                            throw new IllegalArgumentException(StringUtils.format(
                                    " ip : {0} port {1} is error !", new Object[]{
                                            hostAndPort[0], hostAndPort[1]}));
                        }

                        if (hostAndPort.length == 3) {
                            hnp.pwd = hostAndPort[2];
                        }

                        hostList.add(hnp);
                    }
                }
                hostAndPortMap.put(key, hostList);
            }
        }
    }

    public HostAndPortUtil() {
    }

    public static Map<String, List<HostAndPortUtil.HostAndPort>> getRedisServers() {
        return hostAndPortMap;
    }

    public static void main(String[] args) {
        String str = "192.168.168.208:6379";
        System.out.println(str.split("\\|").length);
    }

    public static class HostAndPort {
        public String host;
        public int port;
        public String pwd;

        public HostAndPort() {
        }
    }
}