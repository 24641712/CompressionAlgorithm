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
    private  final static double LimitDis = 0.35500298;//阈值
    private static int delTotal=0;//统计删除数据

    /*
     *@param beforeTraj 压缩前轨迹点
     *@param start 起始下标
     *@param end 终止下标
     *@return void
     **/
    public static void TD_TRAlgorithm(List<Point> beforeTraj,int start,int end){
        double maxdis,curdis;
        int i = 0,maxNO = 0;
        Distance distance = new Distance();
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
            if(maxdis > LimitDis) {
                afterTraj.add(beforeTraj.get(maxNO));
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
        beforeTraj = getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        TD_TRAlgorithm(beforeTraj,0,beforeTraj.size()-1);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("TD-TR算法");
        System.out.println("压缩前轨迹点数："+beforeTraj.size());
        System.out.println("压缩后轨迹点数："+afterTraj.size());
        System.out.println("*********************");
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}
