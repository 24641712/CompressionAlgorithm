import entity.Point;
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

    //分割一条轨迹点信息
    static Point Spilict(String str){
        Point point=new Point();
        String[] strings=str.split(",");
        point.setLatitude(Double.parseDouble(strings[0]));
        point.setLongitude(Double.parseDouble(strings[1]));
        point.setAltitude(Double.parseDouble(strings[3]));
        point.setDate(strings[5]);
        point.setTime(strings[6]);
        return point;
    }

    // 从数据源文件中获取轨迹数据
    static ArrayList<Point> getDataFromFile(File file){
        int i=0;
        ArrayList< Point> arrayList=new ArrayList<Point>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s = bReader.readLine()) != null) {
                sb.append(s + "\n");
                Point point = new Point();
                point = Spilict(s);
                arrayList.add(point);
            }
            bReader.close();
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arrayList;
    }
    //	计算两点之间的距离
    static float p2pdis(Point pa,Point pb) {
        float d;
        d=(float)Math.sqrt(
                (pa.getLatitude()-pb.getLatitude())*
                (pa.getLatitude()-pb.getLatitude())+
                (pa.getLongitude()-pb.getLongitude())*
                (pa.getLongitude()-pb.getLongitude()));
        return d;
    }
    //删除轨迹点
    static void Delpt(ArrayList<Point> list,int a,int b) {
        int c=a+1;
        while(c<b)  {
            list.get(c).setRes('F');
            c++;
        }

    }

    /*
     * DP算法
     */
    static void Simp(ArrayList<Point> list,int p1,int p2){
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
                Simp(list,p1,maxNO);
                Simp(list,maxNO,p2);

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

        ArrayList<Point> list = new ArrayList<Point>();
        ArrayList<Point> points = new ArrayList<>();
        //获取数据
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        GetDataFromFile getData = new GetDataFromFile();
        LimitDis = (float) 0.0000298;
        list =getData.getDataFromFile(file,"1");

        Simp(list,0,list.size()-1);
        System.out.println(list.size());
        System.out.println(targetList.size()+"   删除"+delTotal+"个轨迹点");

//        points = com.org.data.dbData.transform(targetList, "5");
//        com.org.data.dbData. writeDataTomysql(points);

        double cpL  =  ((double)targetList.size() / (double)list.size())* 100;
        DecimalFormat df  =  new DecimalFormat("0.000000");
        System.out.println("压缩率"+ df.format(cpL) + "%");

    }

}
