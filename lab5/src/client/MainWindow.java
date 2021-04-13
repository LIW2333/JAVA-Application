
package com.chatproj;


// import client.AddFriend;
// import client.ClientReceiveThread;
// import client.GroupMessageCollection;
// import client.MessageCollection;
// import model.Message;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class MainWindow {
    private JPanel JPanel1;
    private JList list1;
    private JTextArea textArea_msglist;
    private JTextField textField_msgsend;
    private JButton sendButton;
    private JLabel Lable_name;
    private JLabel Lable_usersnum;
    private JPanel JPanel_chatwindow;
    private JTabbedPane tabbedPane1;
    private JList list2;
    private JLabel Lable_username;
    private JLabel Lable_icon;
    private JPanel tp1;
    private JPanel tp2;
    private JTextField friend_name;
    private JButton button_add_friend;
    private static String username;
    private static Socket s;
    private static List<String> friendsList;
    private static List<String> groupsList; 
    private static Client client = new Client();
    private static MessageList messages;
    // private static GroupMessageCollection messages;

    public MainWindow() throws SQLException {

        //load the friends list into list1
        DefaultListModel<String> listModelFriend = new DefaultListModel<String>();
        for (String friend: friendsList) {
            listModelFriend.addElement(friend);
        }
        list1.setModel(listModelFriend);
        messages = new MessageList(username);
        //load the groups list into the list2
        DefaultListModel<String> listModelGroup = new DefaultListModel<String>();
        for (String group: groupsList) {
            listModelGroup.addElement(group);
        }
        list2.setModel(listModelGroup);

        messages = new MessageList(username);
        Thread t = new Thread(new ClientReceiveThread(s, username, textArea_msglist, list1, list2, friendsList, messages));
        t.start();

        sendButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MessageModel message = new MessageModel();
                message.setContent(textField_msgsend.getText());
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                message.setTime(System.currentTimeMillis());
                try {
                    if (list1.getSelectedValue() == null && list2.getSelectedValue() != null) {
                        message.setType("group");
                        message.setReciver(list2.getSelectedValue().toString());
                        messages.add(list2.getSelectedValue().toString(),"\t\t\t\tMe:" + message.getContent() + "\n\r");
                    }
                    else if (list1.getSelectedValue() != null && list2.getSelectedValue() == null) {
                        message.setType("personal");
                        message.setReciver(list1.getSelectedValue().toString());
                        messages.add(list1.getSelectedValue().toString(), "\t\t\t\tMe:" + message.getContent() + "\n\r");
                    } else {
                        textArea_msglist.append("please select a user or group！\n\r");
                        return;
                    }
                    message.setSource(username);
                } catch (Exception e1) {
                    textArea_msglist.append("please select a user to send message\n\r");
                    return;
                }
                try {
                    ClientReceiveThread.clientSendMessage(message, s);
                } catch (IOException e1) {
                    textArea_msglist.append("please connect to the server\n\r");
                    return;
                }
                textArea_msglist.append("\t\t\t\tMe:" + message.getContent() + "\n\r");
                textField_msgsend.setText("");
            }
        });

        button_add_friend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String friend = friend_name.getText();
                if (friendsList.contains(friend)) {
                    JOptionPane.showMessageDialog(null, "This user has been your friend", "Failed", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                else {
                    try {
                        client.user = username;
                        // .add(s, username, friend);
                        client.SendMessage("addFriend", friend, s);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (list1.getSelectedValue()==null) {
                        return;
                    }
                    Lable_name.setText(list1.getSelectedValue().toString());
                    if (!list2.isSelectionEmpty()) {
                        list2.clearSelection();
                    }
                    String content = messages.showContent(list1.getSelectedValue().toString());
                    textArea_msglist.setText(content);
                }
            }
        });

        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    if (list2.getSelectedValue()==null) {
                        return;
                    }
                    Lable_name.setText(list2.getSelectedValue().toString());
                    if (!list1.isSelectionEmpty()) {
                        list1.clearSelection();
                    }
                    String content = messages.showContent(list2.getSelectedValue().toString());
                    textArea_msglist.setText(content);
                }
            }
        });
    }

    public static void InitMain(String userName, Socket socket, List<String> friends, List<String> groups) throws SQLException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Chat Room    "+ userName);
        username = userName;
        friendsList = friends;
        groupsList = groups;
        s = socket;
        frame.setContentPane(new MainWindow().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,600));
        frame.setLocation(200,200);
        frame.pack();
        frame.setVisible(true);
    }

}
