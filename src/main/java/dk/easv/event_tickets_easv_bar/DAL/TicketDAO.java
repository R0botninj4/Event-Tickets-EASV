package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Ticket;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;
import dk.easv.event_tickets_easv_bar.DAL.Interface.ITicketDAO;

import java.io.IOException;
import java.sql.*;

public class TicketDAO implements ITicketDAO {

    private DBConnector dbConnector;

    public TicketDAO(){
        try{
            dbConnector = new DBConnector();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void buyTicket(Ticket ticket){

        String sql = "INSERT INTO Tickets (EventID,CustomerID,TicketAmount) VALUES (?,?,?)";

        try(Connection conn = dbConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,ticket.getEventId());
            stmt.setInt(2,ticket.getCustomerId());
            stmt.setInt(3,ticket.getAmount());

            stmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}