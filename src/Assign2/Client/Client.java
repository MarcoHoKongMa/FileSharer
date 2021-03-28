package Assign2.Client;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args){
        try{
            Socket socket = new Socket("192.168.0.", 1024);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
