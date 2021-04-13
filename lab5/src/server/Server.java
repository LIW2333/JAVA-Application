package com.chatproj;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ServerThread implements Runnable{
    private ServerSocket serverSocket;
    private JLabel label;
    private static JTextArea textArea;
    private String username;
    private String password;
    private String serverName;
    private static boolean isStart = true;
    private static JList list_users;
    private static JLabel Label_username;
    private static JTextArea textArea_state;


    public ServerThread(ServerSocket serverSocket,JLabel label,
                        JTextArea jtextArea,JList jList,
                        JLabel jLabel,String ServerName,
                        JTextArea textArea2_state, JScrollPane Online_users){
        this.serverSocket = serverSocket;
        this.label = label;
        textArea = jtextArea;
        this.serverName = ServerName;
        list_users = jList;
        Label_username = jLabel;
        textArea_state = textArea2_state;
    }


    private void sendMsg(String type,String username, String content, Socket s) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        MessageModel message = new MessageModel();
        message.setType(type);
        message.setTime(System.currentTimeMillis());
        message.setSource(serverName);
        message.setReciver(username);
        message.setContent(content);
        oos.writeObject(message);
        // return message;
    }

    public void sendMsgPersonal(MessageModel message, Socket s) throws SQLException, IOException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(message);
        }catch (Exception e){
            System.out.println(message.getReciver() + "getter is off line now!\n\r");
        }
    }


    @Override
    public void run() {
        while (isStart){
            try {
                Socket s = serverSocket.accept();
                //TODO
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                MessageModel message = (MessageModel) ois.readObject();
                // MessageModel message = receiveLoginInfo(s);
                if (message.getType().equals("loginRequest")) {
                    String databasePassword = DBManager.getPassword(username);
                    Integer status = DBManager.getStatus(username);
                    MessageModel response = new MessageModel();
                    String content = "";
                    
                    if (databasePassword.equals(password) && status == 0) {
                        //login allowed
                        DBManager.setStatusOn(username);
                        content = "agree";

                        //set friendsList
                        List<String> friendsList = DBManager.getFriend(username);
                        String friends = "";
                        for (String str: friendsList) {
                            friends += str + "\n";
                        }
                        content += " " + friends;
                        List<String> groupsList = DBManager.getGroup(username);
                        String groups = "";
                        for (String groupName: groupsList) {
                            groups += groupName + "\n";
                        }
                        content += " " + groups;
                        // returnMessage.setContent(content);

                        //create the serverReceiveThread for this username
                        //TODO 
                        ServerThread ReceiveThread = new ServerThread(username,s);
                        ServerManager.add(username, ReceiveThread);
                        Thread t = new Thread(ReceiveThread, username);
                        t.start();
                    }
                    else if (!databasePassword.equals(password)){
                        //wrong username or password
                        content = "disagree" + " " + "1";
                        // returnMessage.setContent(content);
                    }
                    else {
                        //This username is login
                        content = "disagree" + " " + "2";
                    }
                    sendMsg("loginResponse", username, content, s);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }


    public static void closeServer(){
        isStart = false;
    }


    
    public void sendMsgToAll(MessageModel message,Socket s) throws IOException, SQLException {
        List<String> userList = DBManager.getUserList();
        for (String user: userList) {
            ServerThread serverReceiveThread = ServerManager.get(user);
            try{
                sendMsgPersonal(message, s);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
    }
}
