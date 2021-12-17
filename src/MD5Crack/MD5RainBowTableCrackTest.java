package MD5Crack;

import java.io.*;
import java.util.Date;

public class MD5RainBowTableCrackTest {
    public static int len = 4;
    public static int testValueAmout = 1000;
    public static boolean onlyNumber = true;

    public static int chainLength;
    public static int chainAmount;

    public static void main(String[] args) throws Exception {

        int totalAmount = (int) Math.pow(10,len);
//        int chainL[] = new int[] {100000,200000,500000,1000000,2000000};
//        int chainA[] = new int[] {50,100,200,500};
//        int chainL[] = new int[] {1};
//        int chainA[] = new int[] {10000000};

//        int chainL[] = new int[] {1,2,5,10,20,40,50,100,200,};
//        int chainL[] = new int[] {6000,8000};
//        int chainA[] = new int[] {10,20,30,50};

        int chainL[] = new int[] {6000};
        int chainA[] = new int[] {100};


        for(int i=0;i<chainL.length;i++) {
            for (int j=0;j<chainA.length;j++)
            {
                chainLength = chainL[i];
                chainAmount = chainA[j];
                int tempAmount = chainLength*chainAmount;
                if( tempAmount < totalAmount ||  tempAmount > 100*totalAmount) {
                    //彩虹表中数据过少或过多，放弃测试
                    continue;
                }
                System.out.format("chainLength:%d, chainAmount:%d\n",chainLength,chainAmount);
                rainBowTableCrackTest();
            }
        }
    }

    public static void rainBowTableCrackTest() throws Exception {

        int crackedCnt = 0;
        int uncrackedCnt = 0;
        double totalCostedTime = 0.0;

        boolean crackSuccess = false;
        String filePath = "./../TestValueRecords/";
        String fileLogPath = "./../TestValueRecords/RainBowLog_Win/";

        String fileName;
        String fileNameTestLog;
        if(onlyNumber) {
            fileName =  "test_number_"+len+"位_"+testValueAmout+".txt";
            fileNameTestLog = "test-4-rainBowTableTestLog_number_"+len+"位_"+ testValueAmout +"_chainLength_"+chainLength+"_chainAmount_" +chainAmount+".txt";
        } else {
            fileName = "test_lowercaseLetterAndNumber_"+len+"位_"+testValueAmout+".txt";
            fileNameTestLog = "test-4-rainBowTableTestLog_lowercaseLetterAndNumber_"+len+"位_"+ testValueAmout +"_chainLength_"+chainLength+"_chainAmount_" +chainAmount+".txt";
        }
        String file = filePath + fileName;
        String logFile = fileLogPath + fileNameTestLog;
        String tempStr;

        RainbowTableCrack rainbowTableCrack = new RainbowTableCrack();
        rainbowTableCrack.getRainBowTableFromFile(len,chainAmount,chainLength,onlyNumber);

        OutputStream fop = new FileOutputStream(logFile);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");

        Date date = new Date();
//        System.out.println(date.toString());
//        System.out.format("len:%d\ntestValueAmout:%d\nfile:%s\ntestLogfile:%s\n",len,testValueAmout,file,logFile);

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

            //彩虹表破解分析
//            System.out.format("chainAmount:%d, chainLength:%d\n", chainAmount, chainLength);
//            System.out.println("彩虹表破解: " + i + "/" + testValueAmout);
            crackSuccess = rainbowTableCrack.crack(MD5Value[i][0], MD5Value[i][1]);

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
//            System.out.format("costTimeArr[%d]:%.2fms,已成功破解%d个\n",i+1,costTimeArr[i],crackedCnt);
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
//        System.out.println(date.toString());
        writer.append(date.toString()+"\n");
        System.out.println("-----------------------------------------------------------------------------------");
        writer.append("-----------------------------------------------------------------------------------");

        writer.close();
        fop.close();
    }
}