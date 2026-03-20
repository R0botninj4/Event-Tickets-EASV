package dk.easv.event_tickets_easv_bar.DAL.Interface;

import dk.easv.event_tickets_easv_bar.BE.Event;
import java.util.List;

public interface IEventDAO {

    List<Event> getAllEvents();

    Event getEventById(int id);

    void createEvent(Event event);

    void updateEvent(Event event);

    void deleteEvent(int id);
}