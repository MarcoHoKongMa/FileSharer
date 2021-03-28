package Assign2.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox root = new VBox();

        // Create the Menubar and add the menu buttons
        MenuBar menuBar = new MenuBar();
        Menu download = new Menu("Download");
        Menu upload = new Menu("Upload");
        menuBar.getMenus().addAll(download, upload);

        // Create the split plane layout
        SplitPane splitPane = new SplitPane();
        VBox leftPanel = new VBox();
        VBox rightPanel = new VBox();
        ListView leftListView = new ListView();
        ListView rightListView = new ListView();
        leftPanel.getChildren().add(leftListView);
        rightPanel.getChildren().add(rightListView);
        splitPane.getItems().addAll(leftPanel, rightPanel);

        root.getChildren().addAll(menuBar, splitPane);

        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
