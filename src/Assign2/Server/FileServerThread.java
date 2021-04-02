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
    private BufferedReader serverInput;
    private PrintWriter serverOutput;

    // Constructor
    public FileServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.serverOutput = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * The run method executes a thread and performs some task.
     */
    public void run(){
        // DIR
        String[] fileNames = DIR();
        for (String fileName : fileNames) {
            serverOutput.println(fileName);
        }

        // Upload or Download
        try {
            if (serverInput.readLine().equals("UPLOAD")) {
                uploadFile();
            }
            else if (serverInput.readLine().equals("DOWNLOAD")){
                downloadFile();
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{   // Update the files the server has
            fileNames = DIR();
            for (String fileName : fileNames) {
                serverOutput.println(fileName);
            }
        }
    }

    public String[] DIR(){
        File directory = new File("\\src\\Assign2\\Server\\TextFiles");
        File[] filesList = directory.listFiles();
        System.out.println(filesList[0].getName());
        String[] fileNames = new String[filesList.length];

        for (int i=0; i< fileNames.length; i++){
            fileNames[i] = filesList[i].getName();
        }
        return fileNames;
    }

    public void uploadFile(){

    }

    public void downloadFile(){

    }
}
