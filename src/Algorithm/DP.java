package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *道格拉斯-普克算法的实现
 * @author ccl
 */
public class DP {
    static int iNum;
    static double LimitDis;
    private static ArrayList<Point> afterTraj = new ArrayList<Point>();
    private static int delTotal=0;

    //删除轨迹点
    protected static void Delpt(ArrayList<Point> list,int a,int b) {
        int c=a+1;
        while(c<b){
            list.get(c).setRes('F');
            c++;
        }
    }

    /*
     *道格拉斯-普克算法（Algorithm.DP）算法
     *@param list 源轨迹集合
     *@param p1 起始点
     *@param p2 终止点
     *@return void
     **/
    static void DPAlgorithm(ArrayList<Point> beforeTraj,int start,int end){
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
                if(maxdis < curdis)   {
                    maxdis = curdis;
                    maxNO = i;
                }
                i++;
            }//end while
           // System.out.println("maxdis="+maxdis+" curList="+maxNO);
            if(maxdis >= LimitDis) {
                afterTraj.add(beforeTraj.get(maxNO));
                DPAlgorithm(beforeTraj,start,maxNO);
                DPAlgorithm(beforeTraj,maxNO,end);
            }
            else {
                Delpt(beforeTraj,start,end);
                delTotal++;
            }
        }
    }

    //主函数
    public static void main(String []args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        GetTime getTime = new GetTime();
        LimitDis = (float) 1.6000298;
        beforeTraj =getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        DPAlgorithm(beforeTraj,0,beforeTraj.size()-1);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("DP算法");
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }

}
