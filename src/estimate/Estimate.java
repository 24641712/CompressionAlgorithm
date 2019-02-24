package estimate;

import entity.Point;
import utils.Distance;

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
     *就算轨迹的压缩误差
     *@param beforeTraj 压缩前的轨迹
     *@param afterTraj 压缩后的轨迹
     *@return void
     **/
    public void CompressionError(ArrayList<Point> beforeTraj,
                                 ArrayList<Point> afterTraj){
        float total = 0;
        Distance distance = new Distance();
      for(int i=0;i<afterTraj.size()-1;i++){
          Point pa = afterTraj.get(i++);
          Point pb = afterTraj.get(i);
          for(int j = pa.getPid()+1;j<pb.getPid();j++){
              Point pc = beforeTraj.get(j);
              total += distance.CalculatedDis(pa,pb,pc);
          }
      }
        System.out.println("压缩误差为："+total/(beforeTraj.size()-1));
    }

}
