package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Voucher;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IVoucherDAO;

import java.io.IOException;
import java.sql.*;

public class VoucherDAO implements IVoucherDAO {

    private DBConnector dbConnector;

    public VoucherDAO(){
        try{
            dbConnector = new DBConnector();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Voucher getVoucher(String code){

        String sql = "SELECT * FROM Vouchers WHERE VoucherCode=?";

        try(Connection conn = dbConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1,code);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return new Voucher(
                        rs.getInt("VoucherID"),
                        rs.getString("VoucherCode"),
                        rs.getDouble("Discount")
                );
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }
}