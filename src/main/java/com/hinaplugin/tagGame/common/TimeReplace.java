package com.hinaplugin.tagGame.common;

public class TimeReplace {

    public String replace(int time){
        if (time > 3600){
            int hour = time / 3600;
            int min = (time % 3600) / 60;
            int sec = time - ((hour * 3600) + (min * 60));
            return hour + "時間" + min + "分" + sec + "秒";
        }else if (time > 60){
            int min = time / 60;
            int sec = time % 60;
            return min + "分" + sec + "秒";
        }else {
            return time + "秒";
        }
    }

    public int color(int allTime, int nowTime){
        double progress = (double) nowTime / allTime;
        if (progress > 0.5){
            return 0;
        }else if (progress > 0.2){
            return 1;
        }else {
            return 2;
        }
    }
}
