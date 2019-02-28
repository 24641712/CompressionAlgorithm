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
    protected double p2pdis(Point pa,Point pb) {
        double d;
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
    public double CalculatedDis(Point pa, Point pb, Point pc){
        double curdis = 0;
        double cosA,cosB,sinA;
        double c = p2pdis(pa,pb);
        double a = p2pdis(pb,pc);
        double b = p2pdis(pa,pc);
        if(a==0 || b==0 || c==0){
            return 0;
        }
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
     *
     *@param d 计算度
     *@return
     **/
    protected  double Rad(double d){
        return d * Math.PI / 180.0;
    }

    /*
     *计算两个轨迹点间的距离
     *@param pA 轨迹点
     *@param pB 轨迹点
     *@return 两点间的距离
     **/
    protected  double geoDist(Point pA,Point pB){
        double radLat1 =  Rad(pA.getLatitude());
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

    /*
     *
     *@param A 轨迹点
     *@param B 轨迹点
     *@param C 轨迹点
     *@return 距离
     **/
    public double getDistance(Point A,Point B,Point C){
        double distance = 0;
        double a = Math.abs(geoDist(A,B));//计算边长
        double b = Math.abs(geoDist(B,C));
        double c = Math.abs(geoDist(A,C));
        double p = (a + b + c)/2.0;
        double s = Math.sqrt(p * (p-a) * (p-b) * (p-c)); //计算面积
        distance = s * 2.0 / a; //求出距离
        return distance;
    }
}
