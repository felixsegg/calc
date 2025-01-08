package main.java.presentation;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class homeController implements Initializable {
    @FXML
    private HBox topBoxHBox;
    @FXML
    private GridPane keyGrid;
    @FXML
    private Label outputLabel, backspaceLabel;
    @FXML
    private VBox root;
    private StringProperty output;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: initialize
        output = outputLabel.textProperty();
        
        initializeOutputLabel();
        initializeKeyGrid();
        Platform.runLater(this::initializeHotkeys);
    }
    
    private void initializeOutputLabel() {
        root.widthProperty().addListener((obs, oldV, newV) -> outputLabel.setMaxWidth(newV.doubleValue() - topBoxHBox.getSpacing() - backspaceLabel.getWidth()));
    }
    
    private void initializeKeyGrid() {
        ReadOnlyDoubleProperty gridHProp = keyGrid.heightProperty();
        ReadOnlyDoubleProperty gridWProp = keyGrid.widthProperty();
        
        for (Node n : keyGrid.getChildren()) {
            if (n instanceof Label l) {
                gridHProp.addListener((obs, oldV, newV) -> l.setPrefHeight(newV.doubleValue() / 5.0));
                gridWProp.addListener((obs, oldV, newV) -> l.setPrefWidth(newV.doubleValue() / 4.0));
            }
        }
    }
    
    private void initializeHotkeys() {
        root.getScene().setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case ENTER -> equalsClick();
                case DIGIT0, NUMPAD0 -> addNumber('0');
                case DIGIT1, NUMPAD1 -> addNumber('1');
                case DIGIT2, NUMPAD2 -> addNumber('2');
                case DIGIT3, NUMPAD3 -> addNumber('3');
                case DIGIT4, NUMPAD4 -> addNumber('4');
                case DIGIT5, NUMPAD5 -> addNumber('5');
                case DIGIT6, NUMPAD6 -> addNumber('6');
                case DIGIT7, NUMPAD7 -> addNumber('7');
                case DIGIT8, NUMPAD8 -> addNumber('8');
                case DIGIT9, NUMPAD9 -> addNumber('9');
                case PERIOD, COMMA, DECIMAL -> addDecimalPoint();
                case MULTIPLY, STAR -> addOperation('×');
                case DIVIDE, SLASH -> addOperation('÷');
                case PLUS, ADD -> addOperation('+');
                case MINUS, SUBTRACT -> addMinus();
                case BACK_SPACE -> backspaceClick();
            }
        });
    }
    
    @FXML
    private void keyClick(MouseEvent mouseEvent) {
        try {
            Label clickedLabel = (Label) mouseEvent.getSource();
            char labelChar = clickedLabel.getText().charAt(0);
            switch (labelChar) {
                case '√' -> addSqrt();
                case '×', '÷', '+' -> addOperation(labelChar);
                case '(' -> addOpeningParenthesis();
                case ')' -> addClosingParenthesis();
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> addNumber(labelChar);
                case '-' -> addMinus();
                case '.' -> addDecimalPoint();
                case 'C' -> clearClick();
                case '=' -> equalsClick();
                default ->
                        throw new RuntimeException("FATAL: Somehow, the clicked label contained an illegal character.");
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("FATAL: Somehow, the keyClick() method in homeController.java was called from something other than a Label.");
        } catch (IndexOutOfBoundsException ex) {
            throw new RuntimeException("FATAL: Somehow, the clicked label had no text.");
        }
    }
    
    @FXML
    private void backspaceClick() {
        String current = output.get();
        if (current.length() > 0) output.setValue(current.substring(0, current.length() - 1));
    }
    
    private void clearClick() {
        output.set("");
    }
    
    private void equalsClick() {
        System.out.println("Jetzt Exception");
        throw new RuntimeException("Not implemented yet");
    }
    
    private void addNumber(char c) {
        String current = output.get();
        if (current.isEmpty()) output.set(String.valueOf(c));
        String regexCheckZero = ".*\\.[0*]?$|.*[^0]$";
        
        // String must not end in 0 or has a decimal point right in front of ending String of zeroes if new input is
        // supposed to be 0, otherwise it gets ignored.
        if (c != '0' || current.matches(regexCheckZero)) output.set(current + c);
    }
    
    private void addOpeningParenthesis() {
        String current = output.get();
        if (current.isEmpty()) output.set("(");
        else {
            char lastChar = current.charAt(current.length() - 1);
            switch (lastChar) {
                case '.' -> output.set(current.concat("0×("));
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')' -> output.set(current.concat("×("));
                default -> output.set(current + '(');
            }
        }
    }
    
    private void addClosingParenthesis() {
        String current = output.get();
        if (current.isEmpty()) return;
        char lastChar = current.charAt(current.length() - 1);
        if (current.charAt(current.length() - 1) == '(') output.set(current.substring(0, current.length() - 1));
        else if (lastChar != '-' && lastChar != '+' && lastChar != '√' && lastChar != '×' && lastChar != '÷') {
            int countOpening = 0;
            int countClosing = 0;
            for (int i = 0; i < current.length(); i++) {
                if (current.charAt(i) == '(') countOpening++;
                else if (current.charAt(i) == ')') countClosing++;
            }
            if (countClosing < countOpening) {
                if (lastChar == '.') output.set(current + '0');
                output.set(current + ')');
            }
        }
    }
    
    private void addMinus() {
        String current = output.get();
        if (current.isEmpty()) output.set("(-");
        else {
            char lastChar = current.charAt(current.length() - 1);
            switch (lastChar) {
                case '.' -> output.set(current.concat("0-"));
                case '-' -> output.set(current.substring(0, current.length() - 1));
                case '+', '√', '×', '÷' -> output.set(current.concat("(-"));
                default -> output.set(current + '-');
            }
        }
    }
    
    private void addOperation(char c) {
        // For '+', '×', '÷'
        String current = output.get();
        if (current.isEmpty()) return;
        
        char lastChar = current.charAt(current.length() - 1);
        switch (lastChar) {
            case '.' -> output.set(current + '0' + c);
            case '+', '×', '÷' ->
                    output.set(current.substring(0, current.length() - 1) + c); // Substitute operation with new one
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')' -> output.set(current + c);
            // In case of '(', '-' or '√' the input is ignored
        }
    }
    
    private void addDecimalPoint() {
        String current = output.get();
        // Regex: If a decimal point is already set, whether directly at the end or before an unbroken string of number
        // characters at the end, another one may not be put in.
        String regexNoDecPointBefore = "^(?!.*\\.\\d*$).*$";
        if (current.isEmpty()) output.set("0.");
        else if (current.matches(regexNoDecPointBefore)) {
            char lastChar = current.charAt(current.length() - 1);
            switch (lastChar) {
                case '+', '√', '×', '÷', '-', '(' -> output.set(current.concat("0."));
                case ')' -> output.set(current.concat("×0."));
                default -> output.set(current + '.');
            }
        }
    }
    
    private void addSqrt() {
        String current = output.get();
        if (current.isEmpty()) output.set("√");
        else {
            char lastChar = current.charAt(current.length() - 1);
            switch (lastChar) {
                case '.' -> output.set(current + '0' + '×' + '√');
                case '+', '×', '÷', '-', '(', '√' -> output.set(current + '√');
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')' -> output.set(current + '×' + '√');
            }
        }
    }
}
