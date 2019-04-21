package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.util.ArrayList;

/**
 * @Author ccl
 * @Date 2019/4/20
 */
public class IncrePW {
    static double LimitDis;

    public static double getMaxDist(ArrayList<Point> beforeTraj,int index){
        Distance distance = new Distance();
        double maxdis=0,curdis;
        int left,right;
        boolean leftFlag,rightFlag;
        leftFlag = rightFlag = false;
        while(){



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
       double maxdis;
       for(int i=1;i<length;i++){






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
