package Java.reflect.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Cat {
    public String name = "Tom";
    private int age = 18;

    public Cat(String name) {
        this.name = name;
    }

    private Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void isAnimal() {
        System.out.println("它是<猫和老鼠>里的" + name + " 年龄: " + age);
    }
}
