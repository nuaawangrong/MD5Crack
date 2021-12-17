package MD5Crack;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class testCasesCreator {

    public static int len = 4;                    //测试用例明文长度
    public static int testValueAmout = 1000;     //测试用例数量
    public static boolean onlyNumber = true;      //是否有小写字母，无则只有数字

    public static char[] z ;

    public static void main(String args[]) throws Exception {

        String filePath = "./../TestValueRecords/";
        String fileName;
        if(onlyNumber) {
            fileName =  "test_number_"+len+"位_"+testValueAmout+".txt";
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        } else {
            fileName = "test_lowercaseLetterAndNumber_"+len+"位_"+testValueAmout+".txt";
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z'};
        }
        System.out.println("Passwords.length:" +(int) Math.pow(z.length, len));

        String file = filePath + fileName;
        OutputStream fop = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk

        StringBuilder passwordBuilder;
        String password;
        double passwordAmount = Math.pow(z.length, len);

        Set indexSet = new HashSet<>();
        int[] indexArr = new int[testValueAmout];
        Random random = new Random();

        for(int i=0;i<testValueAmout;i++) {
            System.out.println("index creating i:" + i);
            int temp = random.nextInt((int) passwordAmount);

            if(indexSet.contains(temp)) {//初始密码中已有，则不添加，继续创建下一个
                i--;
                continue;
            } else {
                indexSet.add(temp);
              indexArr[i] = temp;
            }
        }
        System.out.println("index create finished.");

        for (int i = 0; i < testValueAmout; i++) {
            passwordBuilder = new StringBuilder();
            for (int j = len - 1; j >= 0; j--) {
                passwordBuilder.append(calculateCharacter(indexArr[i], z.length, j));
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
        if(res.length()<32) {   //防止生成的hash值前因为有0不显示,自动补上
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