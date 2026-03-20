package dk.easv.event_tickets_easv_bar.BE;

public class User {

    private int id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private int role;

    public User(int id, String username, String name, String email, String phoneNumber, int role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public int getId() { return id; }

    public String getUsername() { return username; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getPhoneNumber() { return phoneNumber; }

    public int getRole() { return role; }
}