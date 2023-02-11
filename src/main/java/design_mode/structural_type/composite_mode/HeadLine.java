package design_mode.structural_type.composite_mode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author XYC
 * 大标题(树枝对象)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeadLine implements Title {
    private final List<Title> bookList = new LinkedList<>();
    private String title;

    public void addBook(Title book) {
        bookList.add(book);
    }

    public void removeBook(Title book) {
        bookList.remove(book);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(title + "\n");
        for (Title book : bookList) {
            stringBuilder.append("\t").append(book);
        }
        return stringBuilder.toString();
    }
}
