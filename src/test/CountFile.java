package test;

import entity.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Description :
 * Copyright: Copyright (c)2019
 * Created Date : 2020/2/12
 */
public class CountFile
{
    public static void main(String[] args) throws IOException {
            String path = "G:\\Geolife Trajectories 1.3\\Data";
            File dir = new File(path);
            getData(dir);
            int totalCount = 0;
            for (File file : fileList) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bReader = new BufferedReader(read);
                String str;
                int count = 0;
                while ((str = bReader.readLine()) != null) {
                    count++;
                }
                if(count>40000){
                    System.out.println("文件名:"+file.getAbsolutePath()+",数量："+count);
                    totalCount += count;
                }
                bReader.close();
                read.close();
            }
        System.out.println(totalCount);
        }

       static List<File> fileList = new ArrayList();
        public static void getData(File dir){
            File[] files = dir.listFiles();
            for(File file:files){
                if(file.isFile()){
                    if(file.getName().endsWith(".plt")){
                        fileList.add(file);
                    }
                }else{
                    getData(file);
                }
            }
        }

}
