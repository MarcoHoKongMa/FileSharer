package Assign2.Client;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client extends Application {

    private PrintWriter clientOutput;
    private BufferedReader clientInput;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Command line arguments
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();

        try{
            Socket socket = new Socket("127.0.0.1", 1024);                            // Connect to the server
            clientOutput = new PrintWriter(socket.getOutputStream(), true);            // Output stream
            clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Input Stream

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

        ListView<String> leftListView = new ListView();
        ListView<String> rightListView = new ListView();
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

        // Download button event
        button1.setOnAction(e -> {
            ObservableList<Integer> selectedFileName = rightListView.getSelectionModel().getSelectedIndices();
            try {
                clientOutput.println(filesList[selectedFileName.get(0)].getName());
            }catch (IndexOutOfBoundsException i) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No file chosen");
                alert.showAndWait();
            }
        });

        // Upload button event
        button2.setOnAction(e -> {
            ObservableList<Integer> selectedFileName = leftListView.getSelectionModel().getSelectedIndices();
            try {
                clientOutput.println(filesList[selectedFileName.get(0)].getName());
            }catch (IndexOutOfBoundsException i) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No file chosen");
                alert.showAndWait();
            }
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
