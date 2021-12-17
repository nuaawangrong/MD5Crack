package MD5Crack;

import java.io.*;
import java.util.Date;

public class MD5LookTableCrackTest {

    public static int len;
    public static int testValueAmout = 1000;
    public static boolean onlyNumber = true;

    public static void main(String[] args) throws Exception {

        int[] lenArr = {5};
        int[] testValueAmoutArr = {1000, 10000};

        //纯数字
        for(int i=0;i<lenArr.length;i++) {
            for(int j=0;j<testValueAmoutArr.length;j++) {
                len = lenArr[i];
                testValueAmout = testValueAmoutArr[j];
                lookTableCrackTest();
            }
        }

    }

    public static void lookTableCrackTest() throws Exception {
        int crackedCnt = 0;
        int uncrackedCnt = 0;
        double totalCostedTime = 0.0;

        boolean crackSuccess;
        String filePath = "./../TestValueRecords/";
        String fileName;
        String fileNameTestLog;
        if(onlyNumber) {
            fileName =  "test_number_"+len+"位_"+testValueAmout+".txt";
            fileNameTestLog = "testLog_number_"+len+"位_随机"+testValueAmout+"个_Linux.txt";
        } else {
            fileName = "test_lowercaseLetterAndNumber_"+len+"位_"+testValueAmout+".txt";
            fileNameTestLog = "testLog_lowercaseLetterAndNumber_"+len+"位_随机"+testValueAmout+"个_Linux.txt";
        }
        String file = filePath + fileName;
        String logFile = filePath + fileNameTestLog;
        String tempStr;

        OutputStream fop = new FileOutputStream(logFile);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");

        Date date = new Date();
        System.out.println(date.toString());
        System.out.format("len:%d\ntestValueAmout:%d\nfile:%s\n",len,testValueAmout,file);

        writer.append(date.toString() + "\n");
        tempStr = String.format("len:%d\ntestValueAmout:%d\nfile:%s\n",len,testValueAmout,file);
        writer.append(tempStr +"\n");

        double[] costTimeArr = new double[testValueAmout];     //花费的时间数组
        String[][] MD5Value = new String[testValueAmout][2];   //password And MD5Value
        int cnt = 0;

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuffer sb;
            String[] str;
            while (in.ready()) {
                sb = (new StringBuffer(in.readLine()));
                str = sb.toString().split(" ");
                MD5Value[cnt][0] = str[0];
                MD5Value[cnt][1] = str[1];

                cnt++;
            }
            in.close();
        } catch (IOException e) {
        }

        for (int i=0;i<testValueAmout;i++) {
            long preTime = System.currentTimeMillis();

            //查表破解分析
            LookupTableCrack lookupTableCrack = new LookupTableCrack();    //查表破解
//            System.out.println("查表破解: " + i + "/" + testValueAmout);
            writer.append("查表破解: " + i + "/" + testValueAmout+"\n");

            String filepath = "./../MD5ValueRecords/";
            String fileOfLookTable;
            if(onlyNumber) {
                fileOfLookTable =  filepath + "number_"+len+"位.txt";
            } else {
                fileOfLookTable =  filepath + "lowercaseLetterAndNumber_"+len+"位.txt";
            }

            crackSuccess = lookupTableCrack.crack(MD5Value[i][0],MD5Value[i][1],fileOfLookTable);

            long nowTime = System.currentTimeMillis();
            long costedTime = nowTime - preTime;
            costTimeArr[i] = costedTime;
            totalCostedTime += costTimeArr[i];

            if(crackSuccess) {
//                System.out.println("破解成功   password = " + MD5Value[i][0]);
                writer.append("破解成功   password = " + MD5Value[i][0] + '\n');
                crackedCnt++;
            } else {
//                System.out.println("破解失败");
                writer.append("破解失败\n");
                uncrackedCnt++;
            }
            System.out.format("costTimeArr[%d]:%.2fms,已成功破解%d个\n",i+1,costTimeArr[i],crackedCnt);
            tempStr = String.format("costTimeArr[%d]:%.2fms,已成功破解%d个\n",i+1,costTimeArr[i],crackedCnt);
            writer.append(tempStr);
        }

        System.out.format("破解结束:  测试数据共%d个\n",testValueAmout);
        tempStr = String.format("\n破解结束:  测试数据共%d个\n",testValueAmout);
        writer.append(tempStr);
        System.out.format("成功%d个,  失败%d,  总计耗时:%.2fms,  平均耗时:%.2fms\n",crackedCnt,uncrackedCnt,totalCostedTime,totalCostedTime/testValueAmout);
        tempStr = String.format("成功%d个,  失败%d,  总计耗时:%.2fms,  平均耗时:%.2fms\n",crackedCnt,uncrackedCnt,totalCostedTime,totalCostedTime/testValueAmout);
        writer.append(tempStr);
        date = new Date();
        System.out.println(date.toString());
        writer.append(date.toString()+"\n");
        System.out.println("-----------------------------------------------------------------------------------");
        writer.append("-----------------------------------------------------------------------------------");

        writer.close();
        fop.close();
    }
}