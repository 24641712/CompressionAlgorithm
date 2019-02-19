import entity.Point;
import utils.GetDataFromFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 开放窗口算法
 * @author ccl
 */
public class OpenWT {

    public static double Rad(double d){
        return d * Math.PI / 180.0;
    }

    public static double geoDist(Point pA,Point pB){

        double radLat1 = Rad(pA.getLatitude());
        double radLat2 = Rad(pB.getLatitude());
        double delta_lon = Rad(pB.getLongitude() - pA.getLongitude());
        double top_1 = Math.cos(radLat2) * Math.sin(delta_lon);
        double top_2 = Math.cos(radLat1) * Math.sin(radLat2) - Math.sin(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
        double top = Math.sqrt(top_1 * top_1 + top_2 * top_2);
        double bottom = Math.sin(radLat1) * Math.sin(radLat2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
        double delta_sigma = Math.atan2(top, bottom);
        double distance = delta_sigma * 6378137.0;
        return distance;
    }

    public static double getDistance(Point A,Point B,Point C){

        double distance = 0;
        double a = Math.abs(geoDist(A,B));//计算边长
        double b = Math.abs(geoDist(B,C));
        double c = Math.abs(geoDist(A,C));
        double p = (a + b + c)/2.0;
        double s = Math.sqrt(p * (p-a) * (p-b) * (p-c)); //计算面积
        distance = s * 2.0 / a; //Ͷ求出距离
        return distance;
    }

    public static ArrayList<Point> openWindowTra (
            ArrayList<Point> sourceList, double maxdis) {
        ArrayList<Point> targetList = new ArrayList<Point>();
        int startPoint = 0;
        int floatPoint = 2;
        int nowPoint = 1;
        int len = sourceList.size();
        ArrayList<Point> listPoint = new ArrayList<Point>();
        listPoint.add(sourceList.get(nowPoint));
        while(true) {
            boolean flag = false;
            for(Point point : listPoint) {
                double disOfTwo = getDistance(sourceList.get(startPoint),sourceList.get(floatPoint),point);
                System.out.println("disOfTwo="+disOfTwo);
                if(disOfTwo >= 1.190){
                    flag = true;
                    break;
                }
            }
            if (flag) {
                targetList.add(sourceList.get(startPoint));
                startPoint = floatPoint-1;
                floatPoint += 1;
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
            flag = false;
        }
        return targetList;
    }

    public static void writeTestPointToFile(File outGPSFile, ArrayList<Point> pGPSPointFilter)throws Exception{
        Iterator<Point> iFilter = pGPSPointFilter.iterator();
        RandomAccessFile rFilter = new RandomAccessFile(outGPSFile,"rw");
        while(iFilter.hasNext()){
            Point p = iFilter.next();
            String sFilter = p.toString();
            byte[] bFilter = sFilter.getBytes();
            rFilter.write(bFilter);
        }
        rFilter.close();
    }
    public static void main(String[] args) throws Exception{
        double maxDistanceError = 30;
        ArrayList<Point> ENPList = new ArrayList<Point>();
        ArrayList<Point> rePointList = new ArrayList<Point>();
        ArrayList<Point> points=new ArrayList<Point>();
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        GetDataFromFile getData = new GetDataFromFile();
        ENPList = getData.getDataFromFile(file,"1");

        System.out.println(ENPList.size());

        rePointList = openWindowTra(ENPList,maxDistanceError);

//        writeTestPointToFile(targetFile,rePointList);
//        dbData dbdata=new dbData();

//        points=dbData.transform(rePointList, "3");
        getData.writeDataTomysql(points);

        System.out.println(ENPList.size());
        System.out.println(points.size());

        double cpL = ((double)rePointList.size() / (double)ENPList.size())* 100;
        DecimalFormat df = new DecimalFormat("0.000000");
        System.out.println("压缩率为："+ df.format(cpL) + "%");

//        double aveDisErr = getMeanDistError(ENPList,rePointList);
//        System.out.println(aveDisErr);

        //generateImage(ENPList,rePointList);


    }




}
