package estimate;

import entity.Point;
import utils.Distance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 评估轨迹压缩算法的性能
 * @author ccl
 */
public class Estimate {

    /*
     *计算轨迹的压缩率
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
     *计算轨迹的压缩误差
     *@param beforeTraj 压缩前的轨迹
     *@param afterTraj 压缩后的轨迹
     *@return void
     **/
    public void CompressionError(ArrayList<Point> beforeTraj,
                                 ArrayList<Point> afterTraj){
        double total = 0;
        Distance distance = new Distance();
        Collections.sort(afterTraj);
      for(int i=0;i<afterTraj.size()-1;i++){
//        int i=96;
          Point pa = afterTraj.get(i);
          Point pb = afterTraj.get(i+1);
          for(int j = pa.getPid()+1;j<pb.getPid();j++){
              Point pc = beforeTraj.get(j);
              double temp = distance.CalculatedDis(pa,pb,pc);
              if(-100 <temp&&temp < 1000){
                  total += temp;
              }else {
                  System.out.println("测试结果："+i);
              }

          }
      }
        System.out.println("压缩误差为："+total/(beforeTraj.size()-1));
    }

}
