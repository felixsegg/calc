

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/home/home.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Taschenrechner");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
        primaryStage.setScene(new Scene(root));
        
        primaryStage.show();
        
        // stage must not be smaller than the initial size. Has to be set after .show(), since the values are set only after.
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
