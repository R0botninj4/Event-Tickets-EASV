package dk.easv.event_tickets_easv_bar.BE;

import java.time.LocalDate;

public class Voucher {

    private int id;
    private String code;
    private String type;
    private String discount;
    private LocalDate validUntil;

    public Voucher(int id, String code, String type, String discount, LocalDate validUntil) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.discount = discount;
        this.validUntil = validUntil;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getDiscount() {
        return discount;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public boolean isValid() {
        return validUntil == null || !LocalDate.now().isAfter(validUntil);
    }

    @Override
    public String toString() {
        return code + " (" + type + ", " + discount + "%)";
    }
}