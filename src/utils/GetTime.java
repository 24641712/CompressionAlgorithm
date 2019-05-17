package utils;

/**
 * 计算程序耗时
 * @Author ccl
 * @Date 2019/3/11
 */
public class GetTime {
    private long startTime;

    private long endTime;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void showTime(){
        System.out.println("程序执行耗时："+(this.endTime-this.startTime)+"ms");
    }



}
