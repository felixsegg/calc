package presentation;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class Toast {
    public static void show(Window window, String message) {
        Label label = new Label(message);
        label.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        
        Popup popup = new Popup();
        popup.getContent().add(label);
        popup.setAutoHide(true);
        
        popup.show(window);
        popup.setX(window.getX() + window.getWidth() / 2 - label.getWidth() / 2);
        popup.setY(window.getY() + window.getHeight() - 100.0);
        
        PauseTransition delay = new PauseTransition(Duration.millis(3000));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }
}
