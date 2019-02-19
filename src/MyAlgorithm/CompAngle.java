package MyAlgorithm;

import entity.Point;
import utils.GetDataFromFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author ccl
 */
public class CompAngle {
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

    static void Delpt(ArrayList<Point> list,int a,int b) {
        int c=a+1;
        while(c<b)  {
            list.get(c).setRes('F');
            System.out.println("ddd+"+c);
            c++;
        }
    }
    public static void runAngle(ArrayList< Point> list,int p1,int p2,int flag) {

        float a,b,c,cosC,curAngle,minAngle;
        int i = 0,maxNO = 0;
//			p1=298;
//			p2=300;
        System.out.println("p1="+p1+", p2="+p2+" flag="+flag);
        if(p2-p1 >= limitAngle) {
            c = p2pdis(list.get(p1), list.get(p2));
            minAngle=-1;
            i=p1+1;
            while(i < p2) {
                curAngle = -1;
                a = p2pdis(list.get(p1), list.get(i));
                b = p2pdis(list.get(p2), list.get(i));
                if(a == 0 || b == 0) {
                    maxNO=i;
                    i++;
                    continue;
                }
                cosC = (a*a + b*b - c*c)/(2*a*b);
//					System.out.println("cosC="+cosC);
                if(cosC >= curAngle) {
                    curAngle = cosC;
                }
                if(curAngle >= minAngle) {  //ֵԽ���ԽС
                    maxNO = i;
                    minAngle = curAngle;
                }
                i++;

            }//end while
            System.out.println("minAngle="+minAngle+" maxNO="+maxNO+" flag="+flag);
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

    public static void main(String[] args) throws Exception {

        ArrayList<Point> points = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        float comp=0;
        GetDataFromFile getData = new GetDataFromFile();
        limitAngle = (float) -0.9969571;
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\1.plt");
        ArrayList<Point> beforeTraj=new ArrayList<Point>();
        beforeTraj = getData.getDataFromFile(file,"1");
        System.out.println(beforeTraj.size());
        System.out.println("����ѹ���ʣ�%����");
        comp = 1-(float)(Double.parseDouble(scanner.next())/100.0);
        limitAngle = beforeTraj.size()/(comp*beforeTraj.size());
        System.out.println("limitAngle="+limitAngle);

        runAngle(beforeTraj, 0, beforeTraj.size()-1,1);
        System.out.println(targetList.size()+"   共删除"+delTotal+"个轨迹点");
        getData.writeDataTomysql(points);

        double cpL = ((double)targetList.size() / (double)beforeTraj.size())* 100;
        DecimalFormat df = new DecimalFormat("0.000000");
        System.out.println("压缩率"+ df.format(cpL) + "%");


    }





}
