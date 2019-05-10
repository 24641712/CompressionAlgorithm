package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;
import java.util.ArrayList;

/**
 * 基本思想：第一个轨迹点是特征点，从第三个点
 * 开始若期间有轨迹点欧式距离一直增大直到减小
 * 时的最大欧式距离的点是轨迹点。
 * @Author ccl
 * @Date 2019/4/20
 */
public class IncrePW {
    static double LimitDist1;
    static double LimitDist2;
    static int index = 0;

    /*
     *求特征点tzd与第k个点间的最大欧式距离
     *@param beforeTraj 源轨迹点
     *@param tzd 特征点
     *@param k 轨迹点
     *@return 最大欧式距离
     **/
    public static double getMaxDist(ArrayList<Point> beforeTraj,int tzd,int k){
        double maxdist = 0;
        double curdist = 0;
        Point pa = beforeTraj.get(tzd);
        Point pb = beforeTraj.get(k);
        Distance distance = new Distance();
        for(int i=tzd+1;i<k;i++){
            Point pc = beforeTraj.get(i);
            curdist = distance.getDistance(pa,pb,pc);
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
    public static ArrayList<Point> IncrementPWAlgorithm(ArrayList<Point> beforeTraj){
       ArrayList<Point> afterTraj = new ArrayList<Point>();
       int length = beforeTraj.size();
       int tzd = 0;//特征点
       double maxdist = 0,curdist;
       for(int i=2;i<length;i++){
           curdist = getMaxDist(beforeTraj,tzd,i);
           System.out.println("maxdist:"+maxdist+", curdist1:"+curdist);
           //不是特征点
           if(maxdist <= curdist || maxdist <= LimitDist1){
               maxdist = curdist;
               System.out.println("maxdist:"+maxdist+", curdist2:"+curdist);
           }else{
               if(maxdist>LimitDist2){
                   System.out.println("起点："+tzd+", 终点："+index);
                   System.out.println("删除了"+(index-tzd)+"个轨迹点");
                   tzd = index;
                   i = index+1;
                   afterTraj.add(beforeTraj.get(index));
                   System.out.println("add maxdist:"+maxdist);
               }
               maxdist = 0;
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
        LimitDist1 = (float) 0.00000;
        LimitDist2 = (float) 0.00000;
        beforeTraj =getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        afterTraj = IncrementPWAlgorithm(beforeTraj);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("IncrePW算法");
        System.out.println("压缩前的轨迹点数："+beforeTraj.size());
        System.out.println("压缩后的轨迹点数："+afterTraj.size());
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }

}
