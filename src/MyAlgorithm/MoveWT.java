package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;

import java.io.File;
import java.util.ArrayList;

/**
 * 用一个固定大小的窗口通过移动确定特征点
 * 来压缩轨迹。
 * @Author ccl
 * @Date 2019/2/24
 */
public class MoveWT {
    static ArrayList<Point> afterTraj = new ArrayList<Point>();
    static float LimitDis;

    /*
     *
     *@param beforeTraj 原始轨迹点
     *@param start 轨迹起始点
     *@param end 轨迹终止点
     *@param size 移动窗口大小
     *@return void
     **/
    public static void MoveWTAlgorithm(ArrayList<Point> beforeTraj,
                              int start,int end,int size){
        double maxdis = 0;
        int index = 0;
        Distance distance = new Distance();
        for(int i=start;i<end-size;i++){
          for(int j=i+1;j<i+1+size;j++){
              double curdis = distance.CalculatedDis(beforeTraj.get(i),
                      beforeTraj.get(i+1+size),beforeTraj.get(j));
              if(maxdis < curdis){
                  maxdis = curdis;
                  index = j;
              }
          }
        }
        System.out.println("maxdis = "+maxdis);
        if(maxdis > LimitDis){
            afterTraj.add(beforeTraj.get(index));
            MoveWTAlgorithm(beforeTraj,start,index-1,size);
            MoveWTAlgorithm(beforeTraj,index+1,end,size);
        }
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        LimitDis = (float) 0.0000001;
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        beforeTraj =getData.getDataFromFile(file,"1");
        MoveWTAlgorithm(beforeTraj,0,beforeTraj.size()-1,6);
        System.out.println("压缩后轨迹点数："+afterTraj.size());
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);

    }
}
