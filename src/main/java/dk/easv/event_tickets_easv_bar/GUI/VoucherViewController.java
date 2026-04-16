package dk.easv.event_tickets_easv_bar.GUI;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import dk.easv.event_tickets_easv_bar.BE.Voucher;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class VoucherViewController {

    @FXML private Label lblType;
    @FXML private Label lblInfo;
    @FXML private Label lblValidUntil;
    @FXML private Label lblUsage;
    @FXML private Label lblCode;
    @FXML private Label lblBarcode;
    @FXML private ImageView barcodeImage;

    public void setVoucher(Voucher voucher) {

        lblType.setText(voucher.getType());
        lblInfo.setText("Discount: " + voucher.getDiscount());

        lblValidUntil.setText(
                voucher.getValidUntil() != null
                        ? voucher.getValidUntil().toString()
                        : "No expiry"
        );

        lblUsage.setText("All Events");

        lblCode.setText(voucher.getCode());
        lblBarcode.setText(voucher.getCode());

        if (voucher.getCode() != null && !voucher.getCode().isEmpty()) {
            barcodeImage.setImage(generateBarcode(voucher.getCode()));
        }
    }

    private Image generateBarcode(String text) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.CODE_128,
                    300,
                    100
            );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);

            return new Image(new ByteArrayInputStream(out.toByteArray()));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}