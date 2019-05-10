package m;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @Author
 * @Date 2019/5/2
 */
public class Main {
    public static void main(String[] args) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/temperatureacq?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "123456";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed()){
                System.out.println("成功连接到数据库");
            }
            Statement stat = con.createStatement();
            String sql = "select * from tb_facility";
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()){
               String handler = rs.getString("handler");
                System.out.println(handler);
            }
           rs.close();
           stat.close();
           con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }


    }



}
