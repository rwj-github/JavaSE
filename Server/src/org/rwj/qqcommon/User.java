package org.rwj.qqcommon;

import java.io.Serializable;

/**
 * @author rwj
 * @create 2022-06-28 8:35
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String pwd;

    public User() {
    }

    public User(String userId, String pwd) {
        this.userId = userId;
        this.pwd = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
