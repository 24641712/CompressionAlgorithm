import entity.Point;
import estimate.Estimate;
import utils.GetDataFromFile;

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
    static float LimitDis;
    private static ArrayList<Point> targetList = new ArrayList<Point>();
    private static int delTotal=0;

    //	计算两点之间的距离
    protected static float p2pdis(Point pa,Point pb) {
        float d;
        d=(float)Math.sqrt(
                (pa.getLatitude()-pb.getLatitude())*
                (pa.getLatitude()-pb.getLatitude())+
                (pa.getLongitude()-pb.getLongitude())*
                (pa.getLongitude()-pb.getLongitude()));
        return d;
    }

    //删除轨迹点
    protected static void Delpt(ArrayList<Point> list,int a,int b) {
        int c=a+1;
        while(c<b){
            list.get(c).setRes('F');
            c++;
        }
    }

    /*
     *道格拉斯-普克算法（DP）算法
     *@param list 源轨迹集合
     *@param p1 起始点
     *@param p2 终止点
     *@return
     **/
    static void DPAlgorithm(ArrayList<Point> list,int p1,int p2){
        float a,b,c,cosA,cosB,sinA,maxdis,curdis;
        int i = 0,maxNO = 0;
        System.out.println("p1="+p1+"  p2="+p2);
        if(p2-p1 >= 2){
            maxdis = 0;
            c=p2pdis(list.get(p1),list.get(p2));
            i=p1+1;
            while(i < p2){
                curdis = 0;
                b = p2pdis(list.get(p1),list.get(i));
                a = p2pdis(list.get(p2),list.get(i));
                cosA = (b*b+c*c-a*a)/(2*b*c);
                cosB = (a*a+c*c-b*b)/(2*a*c);
                sinA = (float)Math.sqrt(1-cosA*cosA);
                if((cosA == 0) || (cosB == 0))   {
                    if(cosA == 0) {curdis = b;}
                    else {curdis = a;}
                }
                else {curdis = b*sinA;    }
                if(maxdis <= curdis)   {
                    maxdis = curdis;
                    maxNO = i;
                }
                i++;
            }//end while
            System.out.println("maxdis="+maxdis+" curList="+maxNO);
            if(maxdis >= LimitDis) {
                targetList.add(list.get(maxNO));
                System.out.println("targetList"+maxNO);
                DPAlgorithm(list,p1,maxNO);
                DPAlgorithm(list,maxNO,p2);
            }
            else {
                Delpt(list,p1,p2);
                delTotal++;
                System.out.println("Delpt: p1="+p1+" p2="+p2+"  删除"+(p2-p1)+"个轨迹点");
            }
        }
    }

    //主函数
    public static void main(String[] args) throws Exception {

        ArrayList<Point> beforeTraj = new ArrayList<Point>();
        ArrayList<Point> points = new ArrayList<>();
        Estimate estimate = new Estimate();
        //获取数据
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        GetDataFromFile getData = new GetDataFromFile();
        LimitDis = (float) 0.0000298;
        beforeTraj =getData.getDataFromFile(file,"1");

        DPAlgorithm(beforeTraj,0,beforeTraj.size()-1);
        System.out.println(beforeTraj.size());
        System.out.println(targetList.size()+"   删除"+delTotal+"个轨迹点");
//        getData.writeDataTomysql(points); //持久化
        estimate.CompressionRatio(beforeTraj.size(),targetList.size());
        estimate.CompressionError(beforeTraj,targetList);
    }

}
