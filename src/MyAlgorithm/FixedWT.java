package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;

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
        System.out.println("dpmaxdis = " + maxdis + " index = " + index);
        if(maxdis > dpLimitDis){
            System.out.println("dpmaxdis = " + maxdis + " index = " + index);
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
        int number = (int)(beforeTraj.size()/size);
        for(int i=0;i<number;i++){
            int start = i*size;
            int end = (i+1)*size;
            dpAlgorithm(beforeTraj,start,end);
        }
        dpAlgorithm(beforeTraj,number*size,beforeTraj.size()-1);

    }

    /*
     *主函数
     *@param args
     *@return void
     **/
    public static void main(String[] args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<>();
        GetDataFromFile getData = new GetDataFromFile();
        Estimate estimate = new Estimate();
        dpLimitDis = 2.500025;
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        beforeTraj = getData.getDataFromFile(file,"1");
        FixedWTAlgorithm(beforeTraj,15);
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}
