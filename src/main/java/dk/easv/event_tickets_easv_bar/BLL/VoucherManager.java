package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.Voucher;
import dk.easv.event_tickets_easv_bar.DAL.VoucherDAO;

import java.util.List;

public class VoucherManager {

    private final VoucherDAO voucherDAO = new VoucherDAO();

    public List<Voucher> getAllVouchers() {
        return voucherDAO.getAllVouchers();
    }

    public Voucher getVoucher(String code) {
        return voucherDAO.getVoucher(code);
    }

    public void createVoucher(Voucher voucher) {
        voucherDAO.createVoucher(voucher);
    }
}