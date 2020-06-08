package utils;

import entity.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @version 1.0
 * @Description :
 * Copyright: Copyright (c)2019
 * Created Date : 2020/2/13
 */
public class FilterNodes {
    public static List<Point> filter(List<Point> list){
        List<Point> result = new ArrayList<>(new HashSet<Point>(list));//去重后的集合
        Collections.sort(result);
        return result;

    }
}
