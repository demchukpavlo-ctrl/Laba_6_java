package task1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Завдання 1, варіант 8. Конвертер систем числення.
 * Поле для введення числа, два JComboBox (з якої та в яку систему: десяткова,
 * двійкова, вісімкова, шістнадцяткова), кнопка «Перетворити».
 * Переведення виконується стандартними методами Java (Long.parseLong(text, radix)),
 * NumberFormatException обробляється з виведенням повідомлення JOptionPane.
 */
public class NumberConverterFrame extends JFrame {

    /** Система числення: назва для списку та основа. */
    enum NumeralSystem {
        DEC("Десяткова (10)", 10, "0-9"),
        BIN("Двійкова (2)", 2, "0, 1"),
        OCT("Вісімкова (8)", 8, "0-7"),
        HEX("Шістнадцяткова (16)", 16, "0-9, A-F");

        final String title;
        final int radix;
        final String digits;

        NumeralSystem(String title, int radix, String digits) {
            this.title = title;
            this.radix = radix;
            this.digits = digits;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    private final JTextField inputField = new JTextField(26);
    private final JComboBox<NumeralSystem> fromBox = new JComboBox<>(NumeralSystem.values());
    private final JComboBox<NumeralSystem> toBox = new JComboBox<>(NumeralSystem.values());
    private final JButton convertButton = new JButton("Перетворити");
    private final JButton swapButton = new JButton("Обміняти");
    private final JLabel resultLabel = new JLabel("Результат: —");

    public NumberConverterFrame() {
        super("Конвертер систем числення");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inputField.setName("input");
        fromBox.setName("from");
        toBox.setName("to");
        convertButton.setName("convert");
        resultLabel.setName("result");
        toBox.setSelectedItem(NumeralSystem.BIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Число:"), c);
        c.gridx = 1; c.gridwidth = 2; c.weightx = 1;
        form.add(inputField, c);

        c.gridwidth = 1; c.weightx = 0;
        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("З системи:"), c);
        c.gridx = 1; c.weightx = 1;
        form.add(fromBox, c);
        c.gridx = 2; c.gridheight = 2; c.weightx = 0; c.fill = GridBagConstraints.VERTICAL;
        swapButton.setToolTipText("Поміняти системи місцями");
        form.add(swapButton, c);

        c.gridheight = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("У систему:"), c);
        c.gridx = 1; c.weightx = 1;
        form.add(toBox, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 3; c.weightx = 1;
        c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.CENTER;
        form.add(convertButton, c);

        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 16f));
        resultLabel.setForeground(new Color(0, 90, 160));
        resultLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 16, 14, 16)));

        add(form, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        convertButton.addActionListener(e -> convert());
        swapButton.addActionListener(e -> {
            Object from = fromBox.getSelectedItem();
            fromBox.setSelectedItem(toBox.getSelectedItem());
            toBox.setSelectedItem(from);
        });
        getRootPane().setDefaultButton(convertButton);   // Enter = «Перетворити»

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    /** Виконує переведення числа з однієї системи числення в іншу. */
    private void convert() {
        NumeralSystem from = (NumeralSystem) fromBox.getSelectedItem();
        NumeralSystem to = (NumeralSystem) toBox.getSelectedItem();
        String text = inputField.getText().trim().replace(" ", "");

        if (text.isEmpty()) {
            showError("Введіть число для перетворення.");
            return;
        }
        try {
            long value = Long.parseLong(text, from.radix);
            String result = Long.toString(value, to.radix).toUpperCase();
            resultLabel.setText("Результат: " + result + " (" + to.radix + ")");
        } catch (NumberFormatException ex) {
            resultLabel.setText("Результат: —");
            showError("Рядок «" + text + "» не є коректним числом у системі «" + from.title + "».\n"
                    + "Допустимі цифри: " + from.digits + " (можливий знак «-»),\n"
                    + "значення має вміщатися в тип long (64 біти).");
            inputField.requestFocus();
            inputField.selectAll();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Помилка введення", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // залишаємо стандартний вигляд
            }
            NumberConverterFrame frame = new NumberConverterFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
