package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.util.ArrayList;

/**
 * 基本思想：假定每一个轨迹点是特征点，求该特征点到两边点的
 * 垂直距离，若距离大于阈值则判定该点是特征点，否则不是特征
 * 点。
 * @Author ccl
 * @Date 2019/4/20
 */
public class IncrePW {
    static double LimitDis;

    public static double getMaxDist(ArrayList<Point> beforeTraj,int index,int tzd){
        Distance distance = new Distance();
        double maxdis=0,curdis;
        int left = index - 1;
        int right = index + 1;
        boolean leftFlag,rightFlag;
        int lenght = beforeTraj.size();
        leftFlag = rightFlag = true;
        Point pc = beforeTraj.get(index);
        while(leftFlag||rightFlag){
            if(left>=0&&leftFlag){
                Point pa = beforeTraj.get(left);
                Point pb = beforeTraj.get(right);
                curdis = distance.getDistance(pa,pb,pc);
                if(maxdis<curdis){
                   maxdis = curdis;
                }else {
                    left++;
                    leftFlag = false;
                }
            }
            if(right<lenght){
                Point pa = beforeTraj.get(left);
                Point pb = beforeTraj.get(right+1);
                curdis = distance.getDistance(pa,pb,pc);
                if(maxdis<curdis){
                    maxdis = curdis;
                }else{
                    right--;
                    rightFlag = false;
                }
            }
            if(left<=0)leftFlag = false;
            //left不能越过上一个特征点
            left = left>tzd?left--:tzd;
            if(rightFlag)
            right = right<lenght?++right:(lenght-1);
        }
        return maxdis;
    }

    public static ArrayList<Point> IncrementPWAlgorithm(ArrayList<Point> beforeTraj){
       ArrayList<Point> afterTraj = new ArrayList<Point>();
       int length = beforeTraj.size();
       int left,right;
       boolean leftFlag = true;
       boolean rightFlag = true;
       double result = 0;
       int tzd = 0;//特征点
       double maxdis = 0,curdis;
       for(int i=1;i<length;i++){
           curdis = getMaxDist(beforeTraj,i,tzd);
           //不是特征点
           if(maxdis<curdis){
               maxdis = curdis;
           }else{
               if(maxdis>LimitDis){
                   afterTraj.add(beforeTraj.get(i));
               }
               maxdis = 0;
           }
       }
       return afterTraj;
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        ArrayList<Point> afterTraj = null;
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        GetTime getTime = new GetTime();
        LimitDis = (float) 7.8000298;
        beforeTraj =getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        afterTraj = IncrementPWAlgorithm(beforeTraj);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("IncrePW算法");
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }

}
