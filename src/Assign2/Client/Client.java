package Assign2.Client;

import java.net.*;
import java.io.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Command line arguments
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();

        try{
            Socket socket = new Socket(args.get(0), 1024);
        }catch (IOException e) {
            e.printStackTrace();
        }

        double windowWidth = 550.0;
        double windowHeight = 500.0;
        VBox root = new VBox();

        // Create buttons
        HBox hBox = new HBox();

        Button button1 = new Button("Download");
        Button button2 = new Button("Upload");

        hBox.getChildren().addAll(button1, button2);

        // Create the split plane layout
        SplitPane splitPane = new SplitPane();

        VBox leftPanel = new VBox();
        VBox rightPanel = new VBox();

        ListView leftListView = new ListView();
        ListView rightListView = new ListView();
        leftListView.setPrefHeight(windowHeight);
        rightListView.setPrefHeight(windowHeight);

        leftPanel.getChildren().add(leftListView);
        rightPanel.getChildren().add(rightListView);
        splitPane.getItems().addAll(leftPanel, rightPanel);

        // Left Panel - Files in Local Client
        File directory = new File(args.get(1));
        File[] filesList = directory.listFiles();

        if(filesList != null) {
            for(File file: filesList) {
                leftListView.getItems().add(file.getName());
            }
        }

        // Buttons Event
        button1.setOnAction(e -> {
            leftListView.getItems().remove("hello");
        });

        button2.setOnAction(e -> {
            leftListView.getItems().add("hello");
        });

        root.getChildren().addAll(hBox, splitPane);

        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
