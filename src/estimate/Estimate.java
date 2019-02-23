package estimate;

import entity.Point;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 评估轨迹压缩算法的性能
 * @author ccl
 */
public class Estimate {

    /*
     *计算轨迹点的压缩率
     *@param before 压缩前的轨迹点数
     *@param after 压缩后的轨迹点数
     *@return void
     **/
    public void CompressionRatio(int before,int after){
        double cpL = ((double)after / (double)before)* 100;
        DecimalFormat df = new DecimalFormat("0.000000");
        System.out.println("压缩率为："+ df.format(cpL) + "%");
    }

    /*
     *计算两个轨迹点间的距离
     *@param pa 轨迹点
     *@param pb 轨迹点
     *@return 两个轨迹点间的距离
     **/
     protected float p2pdis(Point pa,Point pb) {
        float d;
        d=(float)Math.sqrt(
                (pa.getLatitude()-pb.getLatitude())*
                (pa.getLatitude()-pb.getLatitude())+
                (pa.getLongitude()-pb.getLongitude())*
                (pa.getLongitude()-pb.getLongitude()));
        return d;
    }

    /*
     *计算点的垂直距离
     *@param pa 轨迹点
     *@param pb 轨迹点
     *@param pc 轨迹点
     *@return 垂直距离
     **/
    protected float CalculatedDis(Point pa,Point pb,Point pc){
        float curdis;
        float cosA,cosB,sinA;
        float c = p2pdis(pa,pb);
        float a = p2pdis(pb,pc);
        float b = p2pdis(pa,pc);
        cosA = (b*b+c*c-a*a)/(2*b*c);
        cosB = (a*a+c*c-b*b)/(2*a*c);
        sinA = (float)Math.sqrt(1-cosA*cosA);
        if(cosA == 0){
            curdis = b;
        }else if(cosB == 0){
            curdis = a;

        }else {
            curdis = b * sinA;
        }
       return curdis;
    }

    /*
     *就算轨迹的压缩误差
     *@param beforeTraj 压缩前的轨迹
     *@param afterTraj 压缩后的轨迹
     *@return void
     **/
    public void CompressionError(ArrayList<Point> beforeTraj,
                                 ArrayList<Point> afterTraj){
        float total = 0;
      for(int i=0;i<afterTraj.size()-1;i++){
          Point pa = afterTraj.get(i++);
          Point pb = afterTraj.get(i);
          for(int j = pa.getPid()+1;j<pb.getPid();j++){
              Point pc = beforeTraj.get(j);
              total += CalculatedDis(pa,pb,pc);
          }
      }
        System.out.println("压缩误差为："+total/(beforeTraj.size()-1));
    }

}
