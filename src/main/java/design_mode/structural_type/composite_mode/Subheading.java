package design_mode.structural_type.composite_mode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XYC
 * 子标题(树叶对象)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subheading implements Title {
    private String title;

    @Override
    public String toString() {
        return "\t"+title+"\n";
    }
}
