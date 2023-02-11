package Java.reflect.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class Dog {
    String name = "spike";


    public void isAnimal() {
        System.out.println("它是<猫和老鼠>里的" + name);
    }
}
