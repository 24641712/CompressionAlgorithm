package MyAlgorithm;

import entity.Point;
import utils.GetDataFromFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *基于角度的轨迹压缩算法
 * @author ccl
 */
public class angle {
    private static float limitAngle;
    private static ArrayList<Point> targetList = new ArrayList<Point>();
    private static int delTotal=0;

    static float p2pdis(Point pa, Point pb) {
        float d;
        d = (float)Math.sqrt(
                (pa.getLatitude()-pb.getLatitude())*
                (pa.getLatitude()-pb.getLatitude())+
                (pa.getLongitude()-pb.getLongitude())*
                (pa.getLongitude()-pb.getLongitude()));
        return d;
    }
    static void Delpt(ArrayList<Point> list, int a, int b) {
        int c=a+1;
        while(c<b)  {
            list.get(c).setRes('F');
            c++;
        }
    }
    public static void runAngle(ArrayList< Point> list,int p1,int p2,int flag) {
        float a,b,c,cosC,curAngle,minAngle;
        int i = 0,maxNO = 0;
        System.out.println("p1="+p1+", p2="+p2+" flag="+flag);
        if(p2-p1 >= 2) {
            c = p2pdis(list.get(p1), list.get(p2));
            curAngle =-1;
            maxNO=i;
            minAngle=-1;
            i=p1+1;
            while(i < p2) {
                a = p2pdis(list.get(p1), list.get(i));
                b = p2pdis(list.get(p2), list.get(i));
                if(a == 0 || b ==0) {
                    i++;
                    continue;
                }
                cosC = (a*a + b*b - c*c)/(2*a*b);
//					System.out.println("cosC="+cosC);
                if(cosC > curAngle) {
                    curAngle = cosC;
                }
                if(curAngle > minAngle) {  //ֵԽ���ԽС
                    maxNO = i;
                    minAngle = curAngle;
                }
                i++;
            }//end while
            System.out.println("minAngle="+minAngle+" curList="+maxNO+" flag="+flag);
            if(minAngle > limitAngle) {
                targetList.add(list.get(maxNO));
                System.out.println("targetList="+maxNO);
                runAngle(list, p1, maxNO,1);
                runAngle(list, maxNO, p2,2);
            }else {
                Delpt(list, p1, p2);
                delTotal++;
                System.out.println("Delpt: p1="+p1+" p2="+p2+"  ɾ��"+(p2-p1)+"�����ݵ�");
            }
        }
    }

    public static void main(String[] args) throws Exception {

        ArrayList<Point> points=new ArrayList<>();
        GetDataFromFile getData=new GetDataFromFile();
        limitAngle = (float) -0.9836571;
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\1.plt");
        ArrayList<Point> beforeTraj=new ArrayList<Point>();
        beforeTraj =getData.getDataFromFile(10000,"1");
        System.out.println(beforeTraj.size());
        runAngle(beforeTraj, 0, beforeTraj.size()-1,1);
//			runAngle(list, 2, 111,1);
        System.out.println(targetList.size()+"   共删除"+delTotal+"个轨迹点");

        getData.writeDataTomysql(points);

        double cpL = ((double)targetList.size() / (double)beforeTraj.size())* 100;
        DecimalFormat df = new DecimalFormat("0.000000");
        System.out.println("压缩率为："+ df.format(cpL) + "%");


    }





}
