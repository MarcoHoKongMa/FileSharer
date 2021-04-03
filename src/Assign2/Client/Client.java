package Assign2.Client;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client extends Application {

    private PrintWriter clientOutput;
    private BufferedReader clientInput;
    private Socket socket = null;
    private File directory = null;
    private File[] filesList = null;
    private ArrayList<String> serverFiles = null;

    // Lists
    ListView<String> leftListView = new ListView<>();
    ListView<String> rightListView = new ListView<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Command line arguments
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();

        // Connect to the server
        reconnect();

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
        Alert info =  new Alert(Alert.AlertType.INFORMATION, "Please choose a folder to share");
        info.showAndWait();
        DirectoryChooser directoryChooser= new DirectoryChooser();          // Configure shared folder graphically
        directoryChooser.setInitialDirectory(new File("."));
        directory = directoryChooser.showDialog(primaryStage);
        leftListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        refreshLeftPanel();

        // Right Panel - Files in the Server
        rightListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        refreshRightPanel();

        // Download button event
        button1.setOnAction(e -> {
            ObservableList<Integer> selectedFileName = rightListView.getSelectionModel().getSelectedIndices();
            for(int index: selectedFileName) {
                reconnect();
                try {
                    clientOutput.println("DOWNLOAD");
                    clientOutput.println(serverFiles.get(selectedFileName.get(index)));

                    File srcFile = new File(directory.getPath() + "\\" + serverFiles.get(index));
                    srcFile = new File(srcFile.getAbsolutePath());
                    PrintWriter writer = new PrintWriter(srcFile);
                    String endOfFile = "false";

                    while(!(endOfFile.equals("true"))) {
                        writer.println(clientInput.readLine());
                        endOfFile = clientInput.readLine();
                    }

                    refreshLeftPanel();
                    socket.close();
                    clientInput.close();
                    clientOutput.close();
                    writer.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (IndexOutOfBoundsException exception) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "No file chosen to download");
                    alert.showAndWait();
                }
            }
        });

        // Upload button event
        button2.setOnAction(e -> {
            ObservableList<Integer> selectedFileName = leftListView.getSelectionModel().getSelectedIndices();
            ObservableList<String> selectedItemsName = leftListView.getSelectionModel().getSelectedItems();
            for(int i = 0; i < selectedFileName.size(); i++) {
                // Make sure it must be a file
                if(!(selectedItemsName.get(i).equals(".")) && filesList[selectedFileName.get(i) - 1].isFile()) {
                    reconnect();
                    try {
                        clientOutput.println("UPLOAD");
                        clientOutput.println(filesList[selectedFileName.get(i) - 1].getName());
                        FileReader fileInput = new FileReader(directory.getPath()
                                + "\\" + (filesList[selectedFileName.get(i) - 1].getName()));
                        BufferedReader bufferedReader = new BufferedReader(fileInput);
                        String line;

                        // Pass contents of the file
                        while((line = bufferedReader.readLine()) != null) {
                            clientOutput.println(line);
                            clientOutput.println("false");
                        }
                        clientOutput.println();
                        clientOutput.println("true");

                        refreshRightPanel();
                        socket.close();
                        clientOutput.close();
                        clientInput.close();
                    } catch (IndexOutOfBoundsException exception) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "No file chosen to upload");
                        alert.showAndWait();
                    } catch(IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        // Navigate Up a Folder
        leftListView.setOnMouseClicked(click -> {
            int index = 0;

            if(click.getClickCount() == 2) {
                ObservableList<Integer> selectedFileName = leftListView.getSelectionModel().getSelectedIndices();
                ObservableList<String> selectedItemsName = leftListView.getSelectionModel().getSelectedItems();
                for(String items: selectedItemsName) {
                    if(items.equals(".")) {
                        directory = new File(directory.getPath() + "\\..");
                        refreshLeftPanel();
                        break;
                    }
                    else if(filesList[selectedFileName.get(index) - 1].isDirectory()) {
                        directory = new File(directory.getPath() + "\\" +
                                filesList[selectedFileName.get(index) - 1].getName());
                        refreshLeftPanel();
                        break;
                    }
                    else if(items.endsWith(".txt")) {
                        VBox vbox = new VBox();
                        Stage stage = new Stage();

                        int textWindowSize = 500;

                        TextArea text = new TextArea();
                        text.setPrefWidth(textWindowSize);
                        text.setPrefHeight(textWindowSize);

                        try {
                            FileReader input = new FileReader(directory.getPath() + "\\" +
                                    filesList[selectedFileName.get(0) - 1].getName());
                            BufferedReader bufferedReader = new BufferedReader(input);
                            String line;

                            while((line = bufferedReader.readLine()) != null) {
                                text.appendText(line + "\n");
                            }

                            bufferedReader.close();
                            input.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }

                        vbox.getChildren().addAll(text);

                        stage.setTitle(items);
                        stage.setScene(new Scene(vbox, textWindowSize, textWindowSize));
                        stage.getIcons().add(new Image("file:filesTransfer.png"));
                        stage.show();
                    }
                    index++;
                }
            }
        });

        root.getChildren().addAll(hBox, splitPane);

        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.getIcons().add(new Image("file:filesTransfer.png"));
        primaryStage.show();
    }

    private void refreshLeftPanel() {
        leftListView.getItems().clear();
        filesList = directory.listFiles();

        leftListView.getItems().add(".");
        if(filesList != null) {
            for(File file: filesList) {
                leftListView.getItems().add(file.getName());
            }
        }
    }

    public void refreshRightPanel() throws IOException {
        clientOutput.println("DIR");

        int index = 0;
        int length = Integer.parseInt(clientInput.readLine());
        serverFiles = new ArrayList<>();
        String fileNames;

        rightListView.getItems().clear();
        while(index < length) {
            fileNames = clientInput.readLine();
            rightListView.getItems().add(fileNames);
            serverFiles.add(fileNames);
            index++;
        }
        socket.close();
        clientInput.close();
        clientOutput.close();
    }

    public void reconnect(){
        try {
            socket = new Socket("127.0.0.1", 1024);                            // Connect to the server
            clientOutput = new PrintWriter(socket.getOutputStream(), true);            // Output stream
            clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));   // Input Stream

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
