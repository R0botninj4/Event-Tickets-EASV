package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.DAL.EventDAO;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IEventDAO;

import java.util.List;

public class EventManager {

    private EventDAO eventDAO = new EventDAO();

    public List<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    public void createEvent(Event event) {
        eventDAO.createEvent(event);
    }

    public void updateEvent(Event event) {
        eventDAO.updateEvent(event);
    }

    public void deleteEvent(int id) {
        eventDAO.deleteEvent(id);
    }
}