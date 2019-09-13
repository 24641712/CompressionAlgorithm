package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 基于垂直距离的开放窗口（OPW_Impro）算法
 * @Author ccl
 */
public class OPW {

    public static ArrayList<Point> openWindowAlgorithm (
                    List<Point> beforeTraj, double LimitDis) {    /*
         *开放窗口算法实现
         *@param sourceList 源轨迹集合
         *@param maxdis 误差阈值
         *@return 压缩后的轨迹点
         **/

        ArrayList<Point> afterTraj = new ArrayList<Point>();
        int startPoint = 0;
        int floatPoint = 2;
        int nowPoint = 1;
        int len = beforeTraj.size();
        Distance distance = new Distance();
        ArrayList<Point> listPoint = new ArrayList<Point>();
        listPoint.add(beforeTraj.get(nowPoint));
        while(true) {
            boolean flag = false;
            for(Point point : listPoint){
                double disOfTwo = distance.getDistance(beforeTraj.get(startPoint),
                            beforeTraj.get(floatPoint),point);
//                System.out.println("disOfTwo="+disOfTwo);
                if(disOfTwo >= LimitDis) {
                    flag = true;
                    break;
                }
            }
            if(flag) {
                afterTraj.add(beforeTraj.get(startPoint));
                startPoint = floatPoint-1;
                floatPoint += 1;
                if(floatPoint >= len) {
                    afterTraj.add(beforeTraj.get(floatPoint-1));
                    break;
                }
                listPoint.clear();
                listPoint.add(beforeTraj.get(startPoint));
            }else {
                listPoint.add(beforeTraj.get(floatPoint));
                floatPoint += 1;
                if(floatPoint >= len) {
                    afterTraj.add(beforeTraj.get(startPoint));
                    afterTraj.add(beforeTraj.get(floatPoint-1));
                    break;
                }
            }
            flag = false;
        }
        return afterTraj;
    }

    public static void main(String []args) throws Exception{
        double maxDistError = 0.7;
        List<Point> beforeTraj = new ArrayList<Point>();
        List<Point> afterTraj = new ArrayList<Point>();
        GetDataFromFile getData = new GetDataFromFile();
        Estimate estimate = new Estimate();
        GetTime getTime = new GetTime();
        beforeTraj = getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        afterTraj = openWindowAlgorithm(beforeTraj,maxDistError);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("OPW算法");
        System.out.println("压缩前轨迹点数："+beforeTraj.size());
        System.out.println("压缩后轨迹点数："+afterTraj.size());
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}
