package test;

import java.io.*;

/**
 * @version 1.0
 * @Description :
 * Copyright: Copyright (c)2019
 * Created Date : 2020/2/12
 */
public class CountNode {
    public static void main(String[] args) throws IOException
    {
        String path = "F:\\trajs";
        File dir = new File(path);
        File []files = dir.listFiles();
        int count = 0;
        for(File file : files){
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            while(reader.readLine()!=null){
                count++;
            }
            reader.close();
            inputStreamReader.close();
        }
        System.out.println(count);
    }




}
