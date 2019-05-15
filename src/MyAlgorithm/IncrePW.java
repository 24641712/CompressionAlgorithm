package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;
import java.util.ArrayList;

public class IncrePW {
    /**
     * 基本思想：第一个轨迹点是特征点，从第三个点
     * 开始若期间有轨迹点欧式距离一直增大直到减小
     * 时的最大欧式距离的点是轨迹点。
     * @Author ccl
     * @Date 2019/4/20
     */
    static double DPLimitDist1;
    static double LimitDist2;
    static int index = 0;
    static ArrayList<Point> afterTraj = new ArrayList<Point>();
    static ArrayList<Point> afterTraj_copy = new ArrayList<Point>();

    static void DPAlgorithm(ArrayList<Point> beforeTraj,int start,int end){
       /*
        *道格拉斯-普克算法（Algorithm.DP）算法
        *@param beforeTraj 源轨迹集合
        *@param start 起始点
        *@param end 终止点
        *@return void
        **/
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
                if(maxdis < curdis) {
                    maxdis = curdis;
                    maxNO = i;
                }
                i++;
            }//end while
             System.out.println("maxdis="+maxdis+" index="+maxNO);
            if(maxdis >= DPLimitDist1) {
                afterTraj.add(beforeTraj.get(maxNO));
                DPAlgorithm(beforeTraj,start,maxNO);
                DPAlgorithm(beforeTraj,maxNO,end);
            }
        }
    }

    public static double getMaxDist(ArrayList<Point> beforeTraj,int tzd,int k){
       /*
        *求特征点tzd与第k个点间的最大欧式距离
        *@param beforeTraj 源轨迹点
        *@param tzd 特征点
        *@param k 轨迹点
        *@return 最大欧式距离
        **/
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

    public static void IncrementPWAlgorithm(ArrayList<Point> beforeTraj){
      /*
       *最大欧式距离增加到最大时的轨迹点就是特征点
       *@param beforeTraj 源轨迹点
       *@return 压缩后的轨迹点
       **/
       int length = beforeTraj.size();
       int tzd = 0;//特征点
       double maxdist = 0,curdist;
       for(int i=2;i<length;i++){
           curdist = getMaxDist(beforeTraj,tzd,i);
//           System.out.println("maxdist:"+maxdist+", curdist1:"+curdist);
           //不是特征点
           if(maxdist <= curdist){
               maxdist = curdist;
//               System.out.println("maxdist:"+maxdist+", curdist2:"+curdist);
           }else{
               if(maxdist>LimitDist2){
//                   System.out.println("起点："+tzd+", 终点："+index);
//                   System.out.println("删除了"+(index-tzd)+"个轨迹点");
                   tzd = index;
                   i = index+1;
                   //获得轨迹点
                   afterTraj.add(beforeTraj.get(index));
                   afterTraj_copy.add(beforeTraj.get(index));
//                   System.out.println("add maxdist:"+maxdist);
               }
               maxdist = 0;
           }
       }
    }

    public static void secnd_Compress_Tragectory(ArrayList<Point> beforeTraj){
        /*
         *对压缩后的轨迹点进行二次压缩
         *@param beforeTraj
         *@return
         **/
        for(int i=0;i<afterTraj_copy.size()-1;i++){
            int left = afterTraj_copy.get(i).getPid();
            int right = afterTraj_copy.get(i+1).getPid();
            DPAlgorithm(beforeTraj,left,right);
        }
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        GetTime getTime = new GetTime();
        DPLimitDist1 = (float) 1.400000;
        LimitDist2 = (float) 25.00000;
        beforeTraj =getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        //找最大轨迹点
        IncrementPWAlgorithm(beforeTraj);
        //进行二次压缩
        secnd_Compress_Tragectory(beforeTraj);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("IncrePW算法");
        System.out.println("压缩前的轨迹点数："+beforeTraj.size());
        System.out.println("压缩后的轨迹点数："+afterTraj.size());
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }

}
