package Java.json;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * fastjson的使用
 * 🧭注解：
 * 1、@JSONType() includes = {""} 指定的属性会被序列化
 * ignores = {""}  指定的属性不会被序列化
 * orders={}设置属性序列化的顺序
 * 2、@JSONField() ordinal=1 配置序列化的字段顺序
 * serialize=false 是否参与序列化(true/false),但是如果加了final，这个字段就无法被过滤
 * format="yyyy-MM-dd HH:mm:ss" 日期按照指定格式序列化
 * name="别名" 使用字段别名
 * serializeFeatures={SerializeFeatures属性} 序列化规则
 * parseFeatures={Features属性} 反序列化规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JSONType(orders = {"title", "price", "publicationTime"})
class Book {
    private String title;
    private Integer price;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd", name = "发行时间")
    private Date publicationTime;
}
