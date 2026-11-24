package task2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Завдання 2, варіант 8. Заявка на оформлення банківської картки.
 * Компоненти: JTextField (ПІБ, телефон), JRadioButton (тип картки: Дебетова / Кредитна),
 * JComboBox (валюта: UAH, USD, EUR), JCheckBox (SMS-інформування, страховка).
 * Кнопка «Надіслати заявку» перевіряє заповнення обов'язкових полів і відображає
 * підсумкову картку-профіль у JDialog. Кнопка «Скинути» очищає форму.
 */
public class CardApplicationFrame extends JFrame {

    /** Щомісячна вартість додаткових послуг, грн. */
    static final int SMS_PRICE = 25;
    static final int INSURANCE_PRICE = 60;

    private final JTextField nameField = new JTextField(24);
    private final JTextField phoneField = new JTextField(24);
    private final JRadioButton debitRadio = new JRadioButton("Дебетова");
    private final JRadioButton creditRadio = new JRadioButton("Кредитна");
    private final ButtonGroup typeGroup = new ButtonGroup();
    private final JComboBox<String> currencyBox = new JComboBox<>(new String[]{"UAH", "USD", "EUR"});
    private final JCheckBox smsCheck = new JCheckBox("Підключити SMS-інформування (" + SMS_PRICE + " грн/міс)");
    private final JCheckBox insuranceCheck = new JCheckBox("Підключити страховку (" + INSURANCE_PRICE + " грн/міс)");
    private final JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JLabel statusLabel = new JLabel(" ");

    private final Border normalBorder = nameField.getBorder();
    private final Border errorBorder = BorderFactory.createLineBorder(new Color(200, 30, 30), 2);
    private int applicationNumber = 0;

    public CardApplicationFrame() {
        super("Заявка на оформлення банківської картки");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        nameField.setName("name");
        phoneField.setName("phone");
        debitRadio.setName("debit");
        creditRadio.setName("credit");
        currencyBox.setName("currency");
        smsCheck.setName("sms");
        insuranceCheck.setName("insurance");

        JLabel header = new JLabel("Заповніть заявку (поля з * — обов'язкові)");
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 4, 14));
        header.setFont(header.getFont().deriveFont(15f));
        add(header, BorderLayout.NORTH);

        // ---------- форма (GridBagLayout) ----------
        typeGroup.add(debitRadio);
        typeGroup.add(creditRadio);
        typePanel.add(debitRadio);
        typePanel.add(creditRadio);

        JPanel options = new JPanel(new GridLayout(0, 1));
        options.add(smsCheck);
        options.add(insuranceCheck);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 4, 5, 4);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        row = addRow(form, c, row, "ПІБ *:", nameField);
        row = addRow(form, c, row, "Телефон *:", phoneField);
        row = addRow(form, c, row, "Тип картки *:", typePanel);
        row = addRow(form, c, row, "Валюта рахунку:", currencyBox);
        addRow(form, c, row, "Додаткові послуги:", options);
        phoneField.setToolTipText("Формат: +380XXXXXXXXX або 0XXXXXXXXX");
        add(form, BorderLayout.CENTER);

        // ---------- кнопки ----------
        JButton submitButton = new JButton("Надіслати заявку");
        JButton resetButton = new JButton("Скинути");
        submitButton.setName("submit");
        resetButton.setName("reset");
        submitButton.addActionListener(e -> submit());
        resetButton.addActionListener(e -> reset());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(resetButton);
        buttons.add(submitButton);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 10));
        south.add(statusLabel, BorderLayout.CENTER);
        south.add(buttons, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(submitButton);

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private static int addRow(JPanel form, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridy = row;
        c.gridx = 0; c.weightx = 0;
        c.anchor = field instanceof JPanel ? GridBagConstraints.NORTHWEST : GridBagConstraints.WEST;
        form.add(new JLabel(label), c);
        c.gridx = 1; c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        form.add(field, c);
        return row + 1;
    }

    // ---------- перевірка даних ----------

    /** ПІБ: щонайменше два слова з літер (дозволені апостроф і дефіс). */
    static boolean isValidName(String name) {
        return name.matches("[\\p{L}'’\\-]{2,}(\\s+[\\p{L}'’\\-]{2,}){1,2}");
    }

    /** Нормалізує телефон до формату +380XXXXXXXXX або повертає null, якщо формат хибний. */
    static String normalizePhone(String phone) {
        String digits = phone.replaceAll("[\\s()\\-]", "");
        if (digits.matches("\\+380\\d{9}")) {
            return digits;
        }
        if (digits.matches("380\\d{9}")) {
            return "+" + digits;
        }
        if (digits.matches("0\\d{9}")) {
            return "+38" + digits;
        }
        return null;
    }

    private void submit() {
        List<String> errors = new ArrayList<>();
        String name = nameField.getText().trim().replaceAll("\\s+", " ");
        String phone = normalizePhone(phoneField.getText());

        mark(nameField, true);
        mark(phoneField, true);
        typePanel.setBorder(null);

        if (name.isEmpty()) {
            errors.add("Не заповнено поле «ПІБ».");
            mark(nameField, false);
        } else if (!isValidName(name)) {
            errors.add("ПІБ має складатися з 2–3 слів, що містять лише літери.");
            mark(nameField, false);
        }
        if (phoneField.getText().isBlank()) {
            errors.add("Не заповнено поле «Телефон».");
            mark(phoneField, false);
        } else if (phone == null) {
            errors.add("Телефон має бути у форматі +380XXXXXXXXX або 0XXXXXXXXX.");
            mark(phoneField, false);
        }
        if (typeGroup.getSelection() == null) {
            errors.add("Не обрано тип картки.");
            typePanel.setBorder(errorBorder);
        }

        if (!errors.isEmpty()) {
            statusLabel.setForeground(new Color(200, 30, 30));
            statusLabel.setText("Помилок: " + errors.size());
            JOptionPane.showMessageDialog(this, String.join("\n", errors),
                    "Заявку не надіслано", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CardApplication app = new CardApplication(++applicationNumber, name, phone,
                debitRadio.isSelected() ? "Дебетова" : "Кредитна",
                (String) currencyBox.getSelectedItem(),
                smsCheck.isSelected(), insuranceCheck.isSelected());
        statusLabel.setForeground(new Color(0, 120, 60));
        statusLabel.setText("Заявку №" + app.number + " надіслано");
        new ProfileDialog(this, app).setVisible(true);
    }

    private void mark(JTextField field, boolean ok) {
        field.setBorder(ok ? normalBorder : BorderFactory.createCompoundBorder(errorBorder, normalBorder));
    }

    /** Скидає всі поля форми до початкового стану. */
    void reset() {
        nameField.setText("");
        phoneField.setText("");
        typeGroup.clearSelection();
        currencyBox.setSelectedIndex(0);
        smsCheck.setSelected(false);
        insuranceCheck.setSelected(false);
        mark(nameField, true);
        mark(phoneField, true);
        typePanel.setBorder(null);
        statusLabel.setText(" ");
        nameField.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // залишаємо стандартний вигляд
            }
            CardApplicationFrame frame = new CardApplicationFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
