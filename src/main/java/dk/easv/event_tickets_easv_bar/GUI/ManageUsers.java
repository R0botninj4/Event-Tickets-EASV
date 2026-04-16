package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.UserManager;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import dk.easv.event_tickets_easv_bar.GUI.Login.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageUsers implements ClosableWindow {

    private final UserManager userManager = new UserManager();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private User selectedUser;

    // TABLE
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colPhone;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;

    // INPUTS
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboRole;
    @FXML private TextField txtSearch;
    @FXML private Button closeButton;

    @FXML
    public void initialize() {

        tableUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        enableEscClose(closeButton);

        comboRole.setItems(FXCollections.observableArrayList(
                "Admin", "Coordinator", "Customer"
        ));

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleText"));

        // SELECT USER
        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedUser = newSel;

                txtUsername.setText(newSel.getUsername());
                txtName.setText(newSel.getName());
                txtEmail.setText(newSel.getEmail());
                txtPhone.setText(newSel.getPhoneNumber());
                comboRole.setValue(newSel.getRoleText());

                txtPassword.clear(); // never show password
            }
        });

        // SEARCH
        FilteredList<User> filtered = new FilteredList<>(users, p -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filtered.setPredicate(user -> {

                if (newVal == null || newVal.isBlank()) return true;

                String s = newVal.toLowerCase();

                return user.getUsername().toLowerCase().contains(s)
                        || user.getName().toLowerCase().contains(s)
                        || (user.getEmail() != null && user.getEmail().toLowerCase().contains(s))
                        || (user.getPhoneNumber() != null && user.getPhoneNumber().toLowerCase().contains(s))
                        || user.getRoleText().toLowerCase().contains(s);
            });
        });

        SortedList<User> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableUsers.comparatorProperty());
        tableUsers.setItems(sorted);

        loadUsers();
    }

    // LOAD USERS
    private void loadUsers() {
        users.setAll(userManager.getAllUsers());
    }

    // ➕ NEW USER MODE
    @FXML
    private void addUser() {
        clearFields();
    }

    // SAVE (CREATE + UPDATE)
    @FXML
    private void updateUser() {

        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String roleStr = comboRole.getValue();

        if (username.isBlank() || name.isBlank() || roleStr == null) {
            showAlert("Error", "Username, name and role are required");
            return;
        }

        int roleInt = switch (roleStr) {
            case "Admin" -> 1;
            case "Coordinator" -> 2;
            case "Customer" -> 3;
            case "GOD"-> 4;
            default -> 3;
        };

        // CREATE
        if (selectedUser == null) {

            if (password.isBlank()) {
                showAlert("Error", "Password required for new user");
                return;
            }

            int id = userManager.addUser(username, password, name, email, phone, roleInt);

            if (id != -1) {
                showAlert("Success", "User created (password hashed with Argon2)");
            } else {
                showAlert("Error", "Failed to create user");
            }

        }
        // UPDATE
        else {
            selectedUser.setUsername(username);
            selectedUser.setName(name);
            selectedUser.setEmail(email);
            selectedUser.setPhoneNumber(phone);
            selectedUser.setRoleInt(roleInt);

            String hashedPassword = null;

            if (!password.isBlank()) {
                hashedPassword = LoginController.PasswordHasher.hash(password);
            }

            boolean ok = userManager.updateUser(
                    selectedUser.getId(),
                    username,
                    name,
                    email,
                    phone,
                    hashedPassword,
                    roleInt
            );

            if (ok) {
                showAlert("Success", "User updated");
            } else {
                showAlert("Error", "Update failed");
            }
        }

        clearFields();
        loadUsers();
    }

    // DELETE
    @FXML
    private void deleteUser() {

        User selected = tableUsers.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Select a user first");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Delete user: " + selected.getUsername() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (userManager.deleteUser(selected.getId())) {
                showAlert("Success", "User deleted");
                loadUsers();
            } else {
                showAlert("Error", "Delete failed");
            }
        }
    }

    // CLOSE
    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }

    // CLEAR
    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        txtName.clear();
        txtPhone.clear();
        txtEmail.clear();
        comboRole.getSelectionModel().clearSelection();

        tableUsers.getSelectionModel().clearSelection();
        selectedUser = null;
    }

    // ALERT
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}