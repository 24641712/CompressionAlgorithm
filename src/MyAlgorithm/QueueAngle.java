package MyAlgorithm;

import entity.Point;
import estimate.Estimate;
import utils.GetDataFromFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 基于队列的轨迹压缩算法
 * @author ccl
 */
public class QueueAngle {
    private static float limitAngle;
    private static ArrayList<Point> targetList = new ArrayList<Point>();
    private static int delTotal=0;
    static float p2pdis(Point pa,Point pb) {
        float d;
        d = (float)Math.sqrt(
                (pa.getLatitude()-pb.getLatitude())*
                (pa.getLatitude()-pb.getLatitude())+
                (pa.getLongitude()-pb.getLongitude())*
                (pa.getLongitude()-pb.getLongitude()));
        return d;
    }
    static float getAngle(Point startP,Point floatP,Point nowP ) {
        float a,b,c,cosC;
        a = p2pdis(startP, nowP) ;
        b = p2pdis(floatP, nowP);
        c = p2pdis(startP, floatP);
        if(a == 0 || b ==0) {
            return -2;
        }
        cosC = (a*a + b*b - c*c)/(2*a*b);
        return cosC;
    }
    public static ArrayList<Point> runAngle(ArrayList< Point> sourceList,int p1,int p2) {
        ArrayList<Point> targetList = new ArrayList<Point>();
        int startPoint = 0;
        int floatPoint = 2;
        int nowPoint = 1;
        int len = sourceList.size();
        ArrayList<Point> listPoint = new ArrayList<Point>();
        listPoint.add(sourceList.get(nowPoint));
        while(true) {
            boolean flag1 = false;
            for(Point point : listPoint) {
                double disOfTwo = getAngle(sourceList.get(startPoint),sourceList.get(floatPoint),point);
                if(disOfTwo != -2) {
//    			   System.out.println("disOfTwo="+disOfTwo);
                }
                if(disOfTwo >= -0.988){
                    flag1 = true;
                    break;
                }
            }
            if (flag1) {
                targetList.add(sourceList.get(startPoint));
                System.out.println("�����ࣺ"+(floatPoint-startPoint));
                startPoint = floatPoint-1;
                floatPoint +=1;
                if(floatPoint >= len) {
                    targetList.add(sourceList.get(floatPoint-1));
                    break;
                }
                listPoint.clear();
                listPoint.add(sourceList.get(startPoint));
            }else {
                listPoint.add(sourceList.get(floatPoint));
                floatPoint += 1;

                if(floatPoint >= len) {
                    targetList.add(sourceList.get(startPoint));
                    targetList.add(sourceList.get(floatPoint-1));
                    break;
                }
            }
            flag1 = false;
        }
        return targetList;
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Point> targetList = new ArrayList<Point>();
        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        Estimate estimate = new Estimate();
        GetDataFromFile getData = new GetDataFromFile();
        limitAngle = (float) -0.9836571;
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        beforeTraj = getData.getDataFromFile(10,"1");
        System.out.println(beforeTraj.size());
        targetList = runAngle(beforeTraj, 0, beforeTraj.size()-1);
//		getData.writeDataTomysql(points);
        System.out.println(targetList.size());
        estimate.CompressionRatio(beforeTraj.size(),targetList.size());

    }
}
