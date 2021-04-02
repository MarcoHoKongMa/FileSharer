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
    private Socket socket = null;

    // Lists
    ListView<String> leftListView = new ListView();
    ListView<String> rightListView = new ListView();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Command line arguments
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();

        try {
            socket = new Socket("127.0.0.1", 1024);                            // Connect to the server
            clientOutput = new PrintWriter(socket.getOutputStream(), true);            // Output stream
            clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));   // Input Stream

        } catch (IOException e) {
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

        // Right Panel - Files in the Server
        refreshRightPanel();

        // Download button event
        button1.setOnAction(e -> {
            ObservableList<Integer> selectedFileName = rightListView.getSelectionModel().getSelectedIndices();
            try {
                clientOutput.println("DOWNLOAD");
                clientOutput.println(filesList[selectedFileName.get(0)].getName());

                File srcFile = new File(directory.getPath() + "\\" + filesList[selectedFileName.get(0)].getName());
                PrintWriter writer = new PrintWriter(srcFile);
                String endOfFile = "false";

                while(!(endOfFile.equals("true"))) {
                    writer.println(clientInput.readLine());
                    endOfFile = clientInput.readLine();
                }

                leftListView.getItems().add(srcFile.getName());
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (IndexOutOfBoundsException exception) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No file chosen to download");
                alert.showAndWait();
            }
        });

        // Upload button event
        button2.setOnAction(e -> {
            ObservableList<Integer> selectedFileName = leftListView.getSelectionModel().getSelectedIndices();
            try {
                clientOutput.println("UPLOAD");
                clientOutput.println(filesList[selectedFileName.get(0)].getName());
                try {
                    FileReader fileInput = new FileReader(directory.getPath()
                            + "\\" + (filesList[selectedFileName.get(0)].getName()));
                    BufferedReader bufferedReader = new BufferedReader(fileInput);
                    String line;
                    while((line = bufferedReader.readLine()) != null) {
                        clientOutput.println(line);
                        clientOutput.println("false");
                    }
                    clientOutput.println();
                    clientOutput.println("true");

                    refreshRightPanel();
                }catch(IOException exception) {
                    exception.printStackTrace();
                }
            }catch (IndexOutOfBoundsException exception) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No file chosen to upload");
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(hBox, splitPane);

        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.show();
    }

    public void refreshRightPanel() throws IOException {
        clientOutput.println("DIR");

        int index = 0;
        int length = Integer.parseInt(clientInput.readLine());
        String line;

        rightListView.getItems().clear();
        while(index < length) {
            rightListView.getItems().add(clientInput.readLine());
            index++;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
