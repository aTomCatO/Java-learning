package demo;


import java.util.*;

/**
 * @author XYC
 * 只适合数据少的情况下,大量数据将浪费性能
 */
public class TableFormat {

    public List<List<String>> data = new ArrayList<>();

    public static void main(String[] args) {

        List<List<String>> data = new ArrayList<>();
        data.add(new ArrayList<String>() {{
            add("time");
            add("size");
            add("name");
            add("url");
        }});

        data.add(new ArrayList<String>() {{
            add("2017-07-05 17:58:52.0");
            add("8.33 MB");
            add("FraudDetection-12.0.jar");
            add("https://myqcloud.com/FraudDetection-12.0.jar");
        }});

        data.add(new ArrayList<String>() {{
            add("2016-12-18 16:24:05.0");
            add("202 MB");
            add("hadoop-2.7.2.tar.gz");
            add("https://myqcloud.com/hadoop-2.7.2.tar.gz");
        }});

        data.add(new ArrayList<String>() {{
            add("2017-07-10 17:07:16.0");
            add("13.03 KB");
            add("TableFormat.txt");
            add("https://myqcloud.com/stopwords.txt");
        }});

        data.add(new ArrayList<String>() {{
            add("2017-07-07 08:33:55.0");
            add("192 MB");
            add("text-classifier-10.0.jar");
            add("https://myqcloud.com/text-classifier-10.0.jar");
        }});

        new TableFormat(data).println(10);

    }

    public String getPlace(String text, Integer maxWidth) {
        int textSize = text.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxWidth - textSize; i++) {
            sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }

    public void println(Integer interval) {
        int width = data.get(0).size();
        int high = data.size();
        int[] maxWidths = getMaxWidth(width, high);
        for (int i = 0; i < high; i++) {
            for (int y = 0; y < width; y++) {
                String text = data.get(i).get(y);
                int maxWidth = maxWidths[y];
                if (y > 0) {
                    maxWidth += interval;
                }
                System.out.print(getPlace(text, maxWidth));
            }
            System.out.println();
        }
    }

    /**
     * 计算每一列每行内容的最大长度
     */
    public int[] getMaxWidth(Integer width, Integer high) {
        int[] widthArray = new int[width];
        for (int w = 0; w < width; w++) {
            String[] array = new String[high];
            for (int h = 0; h < high; h++) {
                array[h] = data.get(h).get(w);
            }
            widthArray[w] = getLengthMax(array);
        }
        return widthArray;
    }

    /**
     * 获取数组字符串中长度最大的值
     */
    public int getLengthMax(String[] arr) {
        int max = arr[0].length();
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].length() > max) {
                max = arr[i].length();
            }
        }
        return max;
    }

    public TableFormat(List<List<String>> data) {
        this.data = data;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}