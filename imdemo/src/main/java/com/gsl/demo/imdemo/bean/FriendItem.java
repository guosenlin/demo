package com.gsl.demo.imdemo.bean;

/**
 * Created by guosenlin on 16-10-18.
 */

public class FriendItem {
    private String userId;
    private String nickName;
    private String topicUrl;
    private int type;

    public FriendItem(String userId, String nickName, String topicUrl, int type) {
        this.userId = userId;
        this.nickName = nickName;
        this.topicUrl = topicUrl;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
