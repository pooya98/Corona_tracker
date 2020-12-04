package com.example.corona_tracker.AdminClass;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotiData implements Serializable {

    public final static String DATE_PAT = "yyyy-MM-dd HH:mm:ss";

    Calendar time, lastTime;
    String title, content;
    String author_id;
    String ImageList;

    public NotiData() {
        time = Calendar.getInstance();
    }

    public NotiData(Calendar _time, Calendar _lastTime, String title, String _content, String ImageList) {
        this.time = _time;
        this.lastTime = _lastTime;
        this.title = title;
        this.content = _content;
        this.ImageList = ImageList;
    }

    public void updateData(Calendar _time, String title, String _content, String ImageList) {
        this.time = _time;
        this.title = title;
        this.content = _content;
        this.ImageList = ImageList;
    }

    public String getTimeToText(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        String ret = format.format(time.getTime());
        return ret;
    }

    public int setTimeFromText(String str){
        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        Date dateTime = null;
        try {
            dateTime = format.parse(str);
        } catch (ParseException e) {
            return -1;
        }
        this.time.setTime(dateTime);
        return 0;
    }

    public String getLastTimeToText(){
        if(lastTime == null) return "";
        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        String ret = format.format(lastTime.getTime());
        return ret;
    }

    public int setLastTimeFromText(String str){
        if(lastTime == null) lastTime = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        Date dateTime = null;
        try {
            dateTime = format.parse(str);
        } catch (ParseException e) {
            return -1;
        }
        this.lastTime.setTime(dateTime);
        return 0;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Calendar getLastTime() {
        return lastTime;
    }

    public void setLastTime(Calendar lastTime) {
        this.lastTime = lastTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageList() {
        return ImageList;
    }

    public void setImageList(String imageList) {
        ImageList = imageList;
    }
}
