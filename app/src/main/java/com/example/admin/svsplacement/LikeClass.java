package com.example.admin.svsplacement;

/**
 * Created by ADMIN on 4/3/2018.
 */

public class LikeClass {
    private String Name;
    private String dp;
    private String time;
    public LikeClass(String name, String dp, String time) {
        Name = name;
        this.dp = dp;
        this.time = time;
    }

    public LikeClass() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
