package com.qst.yunpan.pojo;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String countSize;
    private String totalSize;
    public static final String NAMESPACE = "username";
    public static final String RECYCLE = "recycle";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountSize() {
        return countSize;
    }

    public void setCountSize(String countSize) {
        this.countSize = countSize;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public static String getNAMESPACE() {
        return NAMESPACE;
    }

    public static String getRECYCLE() {
        return RECYCLE;
    }


}