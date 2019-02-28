import entity.Point;
import estimate.Estimate;
import utils.Distance;
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
    static double LimitDis;
    private static ArrayList<Point> targetList = new ArrayList<Point>();
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
     *道格拉斯-普克算法（DP）算法
     *@param list 源轨迹集合
     *@param p1 起始点
     *@param p2 终止点
     *@return void
     **/
    static void DPAlgorithm(ArrayList<Point> beforeTraj,int p1,int p2){
        double a,b,c,cosA,cosB,sinA,maxdis,curdis;
        int i = 0,maxNO = 0;
        Distance distance = new Distance();
        System.out.println("p1="+p1+"  p2="+p2);
        Point pa = beforeTraj.get(p1);
        Point pb = beforeTraj.get(p2);
        if(p2-p1 >= 2){
            maxdis = 0;
            i=p1+1;
            while(i < p2){
                Point pc = beforeTraj.get(i);
                curdis = distance.CalculatedDis(pa,pb,pc);
                if(maxdis < curdis)   {
                    maxdis = curdis;
                    maxNO = i;
                }
                i++;
            }//end while
            System.out.println("maxdis="+maxdis+" curList="+maxNO);
            if(maxdis >= LimitDis) {
                targetList.add(beforeTraj.get(maxNO));
                System.out.println("targetList"+maxNO);
                DPAlgorithm(beforeTraj,p1,maxNO);
                DPAlgorithm(beforeTraj,maxNO,p2);
            }
            else {
                Delpt(beforeTraj,p1,p2);
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
        GetDataFromFile getData = new GetDataFromFile();
        //获取数据
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
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
