package Algorithm;

import entity.Point;
import utils.GetDataFromFile;

import java.io.File;
import java.util.ArrayList;

/**
 * SQUISH_E算法
 * @Author ccl
 * @Date 2019/3/5
 */
public class SQUISH_E {

    public static  void SQUISH_EAlgorothm(ArrayList<Point> beforeTraj){





    }






    public static void main(String[] args) throws Exception {
        ArrayList<Point> beforeTraj = new ArrayList<>();
        GetDataFromFile getData = new GetDataFromFile();
        File file = new File("F:\\GeolifeTrajectoriesData\\000\\Trajectory\\15.plt");
        beforeTraj = getData.getDataFromFile(file,"1");




    }
}
