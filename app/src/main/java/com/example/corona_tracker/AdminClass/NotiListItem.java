package com.example.corona_tracker.AdminClass;

public class NotiListItem {
    private String Title;
    private String Content;
    private String date;
    private String thumbnailPath;

    public String getTitle() {
        if(Title == null) return "";
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        if(Content == null) return "";
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        if(date == null) return "";
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
