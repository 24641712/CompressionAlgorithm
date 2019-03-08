package Algorithm;

import entity.Point;
import utils.Distance;
import utils.GetDataFromFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * SQUISH_E算法
 * @Author ccl
 * @Date 2019/3/5
 */
public class SQUISH_E {

    public static  void SQUISH_EAlgorithm(ArrayList<Point> beforeTraj
                           ,double ratio,double maxdis){
        ArrayList<Point> afterTraj = new ArrayList<>();
        int []value = new int[1024];
        int capacity = 4;
        for(int i=0;i<beforeTraj.size();i++){
            if(i > capacity*ratio)capacity++;//增加优先队列的容量
            beforeTraj.get(i).setPriority(0);
            afterTraj.add(beforeTraj.get(i));
            if(i>1){
                adjust_priority(i-1,afterTraj);//调整优先级队列中的优先级
            }
            if(afterTraj.size() == capacity){
                reduce(afterTraj);
            }
        }
        double priority = min_priority(afterTraj);
        while(priority < maxdis){
            reduce(afterTraj);
            priority = min_priority(afterTraj);
        }
    }

    /*
     *删除最小优先级的节点
     *@param aftertraj 优先队列
     *@return void
     **/
    public static void reduce(ArrayList<Point> aftertraj){
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

    /*
     *
     *@param index 有调整优先级的节点
     *@param afterTraj 优先级队列
     *@return void
     **/
    public static void adjust_priority(int index,ArrayList<Point> afterTraj){
        Distance distance = new Distance();
        double priority = 0;
        Point A = afterTraj.get(index-1);
        Point B = afterTraj.get(index+1);
        Point C = afterTraj.get(index);
        priority = afterTraj.get(index).getPriority()+
                distance.getSedDist(A,B,C);
        afterTraj.get(index).setPriority(priority);
    }

    /*
     *查找优先级队列中最低优先级
     *@param afterTraj 优先级队列
     *@return 最低优先级
     **/
    public static double min_priority(ArrayList<Point> afterTraj){
        double priority = Double.MAX_VALUE;
        double temp = 0;
        for(int i=0;i<afterTraj.size();i++) {
            temp = afterTraj.get(i).getPriority();
            if (priority > temp) {
                priority = temp;
            }
        }
        return priority;
    }

    /*
     *主函数
     *@param args 参数
     *@return void
     **/
    public static void main(String[] args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<>();
        GetDataFromFile getData = new GetDataFromFile();
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        beforeTraj = getData.getDataFromFile(file,"1");




    }
}
