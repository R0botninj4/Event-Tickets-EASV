package dk.easv.event_tickets_easv_bar.GUI;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.UserManager;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageUsers implements ClosableWindow {

    private final UserManager userManager = new UserManager();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private User selectedUser;

    // TableView and columns
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colPhone;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;

    // Input fields
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboRole;
    @FXML private Button closeButton;
    @FXML private TextField txtSearch;

    @FXML
    public void initialize() {
        tableUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        enableEscClose(closeButton);

        comboRole.setItems(FXCollections.observableArrayList("Admin", "Coordinator", "Customer"));

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleText"));

        // 🔥 AUTO LOAD når man klikker
        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedUser = newSel;

                txtUsername.setText(newSel.getUsername());
                txtName.setText(newSel.getName());
                txtEmail.setText(newSel.getEmail());
                txtPhone.setText(newSel.getPhoneNumber());
                comboRole.setValue(newSel.getRoleText());

                txtPassword.clear(); // 🔥 password vises ikke
            }
        });

        FilteredList<User> filteredUsers = new FilteredList<>(users, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String search = newValue.toLowerCase();

                if (user.getUsername().toLowerCase().contains(search)) return true;
                if (user.getName().toLowerCase().contains(search)) return true;
                if (user.getEmail() != null && user.getEmail().toLowerCase().contains(search)) return true;
                if (user.getPhoneNumber() != null && user.getPhoneNumber().toLowerCase().contains(search)) return true;
                if (user.getRoleText().toLowerCase().contains(search)) return true;

                return false;
            });
        });

        SortedList<User> sortedUsers = new SortedList<>(filteredUsers);
        sortedUsers.comparatorProperty().bind(tableUsers.comparatorProperty());

        tableUsers.setItems(sortedUsers);

        loadUsers();
    }

    private void loadUsers() {
        users.clear();
        users.addAll(userManager.getAllUsers());

    }

    // ➕ PLUS KNAP = NY USER (ryd felter)
    @FXML
    private void addUser() {
        clearFields();
    }

    // 💾 SAVE = ADD + UPDATE
    @FXML
    private void updateUser() {

        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String roleStr = comboRole.getValue();

        if(username.isEmpty() || name.isEmpty() || roleStr == null) {
            showAlert("Error", "Username, name, and role are required");
            return;
        }

        int roleInt = switch(roleStr) {
            case "Admin" -> 1;
            case "Coordinator" -> 2;
            default -> 3;
        };

        // 🔥 ADD
        if (selectedUser == null) {

            if(password.isEmpty()) {
                showAlert("Error", "Password required for new user");
                return;
            }

            int id = userManager.addUser(username, password, name, email, phone, roleInt);

            if(id != -1) {
                showAlert("Success", "User created!");
            } else {
                showAlert("Error", "Failed to create user");
            }

        } else {
            // 🔥 UPDATE
            selectedUser.setUsername(username);
            selectedUser.setName(name);
            selectedUser.setEmail(email);
            selectedUser.setPhoneNumber(phone);
            selectedUser.setRoleInt(roleInt);

            boolean success = userManager.updateUser(selectedUser);

            if(success) {
                showAlert("Success", "User updated!");
            } else {
                showAlert("Error", "Failed to update user");
            }
        }

        clearFields();
        loadUsers();
    }

    @FXML
    private void deleteUser() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if(selected == null) {
            showAlert("Error", "Please select a user to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete user: " + selected.getUsername() + "?");

        if(confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean success = userManager.deleteUser(selected.getId());
            if(success) {
                showAlert("Success", "User deleted successfully");
                loadUsers();
            } else {
                showAlert("Error", "Failed to delete user");
            }
        }
    }

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        txtName.clear();
        txtPhone.clear();
        txtEmail.clear();
        comboRole.getSelectionModel().clearSelection();

        tableUsers.getSelectionModel().clearSelection(); // 🔥
        selectedUser = null; // 🔥
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}