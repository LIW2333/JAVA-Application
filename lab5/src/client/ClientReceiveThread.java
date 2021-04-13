package com.chatproj;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ClientReceiveThread implements Runnable{
    private Socket socket;
    private String username;
    private List<String> friendsList;
    private boolean isRun = true;
    private JList list1;
    private JList list2;
    private static JTextArea textArea;
    private MessageList messages;
    

    public ClientReceiveThread(Socket socket, String username, JTextArea jtextArea, JList list1, JList list2, List<String> friends, MessageList messages) throws SQLException {
        this.socket = socket;
        this.username = username;
        textArea= jtextArea;
        this.list1 = list1;
        this.list2 = list2;
        friendsList = friends;
        this.messages = messages;
       
    }

    /**
     * this constructor is used for terminal test.
     * @param socket
     * @param username
     * @param friends
     */
    public ClientReceiveThread(Socket socket, String username, List<String> friends) {
        this.socket = socket;
        this.username = username;
        friendsList = friends;
        
    }

    public void close() {
        this.isRun = false;
    }

    @Override
    public void run() {
        while (isRun) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                MessageModel message = (MessageModel)ois.readObject();
                if (message.getType().equals("group")) {
                    String content = message.getContent();
                    System.out.println("From: " + message.getSource() + " in group"
                            + "\n" + content + "-----"
                            + message.getTime());
                    
                    textArea.append(message.getSource() + ": " + content + "\n");
                }
                else if (message.getType().equals("personal")) {
                    String content = message.getContent();
                    System.out.println("From: " + message.getSource()
                            + "\n" + content + "-----"
                            + message.getTime());
                    //TODO
                    
                    System.out.println("+++++++++++++++++++");
                    messages.add(username, content);
                    System.out.println(message.getSource() + ": " + content + "\n");
                    System.out.println("+++++++++++++++++++");
                    list1.setSelectedValue(message.getSource(), true);
                }
                else if (message.getType().equals("addFriendResponse")) {
                    String content = message.getContent();
                    if (!content.equals("disagree")) {
                        friendsList.add(content);
                        DefaultListModel<String> listModel = new DefaultListModel<String>();
                        for (String friend: friendsList) {
                            listModel.addElement(friend);
                        }
                        list1.setModel(listModel);
                        JOptionPane.showMessageDialog(null, "This user is your friend!", "Success", JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        //system info will inform that this username is invalid or not exist in the Database;
                        JOptionPane.showMessageDialog(null, "This user may not exist", "Failed", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clientSendMessage(MessageModel message, Socket socket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
    }

}
