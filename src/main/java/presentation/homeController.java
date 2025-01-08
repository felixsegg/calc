package main.java.presentation;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class homeController implements Initializable {
    @FXML
    private Label outputLabel;
    private StringProperty output;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: initialize
        output = outputLabel.textProperty();
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
        throw new RuntimeException("Not implemented yet");
    }
    
    private void addNumber(char c) {
        String current = output.get();
        if (current.isEmpty()) output.set(String.valueOf(c));
        String regexCheckZero = ".*\\.[0*]?$|.*[^0]$";
        
        // String must not end in 0 or has a decimal point right in front of ending String of zeroes if new input is
        // supposed to be 0, otherwise it gets ignored.
        if (c != '0' || current.matches(regexCheckZero))
            output.set(current + c);
    }
    
    private void addOpeningParenthesis() {
        String current = output.get();
        if (current.isEmpty())
            output.set("(");
        else {
            char lastChar = current.charAt(current.length() - 1);
            switch (lastChar) {
                case '.' -> output.set(current.concat("0("));
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')' -> output.set(current.concat("×("));
                default -> output.set(current + '(');
            }
        }
    }
    
    private void addClosingParenthesis() {
        String current = output.get();
        if (!current.isEmpty() && current.charAt(current.length()-1) == '(')
            output.set(current.substring(0, current.length()-1));
        else {
            int countOpening = 0;
            int countClosing = 0;
            for (int i = 0; i < current.length(); i++) {
                if (current.charAt(i) == '(') countOpening++;
                else if (current.charAt(i) == ')') countClosing++;
            }
            if (countClosing < countOpening)
                output.set(current + ')');
        }
    }
    
    private void addMinus() {
        String current = output.get();
        if (current.isEmpty()) output.set("(-");
        else {
            char lastChar = current.charAt(current.length() - 1);
            switch (lastChar) {
                case '.' -> output.set(current.concat("0-"));
                case '-' -> output.set(current.substring(0, current.length()-1));
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
            case '+', '√', '×', '÷', '-' -> output.set(current.substring(0, current.length()-1) + c); // Substitute operation with new one
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')' -> output.set(current + c);
            // In case of '(' the input is ignored
        }
    }
    
    private void addDecimalPoint() {
        String current = output.get();
        // Regex: If a decimal point is already set, whether directly at the end or before an unbroken string of number
        // characters at the end, another one may not be put in.
        String regexNoDecPointBefore = "^(?!.*\\.\\d*$).*$";
        if (current.isEmpty()) output.set("0.");
        else if (current.matches(regexNoDecPointBefore)){
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
