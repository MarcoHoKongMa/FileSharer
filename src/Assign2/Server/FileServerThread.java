package Assign2.Server;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * FileServerThread used to represent a thread for a server that process requests
 * from the client such as taking command line arguments.
 * @author  Ting Wu, Marco Ma
 * @version 1.0
 * @since   2021-03-28
 */

public class FileServerThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String computerName;
    private String filePath;

    private String command;

    public FileServerThread(Socket socket) {
        this.socket = socket;
        this.computerName = computerName;
        this.filePath = filePath;
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


    /**
     * The run method executes a thread and performs some task.
     */
    public void run(){

    }
}
