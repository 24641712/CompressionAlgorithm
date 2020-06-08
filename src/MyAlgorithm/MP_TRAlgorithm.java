package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @version 1.0
 * @Description :
 * Copyright: Copyright (c)2019
 * Created Date : 2020/3/29
 */
public class MP_TRAlgorithm {

    private final static double DPLimitDist1 = (float) 0.000673010500;
    private static double IALimitDist2 = (float) 14.0000;
    static int index = 0;
    static List<Point> afterTraj = new ArrayList<Point>();
    static List<Point> afterTraj_copy = new ArrayList<Point>();

    /*
     *道格拉斯-普克（Algorithm.DP）算法
     *@param beforeTraj 源轨迹集合
     *@param start 起始点
     *@param end 终止点
     *@return void
     **/
    static void DPAlgorithm(List<Point> beforeTraj,int start,int end){
        double maxdis,curdis;
        int i = 0,maxNO = 0;
        Distance distance = new Distance();
//        System.out.print("start="+start+"  end="+end);
        Point pa = beforeTraj.get(start);
        Point pb = beforeTraj.get(end);
        if(end-start >= 2){
            maxdis = 0;
            i=start+1;
            while(i < end){
                Point pc = beforeTraj.get(i);
                curdis = distance.getDistance(pa,pb,pc);
                System.out.println("DP---->"+curdis);
                if(maxdis < curdis) {
                    maxdis = curdis;
                    maxNO = i;
                }
                i++;
            }//end while
            if(maxdis >= DPLimitDist1) {
                afterTraj.add(beforeTraj.get(maxNO));
                DPAlgorithm(beforeTraj,start,maxNO);
                DPAlgorithm(beforeTraj,maxNO,end);
            }
        }
    }

    /*
     *求特征点tzd与第k个点间的最大垂直欧氏距离
     *@param beforeTraj 源轨迹点
     *@param tzd 特征点
     *@param k 轨迹点
     *@return 最大欧式距离
     **/
    public static double getMaxDist(List<Point> beforeTraj,int tzd,int k){
        double maxdist = 0;
        double curdist = 0;
        Point pa = beforeTraj.get(tzd);
        Point pb = beforeTraj.get(k);
        Distance distance = new Distance();
        for(int i=tzd+1;i<k;i++){
            Point pc = beforeTraj.get(i);
            curdist = distance.getSedDist(pa,pb,pc);
            if(maxdist<curdist){
                maxdist = curdist;
                index = i;
            }
        }
        return maxdist;
    }

    /*
     *最大欧式距离增加到最大时的轨迹点就是特征点
     *@param beforeTraj 源轨迹点
     *@return 压缩后的轨迹点
     **/
    public static void IncrementPWAlgorithm(List<Point> beforeTraj){
        int length = beforeTraj.size();
        int tzd = 0;//特征点
        double maxdist = 0,curdist;
        int count = 0;
        afterTraj.add(beforeTraj.get(tzd));
        afterTraj_copy.add(beforeTraj.get(tzd));
        for(int i=2;i<length;i++){
            System.out.println(count++);
            curdist = getMaxDist(beforeTraj,tzd,i);
            System.out.println("MP-TR---->"+curdist);
            if(curdist>=14){
                System.out.println(curdist);
            }
            //不是特征点
            if(maxdist <= curdist){
                maxdist = curdist;
            }else{
                if(maxdist > IALimitDist2){
                    tzd = index;
                    i = index+2;
                    //获得轨迹点
                    afterTraj.add(beforeTraj.get(index));
                    afterTraj_copy.add(beforeTraj.get(index));
                }
                maxdist = 0;
            }
        }
    }

    /*
     *对压缩后的轨迹点进行二次压缩
     *@param beforeTraj
     *@return
     **/
    public static void second_Compress_Tragectory(List<Point> beforeTraj){
        for(int i=0;i<afterTraj_copy.size()-1;i++){
            int left = afterTraj_copy.get(i).getPid();
            int right = afterTraj_copy.get(i+1).getPid();
            DPAlgorithm(beforeTraj,left,right);
        }
    }

    public static void main(String[] args) throws Exception {
        List<Point> beforeTraj = new ArrayList<Point>();
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        GetTime getTime = new GetTime();
        beforeTraj =getData.getDataFromFile(1000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        //极大值点
        IncrementPWAlgorithm(beforeTraj);
        Collections.sort(afterTraj_copy);
        //二次压缩
        second_Compress_Tragectory(beforeTraj);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("压缩后的轨迹点数："+afterTraj.size());
        System.out.println("MP-TR算法");
        System.out.println("压缩前的轨迹点数："+beforeTraj.size());
        System.out.println("过滤后的轨迹点数："+afterTraj.size());
        System.out.println("*********************");
        getTime.showTime();

        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }

}
