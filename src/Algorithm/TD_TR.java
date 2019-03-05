package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;

import java.io.File;
import java.util.ArrayList;

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

    /*
     *
     *@param beforeTraj 压缩前轨迹点
     *@param start 起始下标
     *@param end 终止下标
     *@return void
     **/
    public static void TD_TRAlgorithm(ArrayList<Point> beforeTraj,int start,int end){
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
            System.out.println("maxdis="+maxdis+" curList="+maxNO);
            if(maxdis > LimitDis) {
                afterTraj.add(beforeTraj.get(maxNO));
//                System.out.println("targetList:"+maxNO);
                TD_TRAlgorithm(beforeTraj,start,maxNO);
                TD_TRAlgorithm(beforeTraj,maxNO,end);
            }
            else {
                Delpt(beforeTraj,start,end);
                delTotal++;
                System.out.println("Delpt: p1="+start+" p2="+end+"  删除"+(end-start)+"个轨迹点");
            }
        }
    }

    /*
     *删除轨迹点
     *@param list 原始轨迹
     *@param a 起始位置下标
     *@param b 终止位置下标
     *@return void
     **/
    protected static void Delpt(ArrayList<Point> list,int a,int b) {
        int c=a+1;
        while(c<b){
            list.get(c).setRes('F');
            c++;
        }
    }

    /*
     *主函数
     *@param args 参数
     *@return void
     **/
    public static void main(String[] args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<>();
        GetDataFromFile getData = new GetDataFromFile();
        Estimate estimate = new Estimate();
        LimitDis = 0.0047;
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        beforeTraj = getData.getDataFromFile(file,"1");
        TD_TRAlgorithm(beforeTraj,0,beforeTraj.size()-1);
        System.out.println("压缩前轨迹点数："+beforeTraj.size());
        System.out.println("压缩后轨迹点数："+afterTraj.size());
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}