package me.cdh;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.regex.Pattern;

public final class Main {
    public static final String FONT = "Hack Nerd Font";
    public static final String NUMBER_REGEX = "(-?\\d+[.]\\d*)|(\\d+)|(-\\d+)";
    public static final int WINDOW_WIDTH = 410;
    public static final int WINDOW_HEIGHT = 600;
    public static final int BUTTON_WIDTH = 80;
    public static final int BUTTON_HEIGHT = 70;
    public static final int MARGIN_X = 20;
    public static final int MARGIN_Y = 60;

    private final JFrame window;
    private final JTextField inputScreen;
    private JButton btnRoot;
    private JButton btnPower;
    private JButton btnLog;

    private char selectedOperator = ' ';
    private boolean go = true;
    private boolean addToDisplay = true;
    private double typedValue = 0;

    private final int[] columns = { MARGIN_X, MARGIN_X + 90, MARGIN_X + 90 * 2, MARGIN_X + 90 * 3, MARGIN_X + 90 * 4 };
    private final int[] rows = { MARGIN_Y, MARGIN_Y + 100, MARGIN_Y + 100 + 80, MARGIN_Y + 100 + 80 * 2,
            MARGIN_Y + 100 + 80 * 3, MARGIN_Y + 100 + 80 * 4 };

    {
        FlatMacDarkLaf.setup();
        window = new JFrame("Jaculator") {
            {
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);
                setResizable(false);
                setVisible(true);
            }
        };
        inputScreen = new JTextField();
    }

    private double calculate(double fNum, double sNum, char operator) {
        return switch (operator) {
            case '+' -> fNum + sNum;
            case '-' -> fNum - sNum;
            case '*' -> fNum * sNum;
            case '/' -> fNum / sNum;
            case '%' -> fNum % sNum;
            case '^' -> Math.pow(fNum, sNum);
            default -> sNum;
        };
    }

    private void initInputScreen(int[] columns, int[] rows) {
        inputScreen.setBounds(columns[0], rows[0], 350, 70);
        inputScreen.setEditable(false);
        inputScreen.setFont(new Font(FONT, Font.PLAIN, 33));
        window.add(inputScreen);
    }

    private JButton createBtn(String label, int x, int y) {
        return new JButton(label) {
            {
                setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                setFont(new Font("Hack Nerd Font", Font.PLAIN, 28));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setFocusable(false);
                window.add(this);
            }
        };
    }

    private void operatorBtnRegistration(char c) {
        if (!Pattern.matches(NUMBER_REGEX, inputScreen.getText()))
            return;
        if (go) {
            typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
            if (Pattern.matches("-?\\d+[.]0", String.valueOf(typedValue))) {
                inputScreen.setText(String.valueOf(((int) typedValue)));
            } else {
                inputScreen.setText(String.valueOf(typedValue));
            }
            selectedOperator = c;
            go = false;
            addToDisplay = false;

        } else
            selectedOperator = c;
    }

    private void resultBtnRegistration(char c) {
        if (!Pattern.matches(NUMBER_REGEX, inputScreen.getText()))
            return;
        if (go) {
            typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
            if (Pattern.matches("-?\\d+[.]0", String.valueOf(typedValue))) {
                inputScreen.setText(String.valueOf(((int) typedValue)));
            } else {
                inputScreen.setText(String.valueOf(typedValue));
            }
            selectedOperator = c;
            addToDisplay = false;
        }
    }

    private void numBtnRegistration(int n) {
        if (addToDisplay) {
            if (Pattern.matches("0*", inputScreen.getText())) {
                inputScreen.setText(String.valueOf(n));
            } else {
                inputScreen.setText(inputScreen.getText() + n);
            }
        } else {
            inputScreen.setText(String.valueOf(n));
            addToDisplay = true;
        }
        go = true;
    }

    private void initBtn(int[] columns, int[] rows) {
        var btnC = createBtn("C", columns[0], rows[1]);
        btnC.addActionListener(_ -> {
            inputScreen.setText("0");
            selectedOperator = ' ';
            typedValue = 0;
        });
        var btnBack = createBtn("<-", columns[1], rows[1]);
        btnBack.addActionListener(_ -> {
            var str = inputScreen.getText();
            var sb = new StringBuilder();
            for (int i = 0; i < str.length() - 1; i++) {
                sb.append(str.charAt(i));
            }
            if (sb.toString().isEmpty()) {
                inputScreen.setText("0");
            } else {
                inputScreen.setText(sb.toString());
            }
        });
        var btnMod = createBtn("%", columns[2], rows[1]);
        btnMod.addActionListener(_ -> {
            if (!Pattern.matches(NUMBER_REGEX, inputScreen.getText()) || !go)
                return;
            typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
            if (Pattern.matches("-?\\d+[.]0", String.valueOf(typedValue)))
                inputScreen.setText(String.valueOf(((int) typedValue)));
            else
                inputScreen.setText(String.valueOf(typedValue));
            selectedOperator = '%';
            go = false;
            addToDisplay = false;
        });
        var btnDiv = createBtn("/", columns[3], rows[1]);
        btnDiv.addActionListener(_ -> operatorBtnRegistration('/'));
        var btnSeven = createBtn("7", columns[0], rows[2]);
        btnSeven.addActionListener(_ -> numBtnRegistration(7));
        var btnEight = createBtn("8", columns[1], rows[2]);
        btnEight.addActionListener(_ -> numBtnRegistration(8));
        var btnNine = createBtn("9", columns[2], rows[2]);
        btnNine.addActionListener(_ -> numBtnRegistration(9));
        var btnMul = createBtn("*", columns[3], rows[2]);
        btnMul.addActionListener(_ -> operatorBtnRegistration('*'));
        var btnFour = createBtn("4", columns[0], rows[3]);
        btnFour.addActionListener(_ -> numBtnRegistration(4));
        var btnFive = createBtn("5", columns[1], rows[3]);
        btnFive.addActionListener(_ -> numBtnRegistration(5));
        var btnSix = createBtn("6", columns[2], rows[3]);
        btnSix.addActionListener(_ -> numBtnRegistration(6));
        var btnSub = createBtn("-", columns[3], rows[3]);
        btnSub.addActionListener(_ -> operatorBtnRegistration('-'));
        var btnOne = createBtn("1", columns[0], rows[4]);
        btnOne.addActionListener(_ -> numBtnRegistration(1));
        var btnTwo = createBtn("2", columns[1], rows[4]);
        btnTwo.addActionListener(_ -> numBtnRegistration(2));
        var btnThree = createBtn("3", columns[2], rows[4]);
        btnThree.addActionListener(_ -> numBtnRegistration(3));
        var btnAdd = createBtn("+", columns[3], rows[4]);
        btnAdd.addChangeListener(_ -> operatorBtnRegistration('+'));
        var btnPoint = createBtn(".", columns[0], rows[5]);
        btnPoint.addChangeListener(_ -> {
            if (addToDisplay) {
                if (!inputScreen.getText().contains(".")) {
                    inputScreen.setText(inputScreen.getText() + ".");
                }
            } else {
                inputScreen.setText("0.");
                addToDisplay = true;
            }
            go = true;
        });
        var btnZero = createBtn("0", columns[1], rows[5]);
        btnZero.addActionListener(_ -> numBtnRegistration(0));
        var btnEqual = createBtn("=", columns[2], rows[5]);
        btnEqual.addActionListener(_ -> resultBtnRegistration('='));
        btnEqual.setSize(2 * BUTTON_WIDTH + 10, BUTTON_HEIGHT);
        btnRoot = createBtn("√", columns[4], rows[1]);
        btnRoot.addActionListener(_ -> resultBtnRegistration('√'));
        btnRoot.setVisible(false);
        btnPower = createBtn("pow", columns[4], rows[2]);
        btnPower.addActionListener(_ -> resultBtnRegistration('^'));
        btnPower.setVisible(false);
        btnLog = createBtn("ln", columns[4], rows[3]);
        btnLog.addChangeListener(_ -> resultBtnRegistration('l'));
        btnLog.setVisible(false);
    }

    private JComboBox<String> createComboBox(String[] items) {
        return new JComboBox<>(items) {
            {
                setBounds(20, 30, 140, 25);
                setToolTipText("Calculator type");
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                window.add(this);
            }
        };
    }

    private void initCalculatorTypeSelector() {
        var comboBox = createComboBox(new String[] { "Standard", "Scientific" });
        comboBox.addItemListener(e -> {
            if (e.getStateChange() != ItemEvent.SELECTED)
                return;
            var item = (String) e.getItem();
            switch (item) {
                case "Standard" -> {
                    window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                    btnRoot.setVisible(false);
                    btnPower.setVisible(false);
                    btnLog.setVisible(false);
                }
                case "Scientific" -> {
                    window.setSize(WINDOW_WIDTH + 80, WINDOW_HEIGHT);
                    btnRoot.setVisible(true);
                    btnPower.setVisible(true);
                    btnLog.setVisible(true);
                }
            }
        });
    }

    private void init() {
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);

        initInputScreen(columns, rows);
        initBtn(columns, rows);
        initCalculatorTypeSelector();

        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main()::init);
    }
}
