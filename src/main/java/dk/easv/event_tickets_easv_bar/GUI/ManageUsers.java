package dk.easv.event_tickets_easv_bar.GUI;

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
        // Roles in ComboBox
        comboRole.setItems(FXCollections.observableArrayList("Admin", "Coordinator", "Customer"));

        // Bind columns to User properties
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleText"));

        loadUsers();
    }

    private void loadUsers() {
        users.clear();
        users.addAll(userManager.getAllUsers());
        tableUsers.setItems(users);
    }

    @FXML
    private void addUser() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String roleStr = comboRole.getValue();

        if(username.isEmpty() || password.isEmpty() || name.isEmpty() || roleStr == null) {
            showAlert("Error", "Username, password, name, and role are required");
            return;
        }

        int roleInt = switch(roleStr) {
            case "Admin" -> 1;
            case "Coordinator" -> 2;
            default -> 3;
        };

        int id = userManager.addUser(username, password, name, email, phone, roleInt);
        if(id != -1) {
            showAlert("Success", "User added with ID: " + id);
            clearFields();
            loadUsers();
        } else {
            showAlert("Error", "Failed to add user");
        }
    }

    @FXML
    private void editUser() {
        selectedUser = tableUsers.getSelectionModel().getSelectedItem();
        if(selectedUser == null) {
            showAlert("Error", "Please select a user to edit");
            return;
        }

        txtUsername.setText(selectedUser.getUsername());
        txtName.setText(selectedUser.getName());
        txtEmail.setText(selectedUser.getEmail());
        txtPhone.setText(selectedUser.getPhoneNumber());
        comboRole.setValue(selectedUser.getRoleText());
    }

    @FXML
    private void updateUser() {
        if(selectedUser == null) {
            showAlert("Error", "No user selected for update");
            return;
        }

        String username = txtUsername.getText();
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

        selectedUser.setUsername(username);
        selectedUser.setName(name);
        selectedUser.setEmail(email);
        selectedUser.setPhoneNumber(phone);
        selectedUser.setRoleInt(roleInt);

        boolean success = userManager.updateUser(selectedUser);

        if(success) {
            showAlert("Success", "User updated successfully");
            clearFields();
            loadUsers();
            selectedUser = null;
        } else {
            showAlert("Error", "Failed to update user");
        }
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
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}