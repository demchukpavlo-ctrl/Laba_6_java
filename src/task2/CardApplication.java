package task2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Дані однієї заявки на оформлення картки.
 */
public class CardApplication {
    final int number;
    final String fullName;
    final String phone;
    final String cardType;
    final String currency;
    final boolean sms;
    final boolean insurance;
    final LocalDateTime created = LocalDateTime.now();

    CardApplication(int number, String fullName, String phone, String cardType,
                    String currency, boolean sms, boolean insurance) {
        this.number = number;
        this.fullName = fullName;
        this.phone = phone;
        this.cardType = cardType;
        this.currency = currency;
        this.sms = sms;
        this.insurance = insurance;
    }

    /** Щомісячна вартість обраних послуг, грн. */
    int monthlyFee() {
        return (sms ? CardApplicationFrame.SMS_PRICE : 0)
                + (insurance ? CardApplicationFrame.INSURANCE_PRICE : 0);
    }

    String services() {
        if (!sms && !insurance) {
            return "не обрано";
        }
        if (sms && insurance) {
            return "SMS-інформування, страховка";
        }
        return sms ? "SMS-інформування" : "страховка";
    }

    /** Телефон у вигляді +380 XX XXX XX XX. */
    String formattedPhone() {
        return phone.substring(0, 4) + " " + phone.substring(4, 6) + " " + phone.substring(6, 9)
                + " " + phone.substring(9, 11) + " " + phone.substring(11);
    }

    String createdText() {
        return created.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}
