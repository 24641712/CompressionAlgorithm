package entity;

/**
 * 轨迹点
 * @author ccl
 */
public class Point {
    private String uid;

    private double latitude;  //纬度

    private double longitude;  //经度

    private double altitude;  //高度

    private String date;  //年月日

    private String time;  //时分秒

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Point{" +
                "uid='" + uid + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
