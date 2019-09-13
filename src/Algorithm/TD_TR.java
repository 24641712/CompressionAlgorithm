package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TD_TR算法，类似于DP算法，
 * 不同点是使用欧式距离代替垂直距离。
 * @Author ccl
 * @Date 2019/3/5
 */
public class TD_TR {
    private static ArrayList<Point> afterTraj = new ArrayList<Point>();//压缩后的轨迹点
    static double LimitDis;//阈值
    private static int delTotal=0;//统计删除数据

    public static void TD_TRAlgorithm(List<Point> beforeTraj,int start,int end){
        /*
         *@param beforeTraj 压缩前轨迹点
         *@param start 起始下标
         *@param end 终止下标
         *@return void
         **/
        double a,b,c,cosA,cosB,sinA,maxdis,curdis;
        int i = 0,maxNO = 0;
        Distance distance = new Distance();
//        System.out.println("start="+start+"  end="+end);
        Point pa = beforeTraj.get(start);
        Point pb = beforeTraj.get(end);
        if(end-start >= 2){
            maxdis = 0;
            i=start+1;
            while(i < end){
                Point pc = beforeTraj.get(i);
                curdis = distance.getSedDist(pa,pb,pc);
                if(maxdis < curdis)   {
                    maxdis = curdis;
                    maxNO = i;
                }
                i++;
            }//end while
//            System.out.println("maxdis="+maxdis+" curList="+maxNO);
            if(maxdis > LimitDis) {
                afterTraj.add(beforeTraj.get(maxNO));
//                System.out.println("targetList:"+maxNO);
                TD_TRAlgorithm(beforeTraj,start,maxNO);
                TD_TRAlgorithm(beforeTraj,maxNO,end);
            }
            else {
                Delpt(beforeTraj,start,end);
                delTotal++;
//                System.out.println("Delpt: p1="+start+" p2="+end+"  删除"+(end-start)+"个轨迹点");
            }
        }
    }

    protected static void Delpt(List<Point> list,int a,int b) {
        /*
         *删除轨迹点
         *@param list 原始轨迹
         *@param a 起始位置下标
         *@param b 终止位置下标
         *@return void
         **/
        int c=a+1;
        while(c<b){
            list.get(c).setRes('F');
            c++;
        }
    }

    public static void main(String []args) throws Exception {
        /*
         *主函数
         *@param args 参数
         *@return void
         **/
        List<Point> beforeTraj = new ArrayList<>();
        GetDataFromFile getData = new GetDataFromFile();
        Estimate estimate = new Estimate();
        GetTime getTime = new GetTime();
        LimitDis = 2.9000298;
        beforeTraj = getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        TD_TRAlgorithm(beforeTraj,0,beforeTraj.size()-1);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("TD-TR算法");
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}
