package Java.jdbc;

import Util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.*;
import java.util.*;

public class JDBC {
    static String 数据库, table_name, static_tableName, sql, sqls, a, b, c, 操作;
    static int columnCount, cao;
    static Connection connection = null;
    static Statement statement = null;
    static ResultSet resultSet = null;
    static JdbcTemplate jdbcTemplate;
    static PreparedStatement preparedStatement;
    static ResultSetMetaData resultSetMetaData;
    static Scanner s = new Scanner(System.in);

    /*public static void setStatic_tableName(String static_tableName) {
        JDBC.static_tableName = static_tableName;
    }*/
    /*
    //解决每次请求服务都要重复输入表名的问题(结合服务3)
    public static void name(){
        if(static_tableName == null){
            P.print("请输入表名:\t");
            table_name = s.next();
        }else if(static_tableName != null){
            table_name = static_tableName;
        }
    }*/
    public static boolean Int(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Map<String, Object> SQL_Map(String sql) throws Exception {
        Map<String, Object> SqlMap = new HashMap<>();
        resultSet = statement.executeQuery(sql);
        String key;
        Object value;
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                key = resultSetMetaData.getColumnName(i);
                value = resultSet.getString(resultSetMetaData.getColumnName(i));
                SqlMap.put(key, value);
            }
        }
        return SqlMap;
    }

    public static List<Map<String, Object>> SQL_List(String sql) throws Exception {
        List<Map<String, Object>> SqlList = new ArrayList<>();
        resultSet = statement.executeQuery(sql);
        String key;
        Object value;
        while (resultSet.next()) {
            Map<String, Object> SqlMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                key = resultSetMetaData.getColumnName(i);
                value = resultSet.getString(resultSetMetaData.getColumnName(i));
                SqlMap.put(key, value);
            }
            SqlList.add(SqlMap);
        }
        return SqlList;
    }


    public static void main(String[] args) throws Exception {
        /*
        //方式一:
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        //1.使用反射获取Driver实现类对象
        Class c = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) c.newInstance();
        //2.提供连接的数据库
        String url = "jdbc:mysql://localhost:3306/xyc";
        //3.提供连接所需要的用户名和密码,将用户名和密码封装在Properties中
        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","xyc20011015");
        connection = driver.connect(url,properties);
        //4.通过Connection对象获取Statement
            statement = connection.createStatement();
            //5.使用Statement执行SQL语句
            String sql = "select * from 电影";
            resultSet = statement.executeQuery(sql);
            //6.操作ResultSet结果集
            P.println("name\t\t\t\t\t|\t\t\t\t分类\t\t\t\t\t|\t\t\t\t评价");
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String 分类 = resultSet.getString("分类");
                int  评价= resultSet.getInt("评价");
                P.println(name+"\t\t\t\t|\t\t\t"+分类+"\t\t\t|\t\t\t\t"+评价);
            };
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //7.关闭连接,释放资源
            if(resultSet != null){resultSet.close();}
            if(statement != null){statement.close();}
            if(connection != null){connection.close();}
        }
        */

        /*
        //方式二(使用DriverManager替换Driver):
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        //1.获取Driver实现类的对象
        Class c = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) c.newInstance();
        //2.提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/xyc";
        String user = "root";
        String password = "xyc20011015";
        //3.注册驱动
        DriverManager.registerDriver(driver);
        //4.获取连接
        connection = DriverManager.getConnection(url,user,password);
            //5.通过Connection对象获取Statement
            statement = connection.createStatement();
            //6.使用Statement执行SQL语句
            String sql = "select * from 电影";
            resultSet = statement.executeQuery(sql);
            //7.操作ResultSet结果集
            P.println("name\t\t\t\t\t|\t\t\t\t分类\t\t\t\t\t|\t\t\t\t评价");
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String 分类 = resultSet.getString("分类");
                int  评价= resultSet.getInt("评价");
                P.println(name+"\t\t\t\t|\t\t\t"+分类+"\t\t\t|\t\t\t\t"+评价);
            };
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //8.关闭连接,释放资源
            if(resultSet != null){resultSet.close();}
            if(statement != null){statement.close();}
            if(connection != null){connection.close();}
        }
         */

        /*
        //方式三(进阶版):
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            //1.加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2.通过DriverManager获取数据库连接
            String url = "jdbc:mysql://localhost:3306/xyc";
            String username = "root";
            String password = "xyc20011015";
            connection = DriverManager.getConnection(url,username,password);
            //3.通过Connection对象获取Statement对象
            statement = connection.createStatement();
            //4.使用Statement执行SQL语句
            String sql = "select * from 电影";
            resultSet = statement.executeQuery(sql);
            //5.操作ResultSet结果集
            P.println("name\t\t\t\t\t|\t\t\t\t分类\t\t\t\t\t|\t\t\t\t评价");
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String 分类 = resultSet.getString("分类");
                int  评价= resultSet.getInt("评价");
                P.println(name+"\t\t\t\t|\t\t\t"+分类+"\t\t\t|\t\t\t\t"+评价);
            };
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //6.关闭连接,释放资源
            if(resultSet != null){resultSet.close();}
            if(statement != null){statement.close();}
            if(connection != null){connection.close();}
        }
        */

        /*
        //方式四(终极版):
        //实现了数据和代码的分离(解耦),如果需要修改配置文件信息,可以避免程序重新打包!
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String name, 分类;
        int 评价, 操作;
        boolean 开关 = true;
        try {
            //1,读取配置文件中的4个基本信息
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("MySQL.properties.txt");

            //将用户名和密码封装在Properties中
            Properties properties = new Properties();
            properties.load(inputStream);

            String driverClass = properties.getProperty("driverClass");
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            //2.加载驱动
            Class.forName(driverClass);
            //3.获取连接
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            P.print("1,select * from 电影\n" +
                    "2,insert into 电影(name,分类,评价) values (?,?,?)\n" +
                    "3,关闭\n");
            do {
                操作 = s.nextInt();
                switch (操作) {
                    case 1: {
                        String sql = "select * from 电影";
                        resultSet = statement.executeQuery(sql);
                        P.println("name\t\t\t\t\t|\t\t\t\t分类\t\t\t\t\t|\t\t\t\t评价");
                        while (resultSet.next()) {
                            name = resultSet.getString("name");
                            分类 = resultSet.getString("分类");
                            评价 = resultSet.getInt("评价");
                            P.println(name + "\t\t\t\t|\t\t\t" + 分类 + "\t\t\t|\t\t\t\t" + 评价);
                        }
                        break;
                    }
                    case 2: {
                        String sql = "insert into 电影(name,分类,评价) values (?,?,?)";
                        preparedStatement = connection.prepareStatement(sql);
                        P.print("请输入该电影的片名:");
                        name = s.next();
                        P.print("请输入该电影的分类:");
                        分类 = s.next();
                        P.print("请输入该电影的评价:");
                        评价 = s.nextInt();
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, 分类);
                        preparedStatement.setInt(3, 评价);
                        preparedStatement.executeUpdate();
                        break;
                    }
                    case 3: {
                        开关 = false;
                        break;
                    }
                }
            } while (开关);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null){
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
*/
        //方式四(神版):把获取数据库的连接封装为一个单独工具类JDBCUtils
        connection = JdbcUtil.getConnection();
//        connection = JDBCUtils.getConnections();
        statement = connection.createStatement();


        //使用Spring框架提供的方法实现增删改查操作,不需要自己实现增删改查的部分代码.
        //1,导入Spring的jar包
        //2,创建JdbcTemplate对象
//        jdbcTemplate = new JdbcTemplate(JDBCUtils.getDatasource());
        //3,具体用法在服务2和16服务出现


        //获取数据库元数据
        DatabaseMetaData databaseMetaData = connection.getMetaData();

        //获取数据库名称
        数据库 = connection.getCatalog();
        boolean Update = true;

        System.out.print("服务:\n" +
                "1,show tables                                           (显示该数据库的数据表)\n" +
                "2,select * from (表)                                    (显示该表的所有数据)\n" +
                "3,使用(切换)数据表 (表)\n" +
                "4,create table (表)(字段 数据类型)                         (创建数据表并使用)\n" +
                "5,alter table (表) add (字段名 数据类型)                   (为数据表添加字段)\n" +
                "6,insert into (表)(字段1[,字段2,..) values (?[,?,..)      (为表中字段插入数据)\n" +
                "7,drop table (表)                                       (删除数据表)\n" +
                "8,delete from (表) where (字段=?)                        (删除数据表指定的数据)\n" +
                "9,alter table (表) drop (字段)                           (删除数据表的字段)\n" +
                "10,alter table (表) rename to (新表)                     (修改数据表的名称)\n" +
                "11,alter table (表) change (字段) (新字段) (数据类型)       (修改数据表字段的名称或数据类型)\n" +
                "12,update (表) set 字段1 = ? where 字段2 = ?              (为表中指定条件修改数据)\n" +
                "13,select count(*) from (表)                             (查询该表有多少条数据)\n" +
                "14,select max(字段) from (表)                             (查询该表 最大值 与 最小字符串)\n" +
                "15,select min(字段) from (表)                             (查询该表 最小值 与 最大字符串)\n" +
                "16,select * from (表) where (字段)=?                      (指定条件查找数据)\n" +
                "17,select * from (表) where (字段)= ? or (字段) = ? or ... (指定多条条件查找数据)\n" +
                "18,关闭服务\n" +
                "如需要退当前操作,请输入 !                                 (本功能仅在服务代号上标注,在其他键入范围输入  !  有效!)\n"
        );
        System.out.println("\n正在使用数据库\t" + 数据库);
        cao = 1;
        //  }
        do {
            switch (cao) {
                case 1:
                    操作 = "1";
                    cao = 3;
                    break;//
                case 3:
                    操作 = "3";
                    cao = 333;
                    break;//进行选择数据表
                case 4:
                    操作 = "4";
                    cao = 444;
                    break;//进行创建数据表
                default: {
                    System.out.print("\n请求服务代号:\t");
                    操作 = s.next();
                    while (!Int(操作) || Integer.parseInt(操作) > 18 || Integer.parseInt(操作) <= 0) {
                        //进阶版判断方案
                        //这里判断必须用 || , 而且两边顺序不能倒!
                        System.out.print("输入有误,请重新输入 1~17:\t");
                        操作 = s.next();
                    }
                    break;
                }
            }
            switch (操作) {
                case "1": {
                    System.out.println("该库的数据表有:");
                    resultSet = databaseMetaData.getTables(数据库, "root", null, new String[]{"TABLE"});
                    //参数解析:1,数据库名称 2,数据库的登录名 3,表名称,可为null 4,类型标准
                    while (resultSet.next()) {
                        /*String tables = (String) resultSet.getObject("TABLE_NAME");
                        P.println(tables);*/
                        System.out.println("            |      " + resultSet.getString("TABLE_NAME") + "      |");
                    }
                    break;
                }
                case "2": {
                    System.out.println("正在使用表:  " + table_name);
                    //String[] 字段 = new String[100];
                    /*name();
                    if(table_name.equals("!")){
                        break;
                    }*/
                    sql = "select * from " + table_name;
                    //resultSet = statement.executeQuery(sql);
                    /*
                    //方式一:(操作繁琐,淘汰)
                    System.out.print("请输入所有字段:");
                    for (int i = 0; i < columnCount; i++) {
                        //字段[i] = s.next();
                    }
                    for (int i = 0; i < columnCount; i++) {
                        System.out.print(字段[i] + "      |      ");
                    }
                    System.out.println("");
                    while (resultSet.next()) {
                        for (int j = 0; j < columnCount; j++) {
                            a = resultSet.getString(字段(j));
                            System.out.print(a+"      |      ");
                        }
                        System.out.println("");
                        */
                    /*
                    //方式二:
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSetMetaData.getColumnName(i) + "      |      ");
                    }
                    System.out.println("");
                    while (resultSet.next()) {//每条数据
                        for (int j = 1; j <= columnCount; j++) {//每条数据里的各个字段的数据
                            a = resultSet.getString(resultSetMetaData.getColumnName(j));
                            System.out.print(a + "      |      ");
                        }
                        System.out.println("");
                    }*/

                    /*方式三:借鉴Spring的JdbcTemplate类的queryForList()的实现原理,自定义一个SQL_List()方法
                            该自定义方法SQL_List()无法顺序输出结果*/
                    //List<Map<String,Object>> list = SQL_List(sql);

                    //方式四:效果同方式三,将每一条数据封装为一个Map集合,再将Map集合装载到List集合中
                    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

                    for (Map<String, Object> map : list) {
                        System.out.println(map);
                    }

                    /*
                    //方式五:jdbcTemplate类提供的query()方法
                            该方法需要反射一个JavaBean类,该JavaBean类需要封装一个已知的数据表的数据信息
                            该方法适用于JDBC具体要实现的数据表事务,通过JavaBean类的toString()可以自定义输出格式
                    List<JdbcBean> list = jdbcTemplate.query(sql,new BeanPropertyRowMapper<JdbcBean>(JdbcBean.class));
                    for (JdbcBean jdbcBeanList : list){
                        System.out.println(jdbcBeanList);
                    }
                    */
                    break;
                }
                case "3": {
                    resultSet = databaseMetaData.getTables(数据库, "root", null, new String[]{"TABLE"});
                    if (!resultSet.next()) {
                        System.out.print("此数据库没有数据表,请创建!\t");
                        cao = 4;
                        break;
                    }
                    System.out.print("请输入需要使用(切换)的表:\t");
                    //static_tableName = s.next();配合name()使用的初级方案
                    table_name = s.next();
                    boolean have_table;
                    if (cao == 333) {
                        //此时没有数据表被选择
                        have_table = true;
                        do {
                            resultSet = databaseMetaData.getTables(数据库, "root", null, new String[]{"TABLE"});
                            while (resultSet.next()) {
                                if (table_name.equals(resultSet.getString("TABLE_NAME"))) {
                                    have_table = false;
                                    static_tableName = table_name;
                                    //进阶方案:记录该表,当下一次选择本服务并且中途取消该切换操服务作时,返回原本使用的数据表
                                    break;
                                }
                            }
                            if (have_table) {
                                System.out.print("未检测到数据库有数据表: " + table_name + "  请重新输入:\t");
                                table_name = s.next();
                            }
                        } while (have_table);
                        cao = 0;
                    } else {
                        have_table = true;
                        do {
                            resultSet = databaseMetaData.getTables(数据库, "root", null, new String[]{"TABLE"});
                            while (resultSet.next()) {
                                if (table_name.equals(resultSet.getString("TABLE_NAME"))) {
                                    static_tableName = table_name;
                                    have_table = false;
                                    break;
                                }
                            }
                            if (table_name.equals("!")) break;
                            if (have_table) {
                                System.out.print("未检测到数据库有  " + table_name + "  请重新输入:\t");
                                table_name = s.next();
                            }
                        } while (have_table);
                        if (table_name.equals("!")) {
                            table_name = static_tableName;
                        }
                    }
                    break;
                }
                case "4": {
                    System.out.print("请输入创建表名:\t");
                    table_name = s.next();
                    if (table_name.equals("!")) {
                        table_name = static_tableName;
                        break;
                    }
                    System.out.print("请输入字段:\t");
                    a = s.next();
                    if (a.equals("!")) {
                        table_name = static_tableName;
                        break;
                    }
                    System.out.print("请输入数据类型:\t");
                    b = s.next();
                    if (b.equals("!")) {
                        table_name = static_tableName;
                        break;
                    }
                    static_tableName = table_name;
                    sql = "create table " + table_name + "(" + a + "  " + b + ")";
                    //statement = connection.createStatement();
                    statement.executeUpdate(sql);
                    break;
                }
                case "5": {
                    /*name();
                    if ( table_name.equals("!")){
                        break;
                    }*/
                    try {
                        System.out.print("请输入字段:\t");
                        a = s.next();
                        if (a.equals("!")) break;
                        System.out.print("请输入数据类型:\t");
                        b = s.next();
                        if (b.equals("!")) break;
                        sql = "alter table " + table_name + "  add  " + a + "  " + b;
                        //statement = connection.createStatement();
                        statement.executeUpdate(sql);
                    } catch (Exception e) {
                        System.out.println("输入数据类型有误!\t" + e.getMessage());
                    }
                    break;
                }
                case "6": {
                    a = " ";
                    b = " ";
                    String[] date = new String[100];

                    for (int i = 1; i <= columnCount; i++) {
                        //进阶,获取所有字段名,为所有字段插入数据.
                        //优点,减少繁琐的逐个输入需要插入数据的字段名
                        //缺点,不能自定义需要插入数据的字段名
                        if (i < columnCount) {
                            a += resultSetMetaData.getColumnName(i) + "  ,  ";
                            b += "?,";
                        } else {
                            a += resultSetMetaData.getColumnName(i);
                            b += "?";
                        }
                    }
                    System.out.println("            |" + a + " |");
                    /*
                    String str = s.next();
                    while (!Int(str)||Integer.parseInt(str)>columnCount||Integer.parseInt(str)==0){
                        //进阶版判断方案
                        //这里判断必须用 || , 而且两边顺序不能倒!
                        System.out.print("输入有误,请重新输入 1~"+columnCount+"):\t");
                        str = s.next();
                    }*/
                    //为了防止输入字符而不是数值,所以while得在String转Int之前执行
                    /*do {
                    //初级判断方案
                        switch (str) {
                            case "1":
                            case "2":
                            case "3":
                            case "4":
                            case "5":
                            case "6":
                                str_num = false;
                                break;
                            default: {
                                System.out.print("输入有误,请重新输入 1~6 :");
                                str = s.next();
                                break;
                            }
                        }
                    }while (str_num);*/
                    /*int num = Integer.parseInt(str);*/
                    /*
                    //初级获取需插入数据的字段(由于需要自己逐个输入字段,淘汰!优点就是可以自定义哪些字段需要插入数据)
                    String[] 字段 = new String[100];
                    System.out.println("请输入需要插入数据的字段名称:\t");
                    for (int i = 0 ; i < num ; i++) {
                        字段[i] = s.next();
                        if (字段[i].equals("!")) {
                            a = null;
                            break;//清空a的值(防止sql语句里的a有值),退出当前操作(终止for循环)
                        }
                        if (i != num - 1) {
                            a += 字段[i] + ",";
                            b += "?,";
                        } else if (i == num - 1) {
                            a += 字段[i];
                            b += "?";
                        }
                    }
                    if(a==null) break;//退出当前操作(退出此switch的case操作)
                    */
                    sql = "insert into " + table_name + "(" + a + ") values (" + b + ")";
                    preparedStatement = connection.prepareStatement(sql);
                    do {
                        System.out.println("请按字段顺序插入相应的数据:\t");
                        for (int i = 0; i < 10000; i++) {
                            date[i] = s.next();
                            if (date[i].equals("!")) {
                                b = null;
                                break;//清空b的值,退出当前操作(终止for循环)
                            } else {
                                preparedStatement.setString(i + 1, date[i]);
                            }
                            if (i == (columnCount - 1)) break;//判断是否已经到达最后一个字段
                        }
                        if (b == null) break;//退出当前操作(退出此switch的case操作)
                        preparedStatement.executeUpdate();
                    } while (true);
                    break;
                }
                case "7": {
                    sql = "drop table  " + table_name;
                    statement.executeUpdate(sql);
                    System.out.println("删除   " + table_name + "    成功!");
                    table_name = null;
                    //static_tableName = null;搭配name()的初级方案(重置静态表名为空)
                    cao = 3;//进阶方案,重启"必选数据表"操作
                    break;
                }
                case "8": {
                    try {
                        System.out.print("条件字段名:\t");
                        a = s.next();
                        if (a.equals("!")) break;
                        System.out.print("条件字段值:\t");
                        b = s.next();
                        if (b.equals("!")) break;
                        sql = "delete from " + table_name + " where " + a + "='" + b + "'";
                        statement.executeUpdate(sql);
                        System.out.println("删除" + b + "成功!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "9": {
                    a = " ";
                    for (int i = 1; i <= columnCount; i++) {
                        if (i < columnCount) {
                            a += resultSetMetaData.getColumnName(i) + "  ,  ";
                        } else {
                            a += resultSetMetaData.getColumnName(i);
                        }
                    }
                    System.out.println("            |" + a + " |");
                    System.out.print("请输入该表需要删除的字段:\t");
                    b = s.next();
                    if (b.equals("!")) break;
                    boolean get = true;
                    while (get) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (b.equals(resultSetMetaData.getColumnName(i))) {
                                get = false;
                                break;
                            }
                        }
                        if (get) {
                            System.out.print("该表没有字段  " + b + "  请重新输入:");
                            b = s.next();
                        }
                        if (b.equals("!")) break;
                    }
                    if (b.equals("!")) break;
                    sql = " alter table " + table_name + " drop " + b;
                    statement.executeUpdate(sql);
                    System.out.println("删除" + b + "字段成功");
                    break;
                }
                case "10": {
                    System.out.print("请输入新表名:\t");
                    a = s.next();
                    if (a.equals("!")) break;
                    sql = "alter table " + table_name + " rename to " + a;
                    statement.executeUpdate(sql);
                    table_name = a;
                    static_tableName = a;
                    System.out.println("修改成功!");
                    break;
                }
                case "11": {
                    System.out.print("请输入需要修改的字段名:\t");
                    a = s.next();
                    if (a.equals("!")) break;
                    boolean get = true;
                    while (get) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (a.equals(resultSetMetaData.getColumnName(i))) {
                                get = false;
                                break;
                            }
                        }
                        if (get) {
                            System.out.print("该表没有字段  " + a + "  请重新输入:");
                            a = s.next();
                        }
                        if (a.equals("!")) {
                            break;
                        }
                    }
                    if (a.equals("!")) break;
                    System.out.print("请输入新字段名:\t");
                    b = s.next();
                    if (b.equals("!")) break;
                    try {
                        System.out.print("请输入该字段的数据类型(可对原数据类型进行修改):\t");
                        c = s.next();
                        if (b.equals("!")) break;
                        sql = "alter table " + table_name + " change " + a + " " + b + " " + c;
                        statement.executeUpdate(sql);
                    } catch (SQLException throwables) {
                        System.out.println("数据类型输入有误!\n" + throwables.getMessage());
                    }
                    break;
                }
                case "12": {
                    try {
                        System.out.print("请输入需要修改的数据所处的字段:\t");
                        b = s.next();
                        if (b.equals("!")) {
                            break;
                        }
                        System.out.print("请输入条件字段:\t");
                        c = s.next();
                        if (c.equals("!")) {
                            break;
                        }
                        sql = "update " + table_name + " set " + b + " = ? where " + c + " = ?";
                        preparedStatement = connection.prepareStatement(sql);
                        while (true) {
                            System.out.print("请输入新数据:\t");
                            String d = s.next();
                            if (d.equals("!")) {
                                break;
                            }
                            System.out.print("请输入条件字段的信息:\t");
                            String e = s.next();
                            if (e.equals("!")) {
                                break;
                            }
                            preparedStatement.setString(1, d);
                            preparedStatement.setString(2, e);
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException throwables) {
                        System.out.println(throwables.getMessage());
                    }
                    break;
                }
                case "13": {
                    int count = 0;
                    sql = "select count(*) from " + table_name;
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        count = resultSet.getInt(1);
                    }
                    System.out.println("该表有数据" + count + "条");
                    break;
                }
                case "14": {
                    Object max;
                    System.out.print("请输入需要查找的最大值(或最小字符串)的所在字段:\t");
                    a = s.next();
                    if (a.equals("!")) break;
                    boolean get = true;
                    while (get) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (a.equals(resultSetMetaData.getColumnName(i))) {
                                get = false;
                                break;
                            }
                        }
                        if (get) {
                            System.out.print("该表没有字段  " + a + "  请重新输入:");
                            a = s.next();
                        }
                        if (a.equals("!")) {
                            break;
                        }
                    }
                    if (a.equals("!")) break;
                    sql = "select max(" + a + ") from " + table_name;
                    resultSet = statement.executeQuery(sql);
                    try {
                        if (resultSet.next()) {
                            max = resultSet.getString(1);
                            sqls = "select * from " + table_name + " where " + a + "='" + max + "'";
                            resultSet = statement.executeQuery(sqls);
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(resultSetMetaData.getColumnName(i) + "      |      ");
                            }
                            System.out.println("");
                            while (resultSet.next()) {
                                for (int j = 1; j <= columnCount; j++) {
                                    a = resultSet.getString(resultSetMetaData.getColumnName(j));
                                    System.out.print(a + "      |      ");
                                }
                                System.out.println("");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "15": {
                    Object min;
                    System.out.print("请输入需要查找的最小值(或最大字符串)的所在字段:\t");
                    a = s.next();
                    if (a.equals("!")) break;
                    boolean get = true;
                    while (get) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (a.equals(resultSetMetaData.getColumnName(i))) {
                                get = false;
                                break;
                            }
                        }
                        if (get) {
                            System.out.print("该表没有字段  " + a + "  请重新输入:");
                            a = s.next();
                        }
                        if (a.equals("!")) {
                            break;
                        }
                    }
                    if (a.equals("!")) break;
                    try {
                        sql = "select min(" + a + ") from " + table_name;
                        resultSet = statement.executeQuery(sql);
                        if (resultSet.next()) {
                            min = resultSet.getString(1);
                            sqls = "select * from " + table_name + " where " + a + "='" + min + "'";
                            resultSet = statement.executeQuery(sqls);
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(resultSetMetaData.getColumnName(i) + "      |      ");
                            }
                            System.out.println("");
                            while (resultSet.next()) {
                                for (int j = 1; j <= columnCount; j++) {
                                    a = resultSet.getString(resultSetMetaData.getColumnName(j));
                                    System.out.print(a + "      |      ");
                                }
                                System.out.println("");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "16": {
                    System.out.print("请输入查找该数据的条件字段:\t");
                    a = s.next();
                    if (a.equals("!")) break;
                    System.out.print("请输入查找该数据的条件字段值:\t");
                    b = s.next();
                    if (b.equals("!")) break;
                    try {
                        sql = "select * from " + table_name + " where " + a + " ='" + b + "'";
                        resultSet = statement.executeQuery(sql);
                        /*
                        //方式一:
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(resultSetMetaData.getColumnName(i) + "      |      ");
                        }
                        System.out.println("");
                        while (resultSet.next()) {
                            for (int j = 1; j <= columnCount; j++) {
                                a = resultSet.getString(resultSetMetaData.getColumnName(j));
                                System.out.print(a + "      |      ");
                            }
                            System.out.println("");
                        }*/
                        /**/
                        /*方式二:借鉴Spring的JdbcTemplate类的queryForMap()的实现原理,自定义一个SQL_Map()方法
                                该自定义方法SQL_Map()无法顺序输出结果*/
                        //Map<String,Object> map = SQL_Map(sql);

                        /*方式三:效果同方式二
                          queryForMap():将查询结果集封装为Map集合,
                                        一个列名作为key,值作为value,
                                        这个方法只能查询一条数据*/
                        Map<String, Object> map = jdbcTemplate.queryForMap(sql);

                        System.out.println(map);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "17": {
                    try {
                        String[] key = new String[100];
                        String[] value = new String[100];
                        int times = 0;
                        String str = " ";
                        while (true) {
                            System.out.print("请输入查找该数据的条件字段:\t");
                            a = s.next();
                            if (a.equals("!")) {
                                times--;
                                break;
                            }
                            System.out.print("请输入查找该数据的条件字段值:\t");
                            b = s.next();
                            if (b.equals("!")) {
                                times--;
                                break;
                            }
                            key[times] = a;
                            value[times] = b;
                            times++;
                        }
                        for (int i = 0; i <= times; i++) {
                            if (i < times) {
                                str += key[i] + " = '" + value[i] + "' or ";
                            } else {
                                str += key[i] + " = '" + value[i] + "'";
                                sql = " select * from " + table_name + " where " + str;
                                //List<Map<String,Object>> list = SQL_List(sql);

                                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

                                for (Map<String, Object> stringObjectMap : list) {
                                    System.out.println(stringObjectMap);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "18": {
                    Update = false;
                    break;
                }
            }
            //刷新数据表信息
            if (table_name != null) {
                sql = "select * from " + table_name;
                resultSet = statement.executeQuery(sql);
                resultSetMetaData = resultSet.getMetaData();//获取resultSet结果集的元数据
                columnCount = resultSetMetaData.getColumnCount();
            }
        } while (Update);
    }
}
