package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.Voucher;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IVoucherDAO;
import dk.easv.event_tickets_easv_bar.DAL.VoucherDAO;

public class VoucherManager {

    private final IVoucherDAO voucherDAO;

    public VoucherManager() {
        voucherDAO = new VoucherDAO();
    }

    public Voucher getVoucher(String code) {
        return voucherDAO.getVoucher(code);
    }

    public boolean isVoucherValid(String code) {
        Voucher voucher = getVoucher(code);
        return voucher != null && voucher.isValid();
    }
}