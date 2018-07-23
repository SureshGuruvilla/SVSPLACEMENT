package com.example.admin.svsplacement;

/**
 * Created by sabari on 4/8/2018.
 */

public class NotificationHelper {

    private String desc,dp,name,posts,s_post,s_uid,time,type;

    public NotificationHelper(String desc, String dp, String name, String posts, String s_post, String s_uid, String time, String type) {
        this.desc = desc;
        this.dp = dp;
        this.name = name;
        this.posts = posts;
        this.s_post = s_post;
        this.s_uid = s_uid;
        this.time = time;
        this.type = type;
    }

    public NotificationHelper() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getS_post() {
        return s_post;
    }

    public void setS_post(String s_post) {
        this.s_post = s_post;
    }

    public String getS_uid() {
        return s_uid;
    }

    public void setS_uid(String s_uid) {
        this.s_uid = s_uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
