package Java.redis;

import Util.JedisUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author XYC
 * redis.clients.jedis.exceptions.JedisConnectionException: Failed connecting to localhost:6379
 * 1.redis 没开启；
 * 2.端口号，IP地址 错误
 * 3.防火墙没关闭
 * 解决：
 * 1:查看防火状态
 * systemctl status firewalld
 * service iptables status
 *
 * 2:暂时关闭防火墙
 * systemctl stop firewalld
 * service iptables stop
 *
 * 3:永久关闭防火墙
 * systemctl disable firewalld
 * chkconfig iptables off
 *
 * 4:重启防火墙
 * systemctl enable firewalld
 * service iptables restart
 */
public class JedisTest {
    private final Jedis jedis = JedisUtil.getJedis();

    @Test
    public void string(){
        jedis.set("hello","jedis");
        System.out.println(jedis.get("hello"));
        jedis.close();
    }
    @Test
    public void hashMap(){
        //对象,Key,Value
        jedis.hset("map","a","1");
        jedis.hset("map","b","2");
        jedis.hset("map","c","3");
        System.out.println(jedis.hgetAll("map"));
        System.out.println(jedis.hget("map","a"));
        jedis.close();
    }
    /**
     * list存储结构:
     * 就像一个盒子,左右俩端各有一扇门(lpush和rpush),数据可以从任意一扇门进去,但是只能从左边的门取出来
     * 所以,通过左边的门先进去的数据,后出来   1,2,3  3,2,1  先进后出的栈
     *     通过右边的门先进去的数据,则先出来  1,2,3  1,2,3  先进先出的队列
     * 而如果数据分别从俩扇门放进去,则从左边门进来的数据先出去,从右边进来的数据后出去,如
     *     lpush: 1,2,3
     *     rpush: 4,5,6
     *     lrange:3,2,1,4,5,6
     * 并且储存的元素是可重复的
     * */
    @Test
    public void list() {
        jedis.lpush("friend","1");
        jedis.lpush("friend","2");
        jedis.lpush("friend","3");
        jedis.rpush("friend","4");
        jedis.rpush("friend","5");
        jedis.rpush("friend","6");
        List<String> list =jedis.lrange("friend",0,-1);
        System.out.println(list.size()+":"+list);
        jedis.close();
    }
    /**元素不可重复,无序*/
    @Test
    public void set(){
        jedis.sadd("set","java","java","Linux","jvm","C++", "MySQL.properties.txt","Redis");
        System.out.println(jedis.smembers("set"));
        jedis.close();
    }
    /**元素不可重复,有序*/
    @Test
    public void sortedset(){
        jedis.zadd("sortedset",4,"b");
        jedis.zadd("sortedset",7,"c");
        jedis.zadd("sortedset",2,"a");
        System.out.println(jedis.zrange("sortedset",0,-1));
        jedis.close();
    }
}
