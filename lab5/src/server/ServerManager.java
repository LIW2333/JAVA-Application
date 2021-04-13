package com.chatproj;

import java.util.*;

public class ServerManager {
    private static Map<String, ServerThread> map = new HashMap<String, ServerThread>();
    private static List<String> userList = new ArrayList<String>();

    public static void add(String username, ServerThread Server){
        map.put(username, Server);
        userList.add(username);
    }

    public static ServerThread get(String username){
        return map.get(username);
    }


    public static void remove(String username){
        map.remove(username);
        userList.remove(username);
    }

    public static boolean contains(String username) {
        if (map.containsKey(username) && userList.contains(username)) {
            return true;
        }
        return false;
    }

    public static List<String> getUserList(){
        return userList;
    }

    public static String printUsers() {
        String users = "\n";
        for (String user: userList) {
            users += user + "\n";
        }
        return  users;
    }
}
