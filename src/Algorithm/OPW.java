package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 基于欧式距离的开放窗口算法
 * @Author ccl
 */
public class OPW {
    /*
     *开放窗口算法实现
     *@param sourceList 源轨迹集合
     *@param maxdis 误差阈值
     *@return 压缩后的轨迹点
     **/
    public static ArrayList<Point> openWindowAlgorithm (
                    ArrayList<Point> beforeTraj, double maxdis) {

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
            for(Point point : listPoint) {
                double disOfTwo = distance.getDistance(beforeTraj.get(startPoint),
                        beforeTraj.get(floatPoint),point);
                System.out.println("disOfTwo="+disOfTwo);
                if(disOfTwo >= 1.190){
                    flag = true;
                    break;
                }
            }
            if (flag) {
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

    public static void main(String[] args) throws Exception{
        double maxDistanceError = 30;
        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        ArrayList<Point> afterTraj = new ArrayList<Point>();
        ArrayList<Point> points=new ArrayList<Point>();
        GetDataFromFile getData = new GetDataFromFile();
        Estimate estimate = new Estimate();
        String path = "F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt";
        File file = new File(path);
        beforeTraj = getData.getDataFromFile(file,"1");
        afterTraj = openWindowAlgorithm(beforeTraj,maxDistanceError);
        System.out.println("压缩前轨迹点数："+beforeTraj.size());
        System.out.println("压缩后的轨迹数："+afterTraj.size());
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }
}