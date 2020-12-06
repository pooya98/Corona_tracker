package com.example.corona_tracker;

public class EnterData {
    private String user_id;
    private String user_phone;
    private String enter_time;

    public EnterData(String user_id, String user_phone, String enter_time) {
        this.user_id = user_id;
        this.user_phone = user_phone;
        this.enter_time = enter_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(String enter_time) {
        this.enter_time = enter_time;
    }
}
