package sql;

import entity.Point;

import java.sql.*;
import java.util.List;

/**
 * @version 1.0
 * @Description :
 * Copyright: Copyright (c)2019
 * Created Date : 2020/5/14
 */
public class SQLOPera {

    public static void batchInsert(List<Point> pointList,int uid){
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost/trajectory?useSSL=FALSE&serverTimezone=UTC";
        String user = "root";
        String password = "root";
        Connection con=null;
        PreparedStatement stat=null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            stat = con.prepareStatement("delete from tb_data");
            stat.executeUpdate();
            for(Point point : pointList){
               String sql = "insert into tb_data(id,longtitude,latitude)values("+
                       uid+","+
                    point.getLongitude()+","+point.getLatitude() +");";
               stat = con.prepareStatement(sql);
               stat.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try
            {
                if(!stat.isClosed()){
                    stat.close();
                }
                if(!con.isClosed()){
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }




}
