package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.Ticket;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.MultiFormatWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicketViewController {
    @FXML private ImageView barcodeImage;
    @FXML private Label lblEventName;
    @FXML private Label lblStartDate;
    @FXML private Label lblEndDate;
    @FXML private Label lblLocation;
    @FXML private Label lblTicketId;
    @FXML private Label lblBarcode;

    public void setTicket(Ticket ticket, Event event) {

        // Event navn
        lblEventName.setText(event.getName());

        // START DATE
        lblStartDate.setText(
                event.getDate() != null ? event.getDate().toString() : ""
        );

        // END DATE
        lblEndDate.setText(
                event.getEndDate() != null ? event.getEndDate().toString() : ""
        );

        // LOCATION
        lblLocation.setText(
                event.getLocation() != null ? event.getLocation() : ""
        );

        // TICKET ID
        lblTicketId.setText(String.valueOf(ticket.getId()));

        // 🔹 BARCODE TEXT
        String barcodeText = ticket.getBarcode() != null ? ticket.getBarcode() : "";
        lblBarcode.setText(barcodeText);

        //  HER ER DET DU MANGLER
        if (!barcodeText.isEmpty()) {
            barcodeImage.setImage(generateBarcode(barcodeText));
        }
    }

    private Image generateBarcode(String text) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.CODE_128, // perfekt til tickets
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