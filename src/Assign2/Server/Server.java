package Assign2.Server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args){
        ArrayList<File> files = new ArrayList<>();

        try{
            ServerSocket serverSocket = new ServerSocket(1024); // Server Socket
            Socket clientSocket = serverSocket.accept(); // Clients Socket (which port the client is using)
            InputStream inputStream = clientSocket.getInputStream(); // set up the input stream
            InputStreamReader reader = new InputStreamReader(inputStream); // read from the input stream
            BufferedReader in = new BufferedReader(reader);
            String line = null;

            File newFile = new File("testing.text");
            FileWriter myWriter = new FileWriter(newFile);
            while((line = in.readLine()) != null){
                myWriter.write(line+"\n");
            }

        }catch(Exception e){
            System.out.println(e);
        }
    }
}
