package com.chatproj;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static Connection conn;
    DBManager(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatroom","root","");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn(){
        return conn;
    }

    public static List<String> getUserList() throws SQLException{
        String sql = "SELECT * FROM user";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        List<String> userList = new ArrayList<String>();
        while(rs.next()){
            userList.add(rs.getString("name"));
        }
        return userList;
    }

    public static String getgroupname(String number) throws SQLException {
        // Connection conn = DButil.getConn();
        String sql = "SELECT groupname FROM t_group WHERE groupnumber = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,number);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("groupname");
        }else{
            return new String("error");
        }
    }
    public static String getgroupnumber(String name) throws SQLException {
        // Connection conn = DButil.getConn();
        String sql = "SELECT groupnumber FROM t_group WHERE groupname = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("groupnumber");
        }else{
            return new String("error");
        }
    }

    public static String getMembers(String groupnumber) throws SQLException {
        // String members = new String();
        // Connection conn = DButil.getConn();
        String sql = "SELECT users FROM t_group WHERE groupnumber = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,groupnumber);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return rs.getString("users");
        }else{
            return "error";
        }
    }

    public static int getRigisters() throws SQLException {
        // Connection conn = DButil.getConn();
        String sql = "SELECT COUNT(*) FROM user ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return Integer.parseInt(rs.getString("COUNT(*)"));
        }else{
            return  -1;
        }
    }

    public static int getMsgcount() throws SQLException {
        // Connection conn = DButil.getConn();
        String sql = "SELECT Msgcount FROM T_Statistic";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return Integer.parseInt(rs.getString("Msgcount"));
        }else {
            return -1;
        }
    }

    public static List<String> getFriend(String username) throws SQLException {
        // Connection con = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs =  ps.executeQuery();
        List<String> friendsList = new ArrayList<String>();
        if (rs.next()){
            String friends[] = rs.getString("friends").split(" ");
            for (String friend: friends) {
                friendsList.add(friend);
            }
        }
        return friendsList;
    }

    public static List<String> getGroup(String username) throws SQLException {
        // Connection conn = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        List<String> groupsList = new ArrayList<String>();
        if (rs.next()){
            String[] groups = rs.getString("groups").split(" ");
            for (String group: groups) {
                groupsList.add(group);
            }
        }
        return groupsList;
    }

    public static String getPassword(String username) throws SQLException {
        // Connection conn = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getString("password");
        }else{
            return "error";
        }
    }

    public static Integer getAccountByUserName(String username) throws SQLException {
        // Connection conn = DButil.getConn();
        String sql = "SELECT account FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("account");
        }else{
            return -1;
        }
    }

    public static Integer getStatus(String username) throws SQLException {
        // Connection conn = DButil.getConn();
        PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM user WHERE username=?");
        ps1.setString(1, username);
        ResultSet rs = ps1.executeQuery();
        if (rs.next()) {
            Integer status = rs.getInt("status");
            return status;
        }
        else {
            Integer status = -1;
            return  status;
        }
    }
    public static void setStatusOn(String username) throws SQLException {
        // Connection conn = DButil.getConn();
        PreparedStatement ps1 = conn.prepareStatement("UPDATE  user SET status=1 WHERE username=?");
        ps1.setString(1, username);
        ps1.execute();
    }
    public static void setStatusOff(String username) throws SQLException {
        // Connection conn = DButil.getConn();
        PreparedStatement ps1 = conn.prepareStatement("UPDATE  user SET status=0 WHERE username=?");
        ps1.setString(1, username);
        ps1.execute();
    }

    public static void register(String username, String password,String email) throws SQLException {
        // Connection conn = DButil.getConn();
        //(account,username,password,email,friend,groups,islogin)
        String sql = "insert into user(username, password, email, friends, status) VALUES (?,?,?,?,0)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3,email);
        ps.setString(4,"1 ");
//        ps.setString(5,"1 ");
        ps.executeUpdate();
    }

    public static void addFriend(String username, List<String> friendsList) throws SQLException {
        // Connection conn = DButil.getConn();
        String friends = "";
        for (String str: friendsList) {
            friends += str + " ";
        }
        PreparedStatement ps1 = conn.prepareStatement("UPDATE  user SET friends=? WHERE username=?");
        ps1.setString(1, friends);
        ps1.setString(2, username);
        ps1.execute();
    }

    // public 
}


