package com.chatproj;

// import dao.Userdao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Register {
    private JPanel JPanel1;
    private JTextField textField_username;
    private JTextField textField_email;
    private JTextField textField_pwd1;
    private JTextField textField_pwd2;
    private JButton Button;
    private JPanel jp1;
    private JPanel jp2;
    private JPanel jp3;
    private JPanel jp4;
    private JPanel jp5;


    public Register() {
        JPanel1 = new JPanel(new GridLayout(5,1));
        
        textField_username = new JTextField("username");
        Dimension rdim = new Dimension(330,30);
        textField_username.setPreferredSize(rdim);
        jp1 = new JPanel(new FlowLayout());
        jp1.add(textField_username);
        textField_email = new JTextField("email");
        jp2 = new JPanel(new FlowLayout());
        jp2.add(textField_email);

        textField_email.setPreferredSize(rdim);
        textField_pwd1 = new JPasswordField("password1");
        textField_pwd1.setPreferredSize(rdim);
        jp3 = new JPanel(new FlowLayout());
        jp3.add(textField_pwd1);
        textField_pwd2 = new JPasswordField("password2");
        textField_pwd2.setPreferredSize(rdim);
        
        jp4 = new JPanel(new FlowLayout());
        jp4.add(textField_pwd2);
        Button = new JButton("Regist");
        Dimension dim2 = new Dimension(100,60);
        Button.setPreferredSize(dim2);
        jp5 = new JPanel(new FlowLayout());
        jp5.add(Button);
        JPanel1.add(jp1);
        JPanel1.add(jp2);
        JPanel1.add(jp3);
        JPanel1.add(jp4);
        JPanel1.add(jp5);
        Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String username = textField_username.getText();
                String pwd1 = new String(textField_pwd1.getText());
                String pwd2 = new String(textField_pwd2.getText());
                String email = textField_email.getText();
                if (!pwd1.equals(pwd2)){
                    JOptionPane.showMessageDialog(null, "two passwords not match");
                    return;
                }
                //TODO
                try {
                    
                    Integer status = DBManager.getStatus(username);
                    if(status != -1){
                        JOptionPane.showMessageDialog(null,"username:"+username+"+has existed!");
                    }else {
                        DBManager.register(username, pwd1, email);
                        JOptionPane.showMessageDialog(null,"register successfully\n\raccount:"+status+"\n\rpassword:"+pwd1+"\n\remail:"+email+"");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    public static void showRegister(){
        JFrame frame = new JFrame("register");
        frame.setContentPane(new RegisterWindow().JPanel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("R egister");
        frame.setContentPane(new RegisterWindow().JPanel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }
}
