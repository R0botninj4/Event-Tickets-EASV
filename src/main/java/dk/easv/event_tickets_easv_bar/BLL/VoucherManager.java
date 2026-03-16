package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.Voucher;
import dk.easv.event_tickets_easv_bar.BLL.Interface.IVoucherManager;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IVoucherDAO;
import dk.easv.event_tickets_easv_bar.DAL.VoucherDAO;

public class VoucherManager implements IVoucherManager {

    private final IVoucherDAO voucherDAO;

    public VoucherManager() {
        voucherDAO = new VoucherDAO();
    }

    @Override
    public Voucher getVoucher(String code) {
        return voucherDAO.getVoucher(code);
    }
}