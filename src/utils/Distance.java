package utils;

import entity.Point;

import java.util.Date;

/**
 * 工具类计算轨迹的垂直距离和同步欧式距离
 * @Author ccl
 * @Date 2019/2/24
 */
public class Distance {

    protected double p2pdis(Point pa,Point pb) {
        /*
         *计算两个轨迹点间的距离
         *@param pa 轨迹点
         *@param pb 轨迹点
         *@return 两个轨迹点间的距离
         **/
        double distance;
        distance = (float)Math.sqrt(
                (pa.getLatitude()-pb.getLatitude())*
                        (pa.getLatitude()-pb.getLatitude())+
                        (pa.getLongitude()-pb.getLongitude())*
                                (pa.getLongitude()-pb.getLongitude()));
        return distance;
    }

    public double CalculatedDis(Point pa, Point pb, Point pc){
        /*
         *利用正弦值计算点的垂直距离
         *@param pa 轨迹点
         *@param pb 轨迹点
         *@param pc 轨迹点
         *@return 垂直距离
         **/
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

    protected  double Rad(double d){
        /*
         *函数功能：角度转弧度
         *@param d:角度
         *@return 返回的是弧度
         **/
        return d * Math.PI / 180.0;
    }

    protected  double geoDist(Point pA,Point pB){
        /*
         *计算两个轨迹点间的距离
         *@param pA 起始点
         *@param pB 结束点
         *@return 距离
         **/
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

    public double getDistance(Point A,Point B,Point C){
        /*
         *使用面积公式计算垂直距离
         *@param A 起始点
         *@param B 结束点
         *@param C 当前点
         *@return 点C到A和B所在直线的距离
         **/
        double distance = 0;
        double a = Math.abs(geoDist(A,B));//计算边长
        double b = Math.abs(geoDist(B,C));
        double c = Math.abs(geoDist(A,C));
        double p = (a + b + c)/2.0;
        double s = Math.sqrt(p * (p-a) * (p-b) * (p-c)); //计算面积
        distance = s * 2.0 / a; //求出距离
        return distance;
    }

    public double getSedDist(Point A,Point B,Point C){
        /*
         *计算C到A和B所在直线的同步欧氏距离
         *@param A 起始点
         *@param B 结束点
         *@param C 当前点
         *@return 同步欧式距离值
         **/
        Date timeA = new GetDate(A.getDate(),A.getTime()).getDate();
        Date timeB = new GetDate(B.getDate(),B.getTime()).getDate();
        Date timeC = new GetDate(C.getDate(),C.getTime()).getDate();
        double percent = (timeC.getTime()-timeA.getTime())
                /(timeB.getTime()-timeA.getTime());
        double latitude = A.getLatitude()+
                (B.getLatitude()-A.getLatitude())*percent;
        double longitude = A.getLongitude()+
                (B.getLongitude()-A.getLongitude())*percent;
        Point point = new Point();
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        double distance = p2pdis(point,C);
        return distance;
    }

}
