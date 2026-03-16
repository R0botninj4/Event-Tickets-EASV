package dk.easv.event_tickets_easv_bar.BLL.Interface;

import dk.easv.event_tickets_easv_bar.BE.Voucher;

public interface IVoucherManager {

    Voucher getVoucher(String code);

}