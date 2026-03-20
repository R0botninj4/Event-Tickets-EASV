package dk.easv.event_tickets_easv_bar.DAL.Interface;

import dk.easv.event_tickets_easv_bar.BE.Voucher;

public interface IVoucherDAO {
    Voucher getVoucher(String code);
}