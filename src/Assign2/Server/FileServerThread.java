package Assign2.Server;

import java.net.*;
import java.io.*;
import java.util.*;

public class FileServerThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String command;

    public FileServerThread(Socket socket) {
        this.socket = socket;
    }

//    InputStream inputStream = clientSocket.getInputStream();        // set up the input stream
//    InputStreamReader reader = new InputStreamReader(inputStream);  // read from the input stream
//    BufferedReader in = new BufferedReader(reader);
//    String line = null;
//
//    File newFile = new File("testing.text");
//    FileWriter myWriter = new FileWriter(newFile);
//                while((line = in.readLine()) != null){
//        myWriter.write(line+"\n");
//    }
}
