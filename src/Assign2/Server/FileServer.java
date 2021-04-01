package Assign2.Server;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * FileServer class used to represent the server of a file sharing system
 * @author  Ting Wu, Marco Ma
 * @version 1.0
 * @since   2021-03-28
 */

public class FileServer {
    // Class Parameters
    private static int serverPort = 1024;   // default port number being used
    private static int maxClients = 25;     // maximum number of threads
    private int numClients = 0;             // counter
    private FileServerThread[] threads;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    // Constructor
    public FileServer() {
        try {
            serverSocket = new ServerSocket(serverPort);                                            // Create a server socket
            threads = new FileServerThread[maxClients];                                             // Create an array of threads
            while(true) {                                                                           // Wait for client to connect to server
                clientSocket = serverSocket.accept();                                               // Clients Socket (which port the client is using)
                threads[numClients] = new FileServerThread(clientSocket);   // Create a new thread
                threads[numClients].start();
                numClients++;
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Main method to execute the server class
    public static void main(String[] args) {
        FileServer fileServer = new FileServer();
    }
}
