package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.Ticket;
import dk.easv.event_tickets_easv_bar.BLL.Interface.ITicketManager;
import dk.easv.event_tickets_easv_bar.DAL.Interface.ITicketDAO;
import dk.easv.event_tickets_easv_bar.DAL.TicketDAO;

public class TicketManager implements ITicketManager {

    private final ITicketDAO ticketDAO;

    public TicketManager() {
        ticketDAO = new TicketDAO();
    }

    @Override
    public void buyTicket(Ticket ticket) {
        ticketDAO.buyTicket(ticket);
    }
}