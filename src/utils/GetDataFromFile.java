package utils;

import entity.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类：获取本地轨迹数据、写入数据库
 * @author ccl
 */
public class GetDataFromFile {

    private final static int size = 20000;

    public static List<Point> getDataFromFile(int number, String uid)
            throws Exception {
        List<Point> list = new ArrayList<Point>();
        boolean flag = false;
//        for(int i=1;i<=4;i++){
            String path = "F:\\trajs\\"+4+".plt";
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bReader = new BufferedReader(read);
                String str;
                int id = 0;
                while ((str = bReader.readLine()) != null) {
                    Point point = new Point();
                    String[] strings = str.split(",");
                    point.setUid(uid);
                    point.setPid(id++);
                    point.setLatitude(Double.parseDouble(strings[0]));
                    point.setLongitude(Double.parseDouble(strings[1]));
                    point.setAltitude(Double.parseDouble(strings[3]));
                    point.setDate(strings[5]);
                    point.setTime(strings[6]);
                    list.add(point);
                    if(list.size()>=number){
                        flag = true;
//                        break;
                    }
                }
                bReader.close();
                read.close();
            }
//            if(flag == true)
//                break;;
//        }
        return list.subList(0,size);
    }


    public static void writeDataTomysql(List<Point> points) {
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
}
