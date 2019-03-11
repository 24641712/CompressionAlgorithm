package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *工具类用来对时间进行操作
 * @Author ccl
 * @Date 2019/3/5
 */
public class GetDate {
    private Date date;

    public GetDate(String date, String time) {
        SimpleDateFormat sdfd =new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        try {
            this.date = sdfd.parse(date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date, String time) {
        SimpleDateFormat sdfd =new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        try {
            this.date = sdfd.parse(date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
