package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SQUISH_E算法
 * @Author ccl
 * @Date 2019/3/5
 */
public class SQUISH_E {
    static double maxdis;

    public static  ArrayList<Point> SQUISH_EAlgorithm(List<Point> beforeTraj
                           ,double ratio,double maxdis){
        ArrayList<Point> afterTraj = new ArrayList<>();
        int []value = new int[1024];
        int capacity = 4;//设置优先级队列的初始容量
        for(int i=0;i<beforeTraj.size();i++){
            if(i/ratio > capacity)capacity++;//增加优先队列的容量
            beforeTraj.get(i).setPriority(0);
            afterTraj.add(beforeTraj.get(i));
            //新加入点不是第一个点，调整其前一个点的优先级
            if(i>1){
                //调整优先级队列中的优先级
                adjust_priority(i-1,afterTraj);
            }
            //优先队列满则删除优先级最低的点
            if(afterTraj.size() == capacity){
                reduce(afterTraj);
            }
        }
//        System.out.println("优先队列的容量是："+capacity);
        double priority = min_priority(afterTraj);
//        System.out.println("优先级是："+priority);
        //删除优先级小于阈值的轨迹点
        while(priority < maxdis){
            reduce(afterTraj);
            priority = min_priority(afterTraj);
//            System.out.println("优先级是："+priority);
        }
        return afterTraj;
    }

    public static void reduce(ArrayList<Point> aftertraj){
        /*
         *删除最小优先级的节点
         *@param aftertraj 优先队列
         *@return void
         **/
        double priority = Double.MAX_VALUE;
        int index = 0;
        for(int i=1;i<aftertraj.size()-1;i++){
             if(priority > aftertraj.get(i).getPriority()){
                    priority = aftertraj.get(i).getPriority();
                    index = i;
             }
        }
        aftertraj.get(index-1).setPriority(Math.max(priority,
                               aftertraj.get(index-1).getPriority()));
        aftertraj.get(index+1).setPriority(Math.max(priority,
                               aftertraj.get(index+1).getPriority()));
        aftertraj.remove(index);//删除最小优先级的节点
    }

    public static void adjust_priority(int index,ArrayList<Point> afterTraj){
        /*
         *@param index 有调整优先级的节点
         *@param afterTraj 优先级队列
         *@return void
         **/
        Distance distance = new Distance();
        double priority = 0;
        Point A = afterTraj.get(index-1);
        Point B = afterTraj.get(index+1);
        Point C = afterTraj.get(index);
        priority = afterTraj.get(index).getPriority()+
                distance.getSedDist(A,B,C);
        afterTraj.get(index).setPriority(priority);
    }

    public static double min_priority(ArrayList<Point> afterTraj){
        /*
         *查找优先级队列中最低优先级
         *@param afterTraj 优先级队列
         *@return 最低优先级
         **/
        double minpriority = Double.MAX_VALUE;
        double temp = 0;
        for(int i=1;i<afterTraj.size()-1;i++) {
            temp = afterTraj.get(i).getPriority();
            if (minpriority > temp) {
                minpriority = temp;
            }
        }
        return minpriority;
    }

    public static void main(String []args) throws Exception {
        /*
         *主函数
         *@param args 参数
         *@return void
         **/
        List<Point> beforeTraj = new ArrayList<>();
        List<Point> afterTraj = new ArrayList<>();
        GetDataFromFile getData = new GetDataFromFile();
        Estimate estimate = new Estimate();
        GetTime getTime = new GetTime();
        maxdis = 0.0000698;
        beforeTraj = getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        afterTraj = SQUISH_EAlgorithm(beforeTraj,0.4,maxdis);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("SQUISH-E算法");
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}
