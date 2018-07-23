package com.example.admin.svsplacement;

/**
 * Created by sabari on 4/20/2018.
 */

public class FeedbackClass {
    String name,mail,feedback,phone,time;

    public FeedbackClass() {
    }

    public FeedbackClass(String name, String mail, String feedback, String phone, String time) {
        this.name = name;
        this.mail = mail;
        this.feedback = feedback;
        this.phone = phone;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
