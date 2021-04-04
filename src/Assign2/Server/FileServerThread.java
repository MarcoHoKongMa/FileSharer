package Assign2.Server;

import java.net.*;
import java.io.*;

/**
 * FileServerThread used to represent a thread for a server that process requests
 * from the client such as the download button and upload button.
 * @author  Ting Wu, Marco Ma
 * @version 1.0
 * @since   2021-03-28
 */

public class FileServerThread extends Thread {
    private Socket socket;
    private BufferedReader serverInput;
    private PrintWriter serverOutput;
    private File directory = new File("src\\Assign2\\Server\\TextFiles");

    // Constructor
    public FileServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.serverOutput = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * The run method executes a thread and performs some task.
     */
    public void run() {
        while(true) {
            // DIR, Upload, or Download
            try {
                String command = serverInput.readLine();

                if(command.equals("DIR")) {              // DIR - List Contents
                    String[] fileNames = dir();
                    serverOutput.println(fileNames.length);
                    for (String fileName: fileNames) {
                        serverOutput.println(fileName);
                    }
                }
                else if(command.equals("UPLOAD")) {      // UPLOAD
                    uploadFile();
                }
                else if(command.equals("DOWNLOAD")){      // DOWNLOAD
                    downloadFile();
                }
            } catch(IOException e) {
                e.printStackTrace();
            } catch(NullPointerException e) {
                break;
            }
        }
    }

    /**
     * This function returns a string array of all the local
     * textfile names to the client.
     * @return String[]. String array of local text file names.
     */
    public String[] dir() {
        File[] filesList = directory.listFiles();
        String[] fileNames = new String[1];

        if(filesList != null) {
            fileNames = new String[filesList.length];
            for (int i = 0; i < fileNames.length; i++) {
                fileNames[i] = filesList[i].getName();
            }
        }

        return fileNames;
    }

    /**
     * This method recieves a textfile name and contents from a client and
     * creates a new textfile that is stored in the TextFiles directory.
     * @throws IOException
     */
    public void uploadFile() throws IOException {
        File srcFile = new File(directory.getPath() + "\\" + serverInput.readLine());
        PrintWriter writer = new PrintWriter(srcFile);
        String endOfFile = "false";

        while(!(endOfFile.equals("true"))) {
            writer.write(serverInput.readLine()+"\n");
            endOfFile = serverInput.readLine();
        }
        writer.close();
    }

    /**
     * This method sends a textfile name and contents to a client
     * where they will reconstruct the file using the filename
     * and its contents.
     * @throws IOException
     */
    public void downloadFile() throws IOException {
        File srcFile = new File(directory.getPath() + "\\" + serverInput.readLine());
        FileReader fileInput = new FileReader(srcFile);
        BufferedReader bufferedReader = new BufferedReader(fileInput);

        String line;
        while((line = bufferedReader.readLine()) != null) {
            serverOutput.println(line);
            serverOutput.println("false");
        }
        serverOutput.println();
        serverOutput.println("true");
        bufferedReader.close();
    }
}
