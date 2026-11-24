import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import task1.NumberConverterFrame;
import task2.CardApplicationFrame;

/**
 * Головне вікно лабораторної роботи №6 — запуск завдань.
 */
public class Menu extends JFrame {

    public Menu() {
        super("Лабораторна робота №6");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 10));

        JLabel title = new JLabel("Графічний інтерфейс (Swing)", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(16f));
        title.setBorder(BorderFactory.createEmptyBorder(14, 20, 0, 20));
        add(title, BorderLayout.NORTH);

        JButton task1 = new JButton("Завдання 1 (варіант 8): конвертер систем числення");
        JButton task2 = new JButton("Завдання 2 (варіант 8): заявка на банківську картку");
        JButton about = new JButton("Про програму");
        JButton exit = new JButton("Вихід");
        task1.addActionListener(e -> new NumberConverterFrame().setVisible(true));
        task2.addActionListener(e -> new CardApplicationFrame().setVisible(true));
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Лабораторна робота №6\nПобудова додатків з графічним інтерфейсом\n\n"
                        + "Виконав: Демчук Павло, група 241б", "Про програму",
                JOptionPane.INFORMATION_MESSAGE));
        exit.addActionListener(e -> System.exit(0));

        JPanel buttons = new JPanel(new GridLayout(0, 1, 0, 8));
        buttons.setBorder(BorderFactory.createEmptyBorder(6, 20, 20, 20));
        buttons.add(task1);
        buttons.add(task2);
        buttons.add(about);
        buttons.add(exit);
        add(buttons, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // залишаємо стандартний вигляд
            }
            new Menu().setVisible(true);
        });
    }
}
