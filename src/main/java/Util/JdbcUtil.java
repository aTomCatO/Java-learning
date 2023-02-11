package Util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


/**
 * @author XYC
 */
public class JdbcUtil {
    /**只能获取一个连接对象*/
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        //1,读取配置文件中的4个基本信息
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("MySQL.properties");

        //将用户名和密码封装在Properties中
        Properties properties = new Properties();
        properties.load(inputStream);

        String driverClass = properties.getProperty("driverClass");
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接并返回
        return DriverManager.getConnection(url, user, password);
    }

    private static DataSource ds;

    /**可获取多个连接对象(数据库连接池)*/
    public static Connection getConnections() throws Exception {
        //1,导入druid的jar包
        //2,加载配置文件
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        Properties properties = new Properties();
        properties.load(is);
        //3,获取连接池对象
        ds = DruidDataSourceFactory.createDataSource(properties);
        return ds.getConnection();
    }

    public static DataSource getDatasource() {
        return ds;
    }
}
