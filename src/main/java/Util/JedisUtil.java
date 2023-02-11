package Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author XYC
 */
public class JedisUtil {
    private static final JedisPool JEDIS_POOL;

    static {
        InputStream is = JedisUtil.class.getClassLoader().getResourceAsStream("Jedis.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));


        JEDIS_POOL =
                new JedisPool(config,properties.getProperty("host"),
                        Integer.parseInt(properties.getProperty("port")),
                        2000,
                        properties.getProperty("password"));
    }

    public static Jedis getJedis() {
        return JEDIS_POOL.getResource();
    }

}
