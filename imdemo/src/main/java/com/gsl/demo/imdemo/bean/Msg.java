package com.gsl.demo.imdemo.bean;

import java.util.Date;

/**
 * Created by guosenlin on 16-10-18.
 */

public class Msg {
    public static final int MSG_SEND = 0;
    public static final int MSG_RECEIVE = 1;

    private String content;
    private int type;
    private String author;
    private Date date;

    public Msg(String content, int type, String author, Date date) {
        this.content = content;
        this.type = type;
        this.author = author;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
