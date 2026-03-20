package dk.easv.event_tickets_easv_bar.DAL.Interface;

import dk.easv.event_tickets_easv_bar.BE.Ticket;

import java.util.List;

public interface ITicketDAO {

    void buyTicket(Ticket ticket);

    List<Ticket> getAllTickets();

    List<Ticket> getTicketsByEvent(int eventId);

    List<Ticket> getTicketsByUser(int userId);
}