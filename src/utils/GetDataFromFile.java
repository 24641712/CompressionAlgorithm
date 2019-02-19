package utils;

import entity.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.util.ArrayList;

/**
 * @author
 */
public class GetDataFromFile {
    public static ArrayList<Point> getDataFromFile(File f, String uid)
            throws Exception {

        ArrayList<Point> list = new ArrayList<Point>();
        if (f.exists() && f.isFile()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(f));
            BufferedReader bReader = new BufferedReader(read);
            String str;
            while ((str = bReader.readLine()) != null) {
                Point point = new Point();
                String[] strings = str.split(",");
                point.setUid(uid);
                point.setLatitude(Double.parseDouble(strings[0]));
                point.setLongitude(Double.parseDouble(strings[1]));
                point.setAltitude(Double.parseDouble(strings[3]));
                point.setDate(strings[5]);
                point.setTime(strings[6]);
//            System.out.println(point.toString());
                list.add(point);
            }

            bReader.close();
            read.close();
        }
        return list;
    }


    public static void writeDataTomysql(ArrayList<Point> points) {
        // 将轨迹数据存入MySQL数据库
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/paper";
        String user="root";
        String password="123456";
        String sql;
        try {
            Class.forName(driver);
            java.sql.Connection	conn = DriverManager.getConnection(url,user,password);
            sql="insert into tb_data values(?,?,?,?,?,?)";
            java.sql.PreparedStatement stat=conn.prepareStatement(sql);

            for(int i=0;i<points.size();i++) {
                stat.setString(1,points.get(i).getUid());
                stat.setDouble(2, points.get(i).getLatitude());
                stat.setDouble(3, points.get(i).getLongitude());
                stat.setDouble(4, points.get(i).getAltitude());
                stat.setString(5, points.get(i).getDate());
                stat.setString(6, points.get(i).getTime());
                stat.executeUpdate();
            }

            stat.close();
            conn.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ArrayList< Point> list=new ArrayList<>();
        ArrayList< Point> list1=new ArrayList<>();
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        try {
            list=getDataFromFile(file,"4");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
//		list1.addAll(list.subList(2, 111));
        writeDataTomysql(list);
//		writeDataTomysql(list1);
        System.out.println(list.size());
        System.out.println("OK");
    }















}
