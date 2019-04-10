package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.io.File;
import java.util.ArrayList;

/**
 * 线上压缩算法：固定窗口的轨迹压缩算法，
 * 设定一个固定的容器，当容器满时使用道格拉斯-普克
 * 算法压缩轨迹。然后置空容器，继续进行轨迹压缩，
 * 依次类推。
 * @Author ccl
 * @Date 2019/3/3
 */
public class FixedWT {
    static ArrayList<Point> afterTraj = new ArrayList<>();
    static double dpLimitDis;

    /*
     *轨迹段长度小于移动窗口时，使用阈值
     *求轨迹点。
     *@param beforeTraj 源轨迹
     *@param start 起始点
     *@param end 终止点
     *@return void
     **/
    protected static void dpAlgorithm(ArrayList<Point> beforeTraj,int start,int end){
        Point pa = beforeTraj.get(start);
        Point pb = beforeTraj.get(end);
        Distance distance = new Distance();
        double maxdis = 0;
        int index = 0;
        if(start >= end-1)return ;
        for(int i=start+1;i<end;i++){
            Point pc = beforeTraj.get(i);
            double temp = distance.getDistance(pa,pb,pc);
            if(temp > maxdis){
                maxdis = temp;
                index = i;
            }
        }
        System.out.println("maxdis:"+maxdis);
        if(maxdis > dpLimitDis){
            afterTraj.add(beforeTraj.get(index));
            dpAlgorithm(beforeTraj,start,index);
            dpAlgorithm(beforeTraj,index,end);
        }
    }

    /*
     *固定容器的轨迹压缩算法
     *@param beforeTraj 源轨迹
     *@param size 容器大小
     *@return void
     **/
    public static void FixedWTAlgorithm(ArrayList<Point> beforeTraj,int size){
        int number = beforeTraj.size();
        int start=1;
        int end = start + size;
        int remember = start;
        while(end <= number-1){
            dpAlgorithm(beforeTraj,start,end);
            int index = afterTraj.size();
            start = afterTraj.get(index-1).getPid();
            end = start + size;
            if(remember == start){
                start = end;
                end = start + size;
            }
            remember = start;
        }
        dpAlgorithm(beforeTraj,start,beforeTraj.size()-1);

    }

    /*
     *主函数
     *@param args
     *@return void
     **/
    public static void main(String[] paths) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<>();
        GetDataFromFile getData = new GetDataFromFile();
        Estimate estimate = new Estimate();
        GetTime getTime = new GetTime();
        dpLimitDis = 0.124;
        beforeTraj = getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        FixedWTAlgorithm(beforeTraj,200);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("FixedWT算法");
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}
