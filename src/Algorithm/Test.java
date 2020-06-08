package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.FilterNodes;
import utils.GetDataFromFile;
import utils.GetTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @version 1.0
 * @Description :
 * Copyright: Copyright (c)2019
 * Created Date : 2020/5/25
 */
public class Test {


    private static List<Point> afterTraj = new ArrayList<Point>();
    private final static double LimitDis = (float)9.6795598;

    //删除轨迹点
    protected static void Delpt(List<Point> list,int a,int b) {
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
    static void DPAlgorithm(List<Point> beforeTraj,int start,int end){
        double maxdis=0,curdis;
        int i = 0,maxNO = 0;
        Distance distance = new Distance();
        Point pa = beforeTraj.get(start);
        Point pb = beforeTraj.get(end);
        if(end-start >= 2){
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
            System.out.println(maxdis);
            if(maxdis >= LimitDis) {
                afterTraj.add(beforeTraj.get(maxNO));
                DPAlgorithm(beforeTraj,start,maxNO);
                DPAlgorithm(beforeTraj,maxNO,end);
            }
            else {
                Delpt(beforeTraj,start,end);
            }
        }
    }

    //主函数
    public static void main(String []args) throws Exception {
        List<Point> beforeTraj = new ArrayList<Point>();
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        GetTime getTime = new GetTime();

        beforeTraj =getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        DPAlgorithm(beforeTraj,0,beforeTraj.size()-1);
        getTime.setEndTime(System.currentTimeMillis());
//        afterTraj = FilterNodes.filter(afterTraj);
        Collections.sort(afterTraj);
        System.out.println("DP算法");
        System.out.println("压缩前的轨迹点数："+beforeTraj.size());
        System.out.println("压缩后的轨迹点数："+afterTraj.size());
        System.out.println("*********************");
        getTime.showTime();
        afterTraj = FilterNodes.filter(afterTraj);
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }

}
