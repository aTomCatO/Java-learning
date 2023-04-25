package Java.json;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.util.*;

/**
 * jackson的使用
 * 🧭Java转换JSON格式的字符串:
 * 1,writeValue(参数1,obj)
 * 参数1:
 * File:将obj对象转换为JSON字符串,并保存到指定得到文件中
 * Write:将obj对象转换为JSON字符串,并将JSON数据填充到字符输出流中
 * OutputStream:将obj对象转换为JSON字符串,并将JSON数据填充到字节输出流中
 * 2,writeValueAsString(ojb):将对象转为JSON字符串.
 * 🧭JSON格式字符串转换为Java对象:
 * readValue(json,obj)
 * 🧭注解:
 * 1,@JsonIgnore标注的属性不会序列化为JSON字符串
 * 2,@JsonFormat标注的属性其值会进行格式化(标注在时间类上)
 **/

/**
 * @author XYC
 */
public class Test {
    ObjectMapper objectMapper = new ObjectMapper();
    List<Book> list = new ArrayList<>();
    Map<String, Book> map = new HashMap<>();

    /**
     * 对象的序列化与反序列化
     */
    @org.junit.Test
    public void Object() throws JsonProcessingException {
        Book book = new Book("Java程序设计", 66, new Date());
        System.out.println("\n使用jackson:");
        String javaStr = objectMapper.writeValueAsString(book);
        System.out.println(javaStr);
        //反序列化为对象
        book = objectMapper.readValue(javaStr, Book.class);
        System.out.println(book);

        System.out.println("\n使用fastjson:");
        javaStr = JSON.toJSONString(book);
        System.out.println(javaStr);
        //反序列化为对象
        book = JSON.parseObject(javaStr, Book.class);
        System.out.println(book);
    }

    /**
     * list集合的序列化与反序列化
     */
    @org.junit.Test
    public void list() throws JsonProcessingException {
        list.add(new Book("Linux操作系统", 88, new Date()));
        list.add(new Book("design_mode", 68, new Date()));

        System.out.println("\n使用jackson:");
        String bookStr = objectMapper.writeValueAsString(list);
        System.out.println(bookStr);

        //反序列化为list
        List<Book> bookList =
                objectMapper.readValue(bookStr, new TypeReference<List<Book>>() {
                });
        for (Book book : bookList) {
            System.out.println(book);
        }

        System.out.println("\n使用fastjson:");
        bookStr = JSON.toJSONString(list);
        System.out.println(bookStr);
        bookList = JSON.parseArray(bookStr, Book.class);
        for (Book book : bookList) {
            System.out.println(book);
        }
    }

    /**
     * Map的序列化与反序列化
     */
    @org.junit.Test
    public void map() throws JsonProcessingException {
        map.put("Java", new Book("Java并发编程的艺术", 19, new Date()));
        map.put("大数据", new Book("大数据开发实战", 18, new Date()));

        System.out.println("\n使用jackson:");
        String bookStr = objectMapper.writeValueAsString(map);
        System.out.println(bookStr);
        //反序列化为map
        Map<String, Book> bookMap =
                objectMapper.readValue(bookStr, new TypeReference<Map<String, Book>>() {
                });
        System.out.println(bookMap);

        System.out.println("\n使用fastjson:");
        bookStr = JSON.toJSONString(map);
        //这里为了区分和Jackson的TypeReference而需要写全类名
        bookMap = JSON.parseObject(bookStr, new com.alibaba.fastjson.TypeReference<Map<String, Book>>() {
        });
        System.out.println(bookMap);
    }

    /**
     * 将数据写到指定的文件中
     */
    @org.junit.Test
    public void outDocument() throws Exception {
        Book girlFriend = new Book("C语言程序设计", 19, new Date());
        objectMapper.writeValue(new File("D:/JavaWorld/JSON.txt"), girlFriend);
    }

    @org.junit.Test
    public void main() throws JsonProcessingException {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("1", "2");
        map1.put("2", "2");

        System.out.println(objectMapper.writeValueAsString(map1));
    }
}
