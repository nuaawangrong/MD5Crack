package MD5Crack;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

public class RainbowTableCrack {

    public static int passwordLength;
    public static int chainLength;
    public static int chainAmount;
    public static int passwordAmount;
    public static boolean onlyNumber = true;

    public static  HashMap<String, String>  rainbowTable= new HashMap<>();
    public static  SortedSet<String> passwords = new TreeSet<>();
    public static String possibleEndValue;
    public static String startValue;
    public static String password;
    public static int amountOfRounds;

    public static char[] z;

    public static void main(String args[]) throws Exception {

        passwordLength = 3;
        chainLength = 5;
        chainAmount = 500;
        onlyNumber = true;

        getRainBowTableFromFile(passwordLength,chainAmount,chainLength,onlyNumber);

        String md5Value = "550a141f12de6341fba65b0ad0433500";    //444

        if(checkChain(md5Value)) {
//            System.out.println("Find End Value:" + possibleEndValue);

            //找到链条开始的明文值，对链条进行遍历
            Set<String> set = rainbowTable.keySet();
            for(String str:set) {
                String findValue = rainbowTable.get(str);
                if(findValue.equals(possibleEndValue)) {
                    startValue = str;
//                    System.out.println("StartValue:" + startValue);
                    if(executeHashReduction(startValue,md5Value)) {
                        break;
                    }
                }
            }
            password = (password==null ? "Not Found" : password);
            System.out.println("Password is : " + password);
        }
    }

    public static boolean executeHashReduction(String startValue,String md5Value) throws  Exception {
        //当找到可能存在明文的链时，从链首依次查找是否有正确的明文出现

        String result = startValue;
        String tempValue;

//        System.out.println("显示全链:");
//        for (int i = 0; i < chainLength; i++) {
//            tempValue = funcMD5(result);
//            System.out.println("i:" + i + " 明文:" + result + "  md5值:" + tempValue);
//            result = funcR(tempValue, i);
//        }

        result = startValue;
//        System.out.println("检查链 :");
        for (int i = 0; i < amountOfRounds; i++) {
            tempValue = funcMD5(result);
            if(tempValue.equals(md5Value)) {//已经找到明文
//                System.out.println("Find! result = " + result);
                password = result;
                return true;
            }
//            System.out.println("i:" + i + "   明文:" + result + "    md5值:" + tempValue);
            result = funcR(tempValue, i);
        }

        if(funcMD5(result).equals(md5Value)) {//找到了正确的明文
//            System.out.println("Find! result = " + result);
            password = result;
            return true;
        }
//        System.out.println("Not Find! result = " + result);
        return false;
    }

    public static boolean checkChain(String md5Value) throws Exception {
        //从尾部开始，重建哈希链，直到有节点与彩虹表中的任意终点相同，则可以视为找到可能存在明文密码的哈希链
        //如果一整条哈希链都没有发生碰撞，说明明文密码不存在于彩虹表中

        String endValue = "Not_Found";
        String  tempBigINTMd5Value;
        int counter = 0;
        while (counter < chainLength && !rainbowTable.containsValue(endValue)) {

            tempBigINTMd5Value = md5Value;
            endValue = funcR( tempBigINTMd5Value, chainLength - counter - 1);

            if (counter != 0) {
                for (int i = counter; i > 0; i--) {
                    tempBigINTMd5Value = funcMD5(endValue);
                    endValue = funcR(tempBigINTMd5Value, chainLength - i);
                }
            }
            amountOfRounds = chainLength - counter - 1;
            counter++;
        }
//        System.out.println("Counter:" + counter);
        if (rainbowTable.containsValue(endValue)) {
            possibleEndValue = endValue;
            return true;
        }
        amountOfRounds = -1;
        return false;
    }


    public static void createRainbowTable() throws Exception {
        //创建彩虹表
        //先创建一定数量的(随机)明文密码，作为链的开始
        generatePasswords();

        Iterator iterator = passwords.iterator();
        while (iterator.hasNext()){
            //对每条链计算出末尾值，将完成的链首链尾添加到彩虹表中
            String begin = (String) iterator.next();
            String end = calcEndValueOfChain(begin);
            rainbowTable.put(begin, end);
        }
//        System.out.println("彩虹表创建完成");
    }

    public static String calcEndValueOfChain(String password) throws Exception{
        //根据链首计算链尾
        //轮流施加函数funcR()和funcMD5()⽣成⼀条哈希链
        String result = password;

        for (int i = 0; i < chainLength; i++) {
            result = funcR(funcMD5(result), i);
        }
//        System.out.format("Chain begin:%s, Chain end:%s\n",password,result);
        return result;
    }

    public static String  funcR(String hashValue,int round) {
        BigInteger tempHashValue = new BigInteger(hashValue,16);
        BigInteger roundHashValue = tempHashValue.add(BigInteger.valueOf(round));
        BigInteger[] divideAndRemainders;
        StringBuilder result = new StringBuilder();

        for (int i = 1; i <= passwordLength; i++) {
            divideAndRemainders = roundHashValue.divideAndRemainder(BigInteger.valueOf(z.length));
            roundHashValue = divideAndRemainders[0];
            result.append(z[divideAndRemainders[1].intValue()]);
        }

//        System.out.print("R后值为:"+result.reverse().toString()+"  ");
        return result.reverse().toString();
    }

    public static String funcMD5(String str) throws Exception {
        //每次使用"new BigInteger(1, md.digest())"之前都必须更新md值,即"md.update(str.getBytes())";
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

    public static void generatePasswords() {
        //生成初始一系列的密码，作为链首

        int sizeZ = z.length;
        StringBuilder passwordBuilder;
        Random random = new Random();
        for (int i = 0; i < chainAmount; i++) {
            //使用随机生成
            int index = random.nextInt(passwordAmount);
//            System.out.println("随机生成开始密码");

            //采用前N个
//            int index = i;
//            System.out.println("固定前N个");

            passwordBuilder = new StringBuilder();
            for (int j = passwordLength - 1; j >= 0; j--) {
                passwordBuilder.append(calculateCharacter(index, sizeZ, j));
            }
            String pwd = passwordBuilder.toString();
            if(passwords.contains(pwd)) {//已经存在，继续生成
                i--;
            } else {//不存在，可以加入链首
                passwords.add(pwd);
            }
        }
    }

    public static char calculateCharacter(int i, int sizeZ, int pow) {
        return z[(i / (int) Math.pow(sizeZ, pow)) % sizeZ];
    }

    public static void createRainBowTableFile() throws IOException {
        //将彩虹表保存到文件中

        String filePath = "./../RainBowTableRecords/";
        String fileName;

        if(onlyNumber) {
            fileName = "number_"+passwordLength+"位_chainLength_"+chainLength+"_chainAmount_" +chainAmount+".txt";
        } else {
            fileName = "lowercaseLetterAndNumber_"+passwordLength+"位_chainLength_"+chainLength+"_chainAmount_" +chainAmount+".txt";
        }
        String file = filePath + fileName;
        OutputStream fop = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk

        String chainBegin, chainEnd;
        for (String key:rainbowTable.keySet()) {
            chainBegin = key;
            chainEnd = rainbowTable.get(key);

            writer.append(chainBegin + " " + chainEnd);
            writer.append("\n");
        }
        writer.close();
        fop.close();

        System.out.println("成功写入文件:" + fileName);
    }

    public static boolean getRainBowTableFromFile(int len, int chain_Amount,int chain_Length,boolean onlyNumber) throws Exception {
        // 从文件中读取彩虹表
        // 若不存在文件，则新建文件和彩虹表，并保存

        if(onlyNumber) {
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        } else {
            z = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                    'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                    'x', 'y', 'z'};
        }
        passwordLength = len;
        chainAmount = chain_Amount;
        chainLength = chain_Length;
        passwordAmount = (int)Math.pow(z.length,passwordLength);

        String filePath = "./../RainBowTableRecords/";
        String fileName;

        if(onlyNumber) {
            fileName = "number_"+len+"位_chainLength_"+chainLength+"_chainAmount_" +chainAmount+".txt";

        } else {
            fileName = "lowercaseLetterAndNumber_"+len+"位_chainLength_"+chainLength+"_chainAmount_" +chainAmount+".txt";
        }
        String file = filePath + fileName;

        File fl = new File(file);
//        System.out.println(fl.exists());
//        if(true) { //直接创建文件

        if(!fl.exists()) { //对应文件不存在，创建文件
            System.out.println("文件不存在 创建文件 filename : " + fileName);
            createRainbowTable();
            createRainBowTableFile();
        } else {//文件存在，打开文件
            System.out.println("文件存在  filename : " + fileName);
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                StringBuffer sb;
                String[] str;
                while (in.ready()) {
                    sb = (new StringBuffer(in.readLine()));
                    str = sb.toString().split(" ");

                    rainbowTable.put(str[0], str[1]);
                }
                in.close();
            } catch (IOException e) {
            }
        }
        return true;
    }

    public static boolean crack(String pwd,String MD5Value) throws Exception {
        password = "";

        if(checkChain(MD5Value)) {
//            System.out.println("Find End Value:" + possibleEndValue);
//            System.out.println("amountOfRounds : " + amountOfRounds);
                Set<String> set = rainbowTable.keySet();
                for(String str:set) {
                    String findValue = rainbowTable.get(str);

                    if(findValue.equals(possibleEndValue)) {   //找到链条开始的明文值，对链条进行遍历
                        startValue = str;
                        if(executeHashReduction(startValue,MD5Value)) {
                            break;
                        }
                    }
                }

                if(password.equals(pwd)) {
//                System.out.println("彩虹表破解成功,明文:" + password);
                    return true;
                }
        }
//                System.out.println("Password Not Found ！！！");
        return false;
    }
}