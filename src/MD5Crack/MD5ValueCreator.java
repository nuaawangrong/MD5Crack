package MD5Crack;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5ValueCreator {

    public static int len = 5;                     //测试用例明文长度
    public static boolean onlyNumber = false;      //是否有小写字母，无则只有数字
    public static char[] z ;                       //密码组成的字符数组

    public static void main(String args[]) throws Exception {

        String filePath = "./../MD5ValueRecords/";
        String fileName;
        if(onlyNumber) {
            fileName = "number_"+len+"位.txt";
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        } else {
            fileName = "lowercaseLetterAndNumber_" + len + "位.txt";
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                    'w', 'x', 'y', 'z'};
        }

        String file = filePath + fileName;

        System.out.println("z.length:" + z.length + "      len:" + len);
        System.out.println("passwordsAmount:" +(int) Math.pow(z.length, len));

        OutputStream fop = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk

        StringBuilder passwordBuilder;
        String password;
        double rateOfProgress;
        double passwordsAmount = Math.pow(z.length, len);

        int pre = -1;
        int now;
        long preTime = System.currentTimeMillis();
        long remainTime = -1;

        for (int i = 0; i < passwordsAmount; i++) {
            long nowTime = System.currentTimeMillis();
            long costedTime = nowTime - preTime;

            rateOfProgress = i/passwordsAmount;
            now = (int)(rateOfProgress*100)%100;
            if(now != 0) {
                remainTime = costedTime*(100-now)/now/1000;   //剩余时间,单位:s
            }
            if(now > pre) {
                System.out.println("rateOfProgress:" + now +"%" + " RemainTime:" + remainTime/60 + "Min " +remainTime%60 + "s");
            }
            pre = now;

            passwordBuilder = new StringBuilder();
            for (int j = len - 1; j >= 0; j--) {
                passwordBuilder.append(calculateCharacter(i, z.length, j));
            }
            password = passwordBuilder.toString();

            // 写入到缓冲区
            writer.append(password + " ");
            writer.append(md5(password));
            writer.append("\n");
            // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
        }

        writer.close();
        // 关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
        fop.close();
        // 关闭输出流,释放系统资源
    }

    public static String md5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        String res = new BigInteger(1, md.digest()).toString(16);
        if(res.length()<32) {
            String strZero = "";
            for(int i=0;i<32-res.length();i++) {
                strZero += "0";
            }
            res = strZero + res;
        }
        return res;
    }

    public static char calculateCharacter(int i, int sizeZ, int pow) {
        return z[(i / (int) Math.pow(sizeZ, pow)) % sizeZ];
    }
}