package demo;

import java.util.Random;

/**
 * @author XYC
 * 验证码生成
 */
public class ProveCode {
    public static void main(String[] args) {
        String code = createCode(6);
        System.out.println(code);
    }

    public static String createCode(int n) {
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        for (int m = 0; m < n; m++) {
            int type = r.nextInt(3);
            switch (type) {
                case 0: {
                    //随机生成一个大写字母
                    char a = (char) (r.nextInt(26) + 65);
                    code.append(a);
                    break;
                }
                case 1: {
                    //随机生成一个小写字母
                    char b = (char) (r.nextInt(26) + 97);
                    code.append(b);
                    break;
                }
                case 2: {
                    //随机生成1~9
                    code.append( r.nextInt(10));
                    break;
                }
                default:
                    break;
            }
        }
        return code.toString();
    }
}
