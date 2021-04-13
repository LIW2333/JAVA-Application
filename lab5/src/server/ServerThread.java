package com.chatproj;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ServerThread implements Runnable{
    private Socket s;
    private JTextArea textArea;
    private JLabel label;
    private String username;
    private JTextArea textArea_state;
    private static boolean isRun = true;

    public ServerThread(String username, Socket socket, JLabel label, JTextArea textArea, JTextArea textArea2_state){
        this.username = username;
        this.label = label;
        this.textArea = textArea;
        this.s = socket; 
        this.textArea_state = textArea2_state;
    }

    public Socket getSocket(){
        return s;
    }

    public void run() {
        while(isRun){
            try {
                System.out.println("start");
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                MessageModel message = (MessageModel) ois.readObject();
                System.out.println(message.getContent());
                System.out.println("done");
                /**
                 * forward the message by its type(group, personal or addFriend)
                 */
                if (message.getType().equals("group")){
                    List<String> userList =  ServerManager.getUserList();
                    for (String user: userList) {
                        if (user.equals(username)) {
                            continue;
                        }
                        ServerThread getterReceiveThread = ServerManager.get(user);
                        getterReceiveThread.sendMsgPersonal(message);
                    }
                }
                else if (message.getType().equals("personal")){
                    if (ServerManager.contains(message.getReciver())) {
                        ServerThread getterReceiveThread = ServerManager.get(message.getReciver());
                        getterReceiveThread.sendMsgPersonal(message);
                    }
                }
                else if (message.getType().equals("addFriendRequest")) {
                    String friendName = message.getContent();
                    String from = message.getSource();
                    MessageModel returnMessage = new MessageModel();

                    returnMessage.setReciver(from);
                    returnMessage.setType("addFriendResponse");
                    returnMessage.setTime(System.currentTimeMillis());
                    returnMessage.setSource("server");

                    List<String> friends = DBManager.getFriend(friendName);
                    if (friends == null) {
                        returnMessage.setContent("disagree");
                        sendMsgPersonal(returnMessage);
                    }
                    else {
                        returnMessage.setContent(friendName);
                        friends.add(from);
                        DBManager.addFriend(friendName, friends);
                        List<String> l = DBManager.getFriend(from);
                        l.add(friendName);
                        DBManager.addFriend(from, l);
                        sendMsgPersonal(returnMessage);

                        //inform the friend as well
                        returnMessage.setReciver(friendName);
                        ServerThread friendServerThread = ServerManager.get(friendName);
                        if (friendServerThread == null) {
                            textArea.append(friendName + " is not on the line right now!");
                            ServerManager.remove(friendName);
                        }
                        else {
                            friendServerThread.sendMsgPersonal(returnMessage);
                        }
                    }
                }
                else {
                    textArea.append("failed when accessing the type of message!\n\r");
                    textArea_state.append("Error：accessing the type of message failed！\n\r");
                }

                textArea.append(username+": to :"+message.getReciver()+": "+message.getContent()+"\n\r");

            } catch (IOException e) {
                    e.printStackTrace;
                try {
                    DBManager.setStatusOn(username);
                    // ServerThread.setOnline();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeThread() throws IOException {
        isRun = false;
        s.close();
    }

    /**
     * server send private message
     */
    public void sendMsgPersonal(MessageModel message) throws SQLException, IOException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(message);
        }catch (Exception e){
            textArea.append(message.getReciver() + "getter is off line now!\n\r");
        }
    }
}
