package utils;

import entity.Point;

import java.io.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类：获取本地轨迹数据、写入数据库
 * @author ccl
 */
public class GetDataFromFile {

    private final static int size = 1001734;

    public static List<Point> getDataFromFile(int number, String uid)
            throws Exception {
        number = size;
        List<Point> list = new ArrayList<Point>();
        boolean flag = false;
//        String path = "F:\\trajs";
        String path = "G:\\Geolife Trajectories 1.3\\Data\\001\\Trajectory";
//        String path = "G:\\trajTest\\";
        File dir = new File(path);
        File []files = dir.listFiles();
        int id = 0;
        for(File file : files){
            System.out.println(file.getName());
            InputStreamReader read = new InputStreamReader(new FileInputStream(file));
            BufferedReader bReader = new BufferedReader(read);
            String str;
            int skip = 0;
            while ((str = bReader.readLine()) != null) {
                if(skip<6){
                    skip++;
                    continue;
                }
                Point point = new Point();
                String[] strings = str.split(",");
                point.setUid(uid);
                point.setPid(id++);
                point.setLatitude(Double.parseDouble(strings[0]));
                point.setLongitude(Double.parseDouble(strings[1]));
                point.setAltitude(Double.parseDouble(strings[3]));
                point.setDate(strings[5]);
                point.setTime(strings[6]);
                point.setTotalseconds(strings[5],strings[6]);
                list.add(point);
                if(list.size() >= number){
                    bReader.close();
                    read.close();
                    return list;
                }
            }
            bReader.close();
            read.close();
        }
        return list;
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

    public static void writeToFile(List<Point> list) throws IOException {
        String path = "G:\\trajTest\\20200525.plt";
        OutputStream out = null;
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        try {
            out = new FileOutputStream(file);
            for(Point point:list){
                String str = point.getLatitude()+","+point.getLongitude()+","
                        +",0,492,39745.0902662037,2008-10-24,02:09:59\n";
                out.write(str.getBytes());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
