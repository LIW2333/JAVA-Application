package com.chatproj;

public class UserModel {
    private Integer uid; // primary key
    private String username;
    private String password;
    private String email;
    private String friends;
    private String groups;
    private Integer status;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    // @Override
    // public String toString() {
    //     return "User{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
    // }
}
