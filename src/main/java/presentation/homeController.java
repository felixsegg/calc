package main.java.presentation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class homeController implements Initializable {
    @FXML
    private Label outputLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: initialize
    }
    
    @FXML
    private void keyClick(MouseEvent mouseEvent) {
        try {
            Label clickedLabel = (Label) mouseEvent.getSource();
             switch (clickedLabel.getText()) {
                 case "√" -> temp();
                 case "(" -> temp();
                 case ")" -> temp();
                 case "÷" -> temp();
                 case "7" -> temp();
                 case "8" -> temp();
                 case "9" -> temp();
                 case "×" -> temp();
                 case "4" -> temp();
                 case "5" -> temp();
                 case "6" -> temp();
                 case "-" -> temp();
                 case "1" -> temp();
                 case "2" -> temp();
                 case "3" -> temp();
                 case "+" -> temp();
                 case "C" -> temp();
                 case "0" -> temp();
                 case "." -> temp();
                 case "=" -> temp();
             }
        } catch (ClassCastException ex) {
            throw new RuntimeException("FATAL: Somehow, the keyClick() method in homeController.java was called from something other than a Label.");
        }
    }
    
    private void temp() {
    
    }
}
