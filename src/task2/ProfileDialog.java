package task2;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Модальне діалогове вікно з підсумковою карткою-профілем заявки.
 */
public class ProfileDialog extends JDialog {

    public ProfileDialog(JFrame owner, CardApplication app) {
        super(owner, "Заявка №" + app.number + " — картка-профіль", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 8));

        JPanel cardHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cardHolder.setBorder(BorderFactory.createEmptyBorder(14, 14, 0, 14));
        cardHolder.add(new CardPicture(app));
        add(cardHolder, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridBagLayout());
        info.setBorder(BorderFactory.createEmptyBorder(4, 22, 4, 22));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 4, 3, 10);
        c.anchor = GridBagConstraints.WEST;
        String[][] rows = {
                {"Номер заявки:", String.valueOf(app.number)},
                {"Дата подання:", app.createdText()},
                {"ПІБ:", app.fullName},
                {"Телефон:", app.formattedPhone()},
                {"Тип картки:", app.cardType},
                {"Валюта рахунку:", app.currency},
                {"Додаткові послуги:", app.services()},
                {"Вартість послуг:", app.monthlyFee() + " грн/міс"},
                {"Статус:", "прийнято до розгляду"},
        };
        for (int i = 0; i < rows.length; i++) {
            c.gridy = i;
            c.gridx = 0;
            JLabel key = new JLabel(rows[i][0]);
            key.setForeground(Color.DARK_GRAY);
            info.add(key, c);
            c.gridx = 1;
            JLabel value = new JLabel(rows[i][1]);
            value.setFont(value.getFont().deriveFont(Font.BOLD));
            info.add(value, c);
        }
        add(info, BorderLayout.CENTER);

        JButton ok = new JButton("OK");
        ok.setName("ok");
        ok.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(ok);
        add(south, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(ok);

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    /** Зображення банківської картки, намальоване в paintComponent(). */
    private static class CardPicture extends JPanel {
        private final CardApplication app;

        CardPicture(CardApplication app) {
            this.app = app;
            setPreferredSize(new Dimension(340, 200));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth() - 4, h = getHeight() - 4;

            boolean credit = app.cardType.equals("Кредитна");
            Color c1 = credit ? new Color(120, 20, 60) : new Color(10, 60, 130);
            Color c2 = credit ? new Color(220, 90, 60) : new Color(40, 150, 200);
            g2.setPaint(new GradientPaint(0, 0, c1, w, h, c2));
            g2.fill(new RoundRectangle2D.Double(2, 2, w, h, 24, 24));

            // чип
            g2.setColor(new Color(230, 195, 100));
            g2.fillRoundRect(28, 62, 46, 34, 8, 8);
            g2.setColor(new Color(170, 135, 60));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(28, 79, 74, 79);
            g2.drawLine(51, 62, 51, 96);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            g2.drawString(app.cardType.toUpperCase() + " КАРТКА", 26, 38);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
            g2.drawString(app.currency, w - 64, 38);

            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            g2.drawString("**** **** **** ****", 26, 132);

            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
            g2.drawString(app.fullName.toUpperCase(), 26, 166);
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            g2.drawString("Заявка №" + app.number, 26, 186);
            g2.dispose();
        }
    }
}
