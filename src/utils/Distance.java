package utils;

import entity.Point;

/**
 * 工具类计算轨迹的垂直距离
 * @Author ccl
 * @Date 2019/2/24
 */
public class Distance {

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
    public float CalculatedDis(Point pa, Point pb, Point pc){
        float curdis = 0;
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
}
