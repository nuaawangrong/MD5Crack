package MD5Crack;

import java.math.BigInteger;
import java.security.MessageDigest;

public class ForceCrack {

    public static char[] z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

//    public static char[] z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
//            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
//            'w', 'x', 'y', 'z'};


    public static void main(String args[]) throws Exception {
        int len = 5;
        int length = (int)Math.pow(len,10);

//        String md5Value = "e140dbab44e01e699491a59c9978b924";      //5013
//        String md5Value = "305ebab8b5f24b0947b25a95f003b93a";      //9999997
        String md5Value = "0ffacceba608e3f882f31fce5fca741d";      //077728

        long timeChuo1 = System.currentTimeMillis();
        String pwd;

        for(int i=0; ; i++) {

            if(i >= length) {
                System.out.println("破解失败");
                break;
            }

            StringBuilder passwordBuilder = new StringBuilder();
            for (int j = len - 1; j >= 0; j--) {
                passwordBuilder.append(calculateCharacter(i, z.length, j));
            }
            pwd = passwordBuilder.toString();
            String md5value = md5(pwd);
//            System.out.format("进度 :  %d/%d    password=%s    MD5Vaule=%s\n",i+1,length,pwd,md5value);
            if( md5Value.equals(md5value)) {
                System.out.println("采用暴力破解法,破解成功,原值为:" + i);
                break;
            }
        }

        long timeChuo2 = System.currentTimeMillis();
        long timeCha = timeChuo2 - timeChuo1;
        System.out.println("采用暴力破解法,破解次数:" + crackCount);
        System.out.println("耗时:" + timeCha  + "ms");
        System.out.println("耗时:" + timeCha/1000f  + "s");
    }

    public static Integer crackCount = 0;

    public static String md5(String str) throws Exception {
        /**
         * 每调用一次md5加密算法，就将破解计数器加一
         */
        crackCount++;
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

    public static boolean crack(String password, String MD5Value, int len,Boolean onlyNumber) throws Exception {
        int possiblePwdAmount;

        if(onlyNumber) {
            possiblePwdAmount = (int)Math.pow(10,len);
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        } else {
            possiblePwdAmount = (int)Math.pow(36,len);
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                           'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                           'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                           'w', 'x', 'y', 'z'};
        }

        String pwd;

        for(int i=0; ; i++) {
//            System.out.println("i = " + i);
            if(i >= possiblePwdAmount) {
                System.out.println("破解失败:" + password);
                return false;
            }

            StringBuilder passwordBuilder = new StringBuilder();
            for (int j = len - 1; j >= 0; j--) {
                passwordBuilder.append(calculateCharacter(i, z.length, j));
            }
            pwd = passwordBuilder.toString();

            if( MD5Value.equals(md5(pwd))) {
                System.out.println("采用暴力破解法,破解成功,原值为:" + pwd);
                break;
            }
        }

        return  true;
    }

}