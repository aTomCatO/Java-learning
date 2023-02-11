package study;

import java.util.*;

import Util.*;

/**
 * @author XYC
 * 狼人杀
 */
public class WerewolfKill {
    private static String abc1, abc2;

    public static void main(String[] args) {
        String[] cards = {"狼人", "狼人", "村民", "村民", "村民", "預言家", "女巫"};
        String[] player = new String[7];
        //设置投票数
        int[] poll = new int[7];
        int[] random = {8, 8, 8, 8, 8, 8, 8};
        //给每一个位置放值
        int a = 0;
        //复活药水
        int p1 = 1;
        //毒药
        int p2 = 1;
        //判断游戏继续/结束
        boolean flag = true;
        do {
            //如果有相同的随机数就+1
            int count = 1;
            int j = (int) (Math.random() * 7);
            for (int i = 0; i < random.length; i++) {
                if (j == random[i]) {
                    count++;
                }
            }
            if (count == 1) {
                random[a] = j;
                a++;
            }
        } while (a != 7);
        //随机设置身份
        for (int i = 0; i < cards.length; i++) {
            player[i] = cards[random[i]];
        }
        //输出每个人的身份
        for (int i = 0; i < player.length; i++) {
            System.out.println((i + 1) + "号玩家的身份为:\t" + player[i]);
        }
        int Y = 0;
        int N = 0;
        for (int i = 0; i < player.length; i++) {
            if (player[i].equals("预言家")) {
                Y = i;
            } else if (player[i].equals("女巫")) {
                N = i;
            }
        }
        do {
            Scanner s = new Scanner(System.in);
            System.out.println("\n天黑,请闭眼\n");
            System.out.print("狼人请选择暗杀几号玩家:");
            int num = s.nextInt();
            System.out.print("预言家请选择要查验谁的身份:");
            if (!player[Y].equals("淘汰")) {
                int num1 = s.nextInt();
                System.out.println(num1 + "号的身份为" + player[num1 - 1]);
            }
            System.out.print("女巫选择是否使用复活药水(y/n):\t");
            //今晚以被狼人暗杀预淘汰1人
            int b = 1;
            int c = 0;

            if (!player[N].equals("淘汰")) {
                if (p1 != 0) {
                    abc1 = s.next();
                    if (abc1.equals("y")) {
                        b = 0;
                        p1 = 0;
                    }
                }
            }
            System.out.print("请选择是否使用毒药(y/n):\t");
            if (!player[N].equals("淘汰")) {
                if (p2 != 0 & !abc1.equals("y")) {
                    abc2 = s.next();
                    if (abc2.equals("y")) {
                        System.out.print("请选择要毒杀几号玩家:");
                        c = s.nextInt();
                        //今晚以被狼人暗杀淘汰1人+被女巫毒杀1人
                        b = 2;
                        p2 = 0;
                    }
                }
            }
            abc1 = "n";
            System.out.println("");
            if (b == 0) {
                System.out.println("\n昨晚是平安夜\n");
            } else if (b == 1) {
                System.out.println("\n" + num + "号玩家淘汰\n");
                player[num - 1] = "淘汰";
            } else {
                System.out.println(num + "号玩家淘汰");
                player[num - 1] = "淘汰";
                System.out.println(c + "号玩家淘汰\n");
                player[c - 1] = "淘汰";
            }
            //设置投票数
            for (int i = 0; i < player.length; i++) {
                if (!player[i].equals("淘汰")) {
                    System.out.print((i + 1) + "号玩家请投票:");
                    int select = s.nextInt();
                    switch (select - 1) {
                        case 0:
                            poll[0]++;
                            break;
                        case 1:
                            poll[1]++;
                            break;
                        case 2:
                            poll[2]++;
                            break;
                        case 3:
                            poll[3]++;
                            break;
                        case 4:
                            poll[4]++;
                            break;
                        case 5:
                            poll[5]++;
                            break;
                        case 6:
                            poll[6]++;
                            break;
                        default:
                            break;
                    }
                }
            }
            //回去最大投票数的下标
            int max = poll[0];
            int w = 0;
            for (int i = 0; i < poll.length; i++) {
                if (poll[i] > max) {
                    max = poll[i];
                    w = i;
                }
            }
            //判断被投死的玩家
            System.out.println("\n" + (w + 1) + "号被投死\n");
            player[w] = "淘汰";
            for (int i = 0; i < player.length; i++) {
                System.out.println((i + 1) + "号玩家的身份为:\t" + player[i]);
            }
            //刷新(重置)票池
            poll = new int[7];
            int end1 = 0;
            int end2 = 0;
            for (int i = 0; i < player.length; i++) {
                if (player[i].equals("狼人")) {
                    end1++;
                } else if (player[i].equals("村民")) {
                    end2++;
                }
            }
            if (end1 == 0) {
                System.out.println("\n游戏结束,村民胜利");
                flag = false;
            } else if (end1 <= end2) {
                System.out.println("\n游戏继续");
            } else if (end2 == 0) {
                System.out.println("\n游戏结束,狼人胜利");
                flag = false;
            }
        } while (flag);
    }
}