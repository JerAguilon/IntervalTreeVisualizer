package apploader;

import controller.GraphDisplayerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by jeremy on 11/15/16.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/GraphDisplayer.fxml"));
        primaryStage.setTitle("Interval graph teaching tool");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

/*        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/GraphDisplayer.fxml"));
        loader.load();
        GraphDisplayerController controller = loader.getController();
        controller.setMainApp(this);*/

        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
