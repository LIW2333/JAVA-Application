package com.chatproj;

// package client.view;

// import client.ClientTest;
// import model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogWindow {
    private JPanel JPanel1;
    private JButton LoginButton;
    private JTextField textFieldAccount=new JTextField("account", 10);
    private JPasswordField passwordField=new JPasswordField("password",10);
    private JButton setportButton;
    private JButton registerButton;
    private static JFrame frame = new JFrame("Login");
    JPanel panel_up=new JPanel();
    JPanel panel_bottom=new JPanel();
    JPanel panel_small_1 =new JPanel();
	JPanel panel_small_2 =new JPanel();
    private String username;
    private String password;
    private int port = 2333;
    private String ip = "localhost";
    private Socket socket;

    private Client client = new Client();
    private List<String> friends = new ArrayList<String>();
    private List<String> groups = new ArrayList<String>();

    public LogWindow() {
        LoginButton = new JButton("Login");
        LoginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                username = new String(textFieldAccount.getText());
                password = new String(passwordField.getPassword());
                try {
                    //open up the main window

                    socket = new Socket(ip,port);


                    String content = username + " " + password;
                    client.user = username;
                    client.SendMessage("loginRequest", content, socket);
                    int connectMsg = client.Connect(true, socket);
                    if (connectMsg == 0) {
                        MainWindow.InitMain(username, socket, friends, groups);
                        frame.dispose();
                    }
                    else if (connectMsg == 1){
                        JOptionPane.showMessageDialog(null, "wrong username or password", "Failed", JOptionPane.PLAIN_MESSAGE);
                    }else if(connectMsg == 2){
                        JOptionPane.showMessageDialog(null, "this user is login", "Failed", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "unknow error", "Failed", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Please check the server and the port", "Failed", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        setportButton= new JButton("setport");
        setportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    port = Integer.parseInt(JOptionPane.showInputDialog(null,"Please input the port(Default 2333)：\n","2333"));
                    ip = JOptionPane.showInputDialog(null,"Please input the ip(Default localhost)：\n","localhost");
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        registerButton = new JButton("register");
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterWindow.showRegister();
            }
        });
        frame.setLayout(new BorderLayout());
		frame.add(panel_up,BorderLayout.CENTER);
		frame.add(panel_bottom,BorderLayout.SOUTH);
 

 
		panel_up.setLayout(new GridLayout(2,1));
		
		// panel_up.add(lbl_username);
		panel_up.add(panel_small_1);
		// panel_up.add(lbl_passsword);
		panel_up.add(panel_small_2);

		panel_small_1.setLayout(new FlowLayout());
		panel_small_1.add(textFieldAccount);
		panel_small_2.setLayout(new FlowLayout());
		panel_small_2.add(passwordField);
 
		panel_bottom.setLayout(new FlowLayout());
		
		panel_bottom.add(LoginButton);
		panel_bottom.add(registerButton);
		panel_bottom.add(setportButton);
    }
    

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        UIManager.setLookAndFeel(lookAndFeel);
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,320));
        frame.setLocation(250,350);
        frame.pack();
        frame.setVisible(true);
    }
}
