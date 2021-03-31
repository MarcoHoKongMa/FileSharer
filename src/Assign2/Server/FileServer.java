package Assign2.Server;

import java.net.*;
import java.io.*;
import java.util.*;

public class FileServer {
    private static int serverPort = 1024;
    private static int maxClients = 25;
    private int numClients = 0;

    private FileServerThread[] threads;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ArrayList<File> files = new ArrayList<>();

    public FileServer() {
        try {
            serverSocket = new ServerSocket(serverPort);        // Server Socket
            threads = new FileServerThread[maxClients];         // Threads
            while(true) {
                clientSocket = serverSocket.accept();           // Clients Socket (which port the client is using)
                threads[numClients] = new FileServerThread(clientSocket);
                numClients++;
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileServer fileServer = new FileServer();
    }
}
