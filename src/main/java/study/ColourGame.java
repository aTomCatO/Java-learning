package study;


import java.util.Random;
import java.util.Scanner;

/**
 * @author XYC
 */
public class ColourGame {
    static Random r = new Random();
    static Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            //得到六合彩码
            int[] a = game();
            //得到玩家输入号码
            int[] b = play();
            start(a, b);
        }
    }

    public static int[] game() {
        int[] games = new int[6];
        //随机生成5个红球(1~26)
        for (int i = 0; i < games.length - 1; i++) {
            while (true) {
                int num = r.nextInt(26) + 1;
                //判断该随机数是否能使用(num没有重复),默认为true
                boolean unRepeat = true;
                //当i=0时,不进行以下循环
                for (int j = 0; j < i; j++) {
                    //如果这个num在前面已生成并储存于数组,将不能在使用
                    if (games[j] == num) {
                        unRepeat = false;
                        break;
                    }
                }
                //num如果没有重复,则可以使用
                if (unRepeat) {
                    games[i] = num;
                    break;
                }
            }
        }
        //随机生成第六个蓝球(1~16)
        games[5] = r.nextInt(16) + 1;
        return games;
    }

    public static int[] play() {
        int[] plays = new int[6];
        for (int i = 0; i < plays.length - 1; i++) {
            System.out.print("请输入第" + (i + 1) + "个红球数值(1~26):\t");
            plays[i] = s.nextInt();
        }
        System.out.print("请输入蓝球数值(1~16):\t");
        plays[5] = s.nextInt();
        return plays;
    }

    public static void start(int[] a, int[] b) {
        //计算红球压中的个数(红球总共有5个)
        int red = 0;
        //计算蓝球压中的个数(蓝球只有1个)
        int blue = 0;
        //双层循环,外层代表六合彩,内层代表玩家.判断玩家押注的号码是否压中某个六合彩码
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < b.length - 1; j++) {
                if (b[j] == a[i]) {
                    red++;
                    break;
                }
            }
        }
        if (a[5] == b[5]) {
            blue++;
        }
        System.out.print("\n本次六合彩的红球码为:\t");
        for (int n = 0; n < a.length - 1; n++) {
            System.out.print(a[n] + "\t");
        }
        System.out.print("蓝球码为:\t");
        System.out.print(a[5] + "\n");

        System.out.print("您输入的的红球码为:\t");
        for (int n = 0; n < a.length - 1; n++) {
            System.out.print(b[n] + "\t");
        }
        System.out.print("蓝球码为:\t");
        System.out.print(b[5] + "\n");

        System.out.println("红球压中\t" + red + "个");
        System.out.println("蓝球压中\t" + blue + "个");

        if (red == 3) {
            System.out.println("恭喜获得参与奖,1000人民币.");
        } else if (red == 2 & blue == 1) {
            System.out.println("恭喜获得三等奖!1万人民币.");
        } else if (red == 4 | red == 5) {
            System.out.println("恭喜获得二等奖!10万人民币!");
        } else if (red == 4 | red == 5 & blue == 1) {
            System.out.println("恭喜获得一等奖!100万人民币!!");
        } else if (red == 6 & blue == 1) {
            System.out.println("恭喜获得终极大奖!1000万人民币!!!");
        } else {
            System.out.println("感谢您的参与,再接再厉!");
        }
    }
}
