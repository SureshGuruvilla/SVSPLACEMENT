package com.example.admin.svsplacement;

/**
 * Created by ADMIN on 4/3/2018.
 */

public class CommentClass {
    private String Comment;
    private String Name;
    private String dp;

    public CommentClass(String comment, String name, String dp) {
        Comment = comment;
        Name = name;
        this.dp = dp;
    }

    public CommentClass() {
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
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
}
