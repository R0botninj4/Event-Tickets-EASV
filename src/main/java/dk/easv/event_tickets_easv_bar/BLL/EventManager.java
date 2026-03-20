package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.DAL.EventDAO;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IEventDAO;

import java.util.List;

public class EventManager {

    private final IEventDAO eventDAO;

    public EventManager() {
        eventDAO = new EventDAO();
    }

    public List<Event> getEvents() {
        return eventDAO.getAllEvents();
    }

    public Event getEventById(int id) {
        return eventDAO.getEventById(id);
    }

    public void createEvent(Event event) {
        eventDAO.createEvent(event);
    }
}