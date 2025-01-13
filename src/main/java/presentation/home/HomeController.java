package presentation.home;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import exception.TermSyntaxException;
import logic.service.CalculationService;
import presentation.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private HBox topBoxHBox;
    @FXML
    private GridPane keyGrid;
    @FXML
    private Label outputLabel, backspaceLabel;
    @FXML
    private VBox root;
    private Scene scene;
    private StringProperty output;
    private final CalculationService service = CalculationService.getInstance();
    private BigDecimal ans;
    private boolean isAns = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ans = BigDecimal.ZERO; // ANS is initially zero
        output = outputLabel.textProperty();
        
        // The scene might not be set instantly, so as soon as this happens, the hotkeys will be initialized on it.
        root.sceneProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                scene = newV;
                initializeHotkeys();
            }
        });
        
        initializeOutputLabel();
        initializeKeyGrid();
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
    
    private void checkIfAns() {
        if (isAns) {
            isAns = false;
            output.set("");
        }
    }
    
    private void initializeHotkeys() {
        try {
            scene.setOnKeyPressed(keyEvent -> {
                checkIfAns();
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
        } catch (NullPointerException ex) {
            System.out.println("Hotkeys not active.");
        }
        
    }
    
    @FXML
    private void keyClick(MouseEvent mouseEvent) {
        checkIfAns();
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
    
    private void addNumber(char c) {
        String current = output.get();
        if (current.isEmpty() || current.equals("E")) output.set(String.valueOf(c));
        else {
            String regexCheckZero = ".*\\.[0*]?$|.*[^0]$";
            
            // String must not end in 0 or has a decimal point right in front of ending String of zeroes if new input is
            // supposed to be 0, otherwise it gets ignored.
            
            char lastChar = current.charAt(current.length()-1);
            // TODO: Fix leading zero. Can only put 2 zeroes after decimal point.
            if (c != '0' || current.matches(regexCheckZero)) output.set(
                    lastChar == ')' ? current + '×' + c : current + c);
        }
    }
    
    private void addOpeningParenthesis() {
        String current = output.get();
        if (current.isEmpty() || current.equals("E")) output.set("(");
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
        if (current.isEmpty() || current.equals("E")) return;
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
        if (current.isEmpty() || current.equals("E")) output.set("(-");
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
        if (current.isEmpty() || current.equals("E")) return;
        
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
        if (current.isEmpty() || current.equals("E")) output.set("0.");
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
        if (current.isEmpty() || current.equals("E")) output.set("√");
        else {
            char lastChar = current.charAt(current.length() - 1);
            switch (lastChar) {
                case '.' -> output.set(current + '0' + '×' + '√');
                case '+', '×', '÷', '-', '(', '√' -> output.set(current + '√');
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')' -> output.set(current + '×' + '√');
            }
        }
    }
    
    private void equalsClick() {
        try {
            ans = service.evaluate(output.get(), ans);
            // Round the output to 10 characters after the decimal point
            String ansString = ans.scale() > 10
                    ? ans.setScale(10, RoundingMode.HALF_EVEN).toString() : ans.toString();
            isAns = true;
            output.set(ansString);
        } catch (ArithmeticException ex) {
            output.set("E");
            Toast.show(scene.getWindow(), "An arithmetic error occurred.");
        } catch (NumberFormatException ex) {
            output.set("E");
            Toast.show(scene.getWindow(), "A number got entered incorrectly.");
        } catch (TermSyntaxException ex) {
            output.set("E");
            Toast.show(scene.getWindow(), "A syntax error occurred.");
        }
    }
}
