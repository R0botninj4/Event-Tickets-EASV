package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Voucher;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    private final DBConnector dbConnector;

    public VoucherDAO() {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Voucher> getAllVouchers() {

        List<Voucher> vouchers = new ArrayList<>();

        String sql = "SELECT * FROM Vouchers";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                LocalDate validUntil = rs.getDate("ValidUntil") != null
                        ? rs.getDate("ValidUntil").toLocalDate()
                        : null;

                vouchers.add(new Voucher(
                        rs.getInt("VoucherID"),
                        rs.getString("VoucherCode"),
                        rs.getString("VoucherType"),
                        rs.getString("Discount"),
                        validUntil
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vouchers;
    }

    public Voucher getVoucher(String code) {

        String sql = "SELECT * FROM Vouchers WHERE VoucherCode = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                LocalDate validUntil = rs.getDate("ValidUntil") != null
                        ? rs.getDate("ValidUntil").toLocalDate()
                        : null;

                return new Voucher(
                        rs.getInt("VoucherID"),
                        rs.getString("VoucherCode"),
                        rs.getString("VoucherType"),
                        rs.getString("Discount"),
                        validUntil
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createVoucher(Voucher voucher) {

        String sql = """
                INSERT INTO Vouchers (VoucherCode, VoucherType, Discount, ValidUntil)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voucher.getCode());
            stmt.setString(2, voucher.getType());
            stmt.setString(3, voucher.getDiscount());

            if (voucher.getValidUntil() != null) {
                stmt.setDate(4, Date.valueOf(voucher.getValidUntil()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}