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
        Set<String> set = propertieUtils.stringPropertyNames();
        if (set.size() == 0) {
            throw new IllegalArgumentException("args is null !");
        } else {
            Iterator it = set.iterator();

            while(true) {
                String key;
                String[] envHosts;
                do {
                    String key;
                    if (!it.hasNext()) {
                        StringBuilder strb = new StringBuilder("Redis hosts to be used : ");

                        label60:
                        for(Iterator it = hostAndPortMap.keySet().iterator(); it.hasNext(); strb.append("]")) {
                            key = (String)it.next();
                            strb.append(" key : " + key);
                            strb.append("[");
                            List<HostAndPortUtil.HostAndPort> hostList = (List)hostAndPortMap.get(key);
                            Iterator var18 = hostList.iterator();

                            while(true) {
                                while(true) {
                                    if (!var18.hasNext()) {
                                        continue label60;
                                    }

                                    HostAndPortUtil.HostAndPort hnp = (HostAndPortUtil.HostAndPort)var18.next();
                                    if (hnp.pwd != null && hnp.pwd.length() > 0) {
                                        strb.append(hnp.host + ":" + hnp.port + ":" + hnp.pwd + ",");
                                    } else {
                                        strb.append(hnp.host + ":" + hnp.port + ",");
                                    }
                                }
                            }
                        }

                        System.out.println(strb);
                        return;
                    }

                    key = (String)it.next();
                    key = propertieUtils.getProperty(key);
                    envHosts = key.split(",|，");
                } while(envHosts.length == 0);

                List<HostAndPortUtil.HostAndPort> hostList = new ArrayList();
                String[] var9 = envHosts;
                int var8 = envHosts.length;

                for(int var7 = 0; var7 < var8; ++var7) {
                    String hostDef = var9[var7];
                    String[] hostAndPort = hostDef.split(":");
                    if (hostAndPort != null && hostAndPort.length > 1) {
                        HostAndPortUtil.HostAndPort hnp = new HostAndPortUtil.HostAndPort();
                        hnp.host = hostAndPort[0];

                        try {
                            hnp.port = Integer.parseInt(hostAndPort[1]);
                        } catch (NumberFormatException var13) {
                            hnp.port = 6379;
                            throw new IllegalArgumentException(StringUtils.format(" ip : {0} port {1} is error !", new Object[]{hostAndPort[0], hostAndPort[1]}));
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