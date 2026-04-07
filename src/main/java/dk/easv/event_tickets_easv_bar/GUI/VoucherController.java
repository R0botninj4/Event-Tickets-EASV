package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Voucher;
import dk.easv.event_tickets_easv_bar.BLL.VoucherManager;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class VoucherController implements ClosableWindow {

    // ===== CLOSE =====
    @FXML private Button closeButton;

    // ===== ALL VOUCHER INPUT =====
    @FXML private TextField emailAllField;

    @FXML private TextField codeField;
    @FXML private TextField discountField;
    @FXML private DatePicker validUntilPicker;
    @FXML private ComboBox<String> typeCombo;



    // ===== SEARCH =====
    @FXML private TextField searchField;

    // ===== TABLE =====
    @FXML private TableView<Voucher> voucherTable;

    @FXML private TableColumn<Voucher, String> voucherCodeColumn;
    @FXML private TableColumn<Voucher, String> codeColumn;
    @FXML private TableColumn<Voucher, String> typeColumn;
    @FXML private TableColumn<Voucher, String> discountColumn;
    @FXML private TableColumn<Voucher, String> validColumn;

    // ===== BUTTONS =====
    @FXML private Button sendButton;
    @FXML private Button printButton;

    private final VoucherManager voucherManager = new VoucherManager();

    private ObservableList<Voucher> masterData = FXCollections.observableArrayList();

    // ================= INIT =================
    @FXML
    public void initialize() {
        setupCombos();
        setupTable();
        loadVouchers();
        setupSearch();
    }

    private void setupCombos() {

        ObservableList<String> types = FXCollections.observableArrayList(
                "Percentage",
                "Fixed",
                "Free Entry"
        );

        typeCombo.setItems(types);

    }

    private void setupTable() {

        codeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCode()));

        typeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getType()));

        discountColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDiscount()));

        validColumn.setCellValueFactory(data -> {
            LocalDate date = data.getValue().getValidUntil();
            return new SimpleStringProperty(date != null ? date.toString() : "Never");
        });
    }

    private void loadVouchers() {

        masterData = FXCollections.observableArrayList(
                voucherManager.getAllVouchers()
        );

        voucherTable.setItems(masterData);
    }

    // ================= SEARCH =================
    private void setupSearch() {

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal == null || newVal.isBlank()) {
                voucherTable.setItems(masterData);
                return;
            }

            ObservableList<Voucher> filtered = FXCollections.observableArrayList(
                    masterData.stream()
                            .filter(v ->
                                    v.getCode().toLowerCase().contains(newVal.toLowerCase()) ||
                                            v.getType().toLowerCase().contains(newVal.toLowerCase()) ||
                                            v.getDiscount().toLowerCase().contains(newVal.toLowerCase())
                            )
                            .toList()
            );

            voucherTable.setItems(filtered);
        });
    }

    // ================= SEND =================
    @FXML
    private void handleSend(ActionEvent event) {

        String code = codeField.getText();
        String type = typeCombo.getValue();
        String discount = discountField.getText();   // ✅ STRING NOW
        LocalDate validUntil = validUntilPicker.getValue();

        String email = emailAllField.getText();

        if (code.isBlank() || type == null || discount.isBlank()) {
            showAlert("Error", "Fill code, type and discount");
            return;
        }

        Voucher voucher = new Voucher(
                0,
                code,
                type,
                discount,
                validUntil
        );

        voucherManager.createVoucher(voucher);

        clearAllFields();
        loadVouchers();

        showAlert("Success", "Voucher created!");
    }

    // ================= PRINT =================
    @FXML
    private void handlePrint(ActionEvent event) {

        Voucher selected = voucherTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Select a voucher first");
            return;
        }

        showAlert("Print", """
                VOUCHER
                Code: %s
                Type: %s
                Discount: %s
                Valid Until: %s
                """.formatted(
                selected.getCode(),
                selected.getType(),
                selected.getDiscount(),
                selected.getValidUntil()
        ));
    }

    // ================= CLEAR =================
    private void clearAllFields() {

        codeField.clear();
        discountField.clear();
        typeCombo.getSelectionModel().clearSelection();
        validUntilPicker.setValue(null);
        emailAllField.clear();
    }

    // ================= CLOSE =================
    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void closeWindow(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}