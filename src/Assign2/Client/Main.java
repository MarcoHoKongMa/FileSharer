package Assign2.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox root = new VBox();

        // Create buttons
        HBox hBox = new HBox();

        Button button1 = new Button("Download");
        Button button2 = new Button("upload");

//        button1.setOnAction(e -> {
//
//        });
//
        button2.setOnAction(e -> {
        File directory = new File("src\\Assign2\\Client\\TextFiles");


        });

        hBox.getChildren().addAll(button1, button2);

        // Create the split plane layout
        SplitPane splitPane = new SplitPane();
        VBox leftPanel = new VBox();
        VBox rightPanel = new VBox();
        ListView leftListView = new ListView();
        ListView rightListView = new ListView();
        leftPanel.getChildren().add(leftListView);
        rightPanel.getChildren().add(rightListView);
        splitPane.getItems().addAll(leftPanel, rightPanel);

        root.getChildren().addAll(hBox, splitPane);

        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
