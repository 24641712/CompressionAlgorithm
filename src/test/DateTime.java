package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0
 * @Description :
 * Copyright: Copyright (c)2019
 * Created Date : 2020/4/1
 */
public class DateTime {
    public static void main(String[] args) throws ParseException {
        String  strdate = "2008-12-20 13:22:40";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(strdate);
        System.out.println(date);
        System.out.println(date.getTime());





    }



}
