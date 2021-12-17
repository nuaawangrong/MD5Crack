package MD5Crack;

import java.io.*;

public class LookupTableCrack {

    private static int len = 8;

    public static void main(String args[]) {

        String md5Value = "305ebab8b5f24b0947b25a95f003b93a";      //9999997

        String filePath = "./../MD5ValueRecords/";
        String fileName = "number_"+len+"位.txt";
        String file = filePath + fileName;

        long preTime = System.currentTimeMillis();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuffer sb;
            String[] str;
            while (in.ready()) {
                sb = (new StringBuffer(in.readLine()));
                str = sb.toString().split(" ");
                if(str[1].equals(md5Value)) {
                    System.out.println("采用查表法,破解成功,原值为:" + str[0]);
                    break;
                }
            }
            in.close();
        } catch (IOException e) {
        }

        long nowTime = System.currentTimeMillis();
        long costTime = nowTime - preTime;

        System.out.println("耗时:" + costTime  + "ms");
        System.out.println("耗时:" + costTime/1000f  + "s");
    }

    boolean crack(String password, String MD5Value, String file) {
        boolean crackSuccess = false;

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuffer sb;
            String[] str;
            while (in.ready()) {
                sb = (new StringBuffer(in.readLine()));
                str = sb.toString().split(" ");
                if(str[0].equals(password) && str[1].equals(MD5Value)) {
//                    System.out.println("采用查表法,破解成功,原值为:" + str[0]);
                    crackSuccess = true;
                    break;
                }
            }
            in.close();
        } catch (IOException e) {
        }
        if(!crackSuccess) return false;//破解不成功，直接返回-1

        return true;
    }
}