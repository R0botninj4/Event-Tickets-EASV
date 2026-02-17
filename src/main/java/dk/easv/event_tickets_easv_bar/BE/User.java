package dk.easv.event_tickets_easv_bar.BE;

public class User {
    private int id;
    private String username;
    private int role; // 1 = Admin, 2 = Coordinator, 3 = Customer

    public User(int id, String username, int role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public int getRole() { return role; }
}
