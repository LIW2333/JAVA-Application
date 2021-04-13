package com.chatproj;

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.io.OutputStream;
// import java.io.OutputStreamWriter;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client {
    public String user;
    public List<String> friends = new ArrayList<String>();
    public List<String> groups = new ArrayList<String>();

    Client(){
        user = "";
    }

    // private Socket socket;
    public void SendMessage(String type, String content, Socket s) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        MessageModel message = new MessageModel();
        message.setType(type);
        message.setSource(user);
        message.setTime(System.currentTimeMillis());
        message.setSource("server");
        message.setContent(content);
        oos.writeObject(message);
    }
    //
    public int Connect(boolean login, Socket s) {
        
        while (login) {
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                MessageModel message = (MessageModel) ois.readObject();
                if (message.getType().equals("loginResponse")) {
                    String[] content = message.getContent().split(" ");
                    if (content[0].equals("disagree")) {
                        login = false;
                        int errno = Integer.parseInt(content[1]);
                        return errno;
                    }
                    else if (content[0].equals("agree")) {
                        // check = true;
                        for (String friend: content[1].split("\n")) {
                            friends.add(friend);
                        }
                        for (String group: content[2].split("\n")) {
                            groups.add(group);
                        }
                        login = false;
                        return 0;
                    }
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return 3;
    }
 }