package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用一个固定大小的窗口通过移动确定特征点
 * 来压缩轨迹,当轨迹段小于窗口大小时使用dp
 * 算法压缩。
 * @Author ccl
 * @Date 2019/2/24
 */
public class MoveWT {
    static ArrayList<Point> afterTraj = new ArrayList<Point>();
    static double dpLimitDis;
    static double mwtLimitDis;
    static Distance distance = new Distance();

    /*
     *轨迹段长度小于移动窗口时，使用阈值
     *求轨迹点。
     *@param beforeTraj 源轨迹
     *@param start 起始点
     *@param end 终止点
     *@return void
     **/
    protected static void dpAlgorithm(List<Point> beforeTraj,int start,int end){
        Point pa = beforeTraj.get(start);
        Point pb = beforeTraj.get(end);
        double maxdis = 0;
        int index = 0;
        if(start >= end-1)return ;
         for(int i=start+1;i<end;i++){
             Point pc = beforeTraj.get(i);
             double temp = distance.CalculatedDis(pa,pb,pc);
             if(temp > maxdis){
                 maxdis = temp;
                 index = i;
             }
         }
        System.out.println(" dpmaxdis = " + maxdis + " index = " + index);
         if(maxdis > dpLimitDis){
             System.out.println(" dpmaxdis = " + maxdis + " index = " + index);
             afterTraj.add(beforeTraj.get(index));
             dpAlgorithm(beforeTraj,start,index);
             dpAlgorithm(beforeTraj,index,end);
         }
    }

    /*
     *@param beforeTraj 原始轨迹点
     *@param start 轨迹起始点
     *@param end 轨迹终止点
     *@param size 移动窗口大小
     *@return void
     **/
    public static void MoveWTAlgorithm(List<Point> beforeTraj,
                              int start,int end,int size) {
        double maxdis = 0;
        int index = 0;

//        System.out.println("当前轨迹段：start = " + start + " end = " + end);
        if (start >= end) return;
        if (end - start < size) {
            dpAlgorithm(beforeTraj, start, end);
        }else{
        for (int i = start; i < end - size; i++) {
            for (int j = i + 1; j < i + 1 + size; j++) {
                double curdis = distance.CalculatedDis(beforeTraj.get(i),
                        beforeTraj.get(i + 1 + size), beforeTraj.get(j));
                if (maxdis < curdis) {
                    maxdis = curdis;
                    index = j;
                }
            }
        }
        System.out.println(" mwtmaxdis = " + maxdis + " index = " + index);
        if (maxdis > mwtLimitDis) {
//            System.out.println(" maxdis = " + maxdis + " index = " + index);
            afterTraj.add(beforeTraj.get(index));
            MoveWTAlgorithm(beforeTraj, start, index, size);
            MoveWTAlgorithm(beforeTraj, index, end, size);
        }else if(maxdis>0){
//            System.out.println(" maxdis = " + maxdis + " index = " + index);
        }
    }
    }

    /*
     *主函数
     *@param args参数
     *@return void
     **/
    public static void main(String[] args) throws Exception {
        List<Point> beforeTraj = new ArrayList<Point>();
        mwtLimitDis =  0.000020005;
        dpLimitDis = 0.000030;
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        beforeTraj = getData.getDataFromFile(10,"1");
        MoveWTAlgorithm(beforeTraj,0,beforeTraj.size()-1,4);
        System.out.println("压缩后轨迹点数："+afterTraj.size());
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);

    }
}
