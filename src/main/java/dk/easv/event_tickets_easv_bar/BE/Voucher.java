package dk.easv.event_tickets_easv_bar.BE;

public class Voucher {

    private int id;
    private String code;
    private double discount;

    public Voucher(int id, String code, double discount) {
        this.id = id;
        this.code = code;
        this.discount = discount;
    }

    public int getId() { return id; }
}