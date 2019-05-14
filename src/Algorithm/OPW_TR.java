package Algorithm;

import entity.Point;
import estimate.Estimate;
import utils.Distance;
import utils.GetDataFromFile;
import utils.GetTime;

import java.io.File;
import java.util.ArrayList;

/**
 * 基于同步欧式距离(SED)的开放窗口算法
 * @Author ccl
 * @Date 2019/3/5
 */
public class OPW_TR {

    /*
     *基于同步欧式距离的开放窗口算法
     *@param sourceList 压缩前轨迹数据
     *@param maxdis 阈值
     *@return 压缩后轨迹
     **/
    public static ArrayList<Point> openWindow_TRAlgorithm (
                  ArrayList<Point> beforeTraj, double maxdis) {
        ArrayList<Point> afterTraj = new ArrayList<Point>();
        int startPoint = 0;
        int floatPoint = 2;
        int nowPoint = 1;
        int len = beforeTraj.size();
        double disOfTwo=0;
        Distance distance = new Distance();
        ArrayList<Point> listPoint = new ArrayList<Point>();
        listPoint.add(beforeTraj.get(nowPoint));
        while(true) {
            boolean flag = false;
            for(Point point : listPoint) {
                disOfTwo = distance.getSedDist(beforeTraj.get(startPoint),
                        beforeTraj.get(floatPoint),point);

                if(disOfTwo >= maxdis){
                    flag = true;
                    break;
                }
            }
//            System.out.println("disOfTwo="+disOfTwo);
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
                if(floatPoint >= len){
                    afterTraj.add(beforeTraj.get(startPoint));
                    afterTraj.add(beforeTraj.get(floatPoint-1));
                    break;
                }
            }
            flag = false;
        }
        return afterTraj;
    }

    /*
     *主函数
     *@param args 参数
     *@return void
     **/
    public static void main(String []args) throws Exception{
        double maxDist = 0.00005700298;
        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        ArrayList<Point> afterTraj = new ArrayList<Point>();
        Estimate estimate = new Estimate();
        GetTime getTime = new GetTime();
        GetDataFromFile getData = new GetDataFromFile();
        beforeTraj = getData.getDataFromFile(10000,"1");
        getTime.setStartTime(System.currentTimeMillis());
        afterTraj = openWindow_TRAlgorithm(beforeTraj,maxDist);
        getTime.setEndTime(System.currentTimeMillis());
        System.out.println("压缩前轨迹点数："+beforeTraj.size());
        System.out.println("压缩后轨迹点数："+afterTraj.size());
        System.out.println("OPW_Impro-TR算法");
        getTime.showTime();
        estimate.CompressionRatio(beforeTraj.size(),afterTraj.size());
        estimate.CompressionError(beforeTraj,afterTraj);
    }

}
