package entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 轨迹点
 * @author ccl
 */
public class Point implements Comparable<Point> {

    private String uid;

    private int Pid;

    private double latitude;  //纬度

    private double longitude;  //经度

    private double altitude;  //高度

    private String date;  //年月日

    private String time;  //时分秒

    private long totalseconds;

    private char Res='T';  //是否是特征点

    private double priority;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int pid) {
        Pid = pid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public char getRes() {
        return Res;
    }

    public void setRes(char res) {
        Res = res;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public long getTotalseconds() {
        return totalseconds;
    }

    public void setTotalseconds(String strdate,String strtime) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(strdate);
        buffer.append(" ");
        buffer.append(strtime);
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(buffer.toString());
            this.totalseconds = date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.totalseconds = totalseconds;
    }

    @Override
    public String toString() {
        return "Point{" +
                "uid='" + uid + '\'' +
                ", Pid=" + Pid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", Res=" + Res +
                '}';
    }

    /*
     *
     *@param o
     *@return
     **/
    @Override
    public int compareTo(Point o) {
        int i = this.getPid()- o.getPid();//按照Pid属性排序
        return i;
    }
}
