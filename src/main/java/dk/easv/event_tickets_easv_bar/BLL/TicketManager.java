package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.Ticket;
import dk.easv.event_tickets_easv_bar.DAL.Interface.ITicketDAO;
import dk.easv.event_tickets_easv_bar.DAL.TicketDAO;

import java.util.List;

public class TicketManager {

    private final ITicketDAO ticketDAO;

    public TicketManager() {
        ticketDAO = new TicketDAO();
    }

    public void buyTicket(Ticket ticket) {
        ticketDAO.buyTicket(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    public List<Ticket> getTicketsByEvent(int eventId) {
        return ticketDAO.getTicketsByEvent(eventId);
    }

    public List<Ticket> getTicketsByUser(int userId) {
        return ticketDAO.getTicketsByUser(userId);
    }
}