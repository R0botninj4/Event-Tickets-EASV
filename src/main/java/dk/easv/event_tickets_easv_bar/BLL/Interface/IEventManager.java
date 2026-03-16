package dk.easv.event_tickets_easv_bar.BLL.Interface;

import dk.easv.event_tickets_easv_bar.BE.Event;
import java.util.List;

public interface IEventManager {

    List<Event> getAllEvents();

    Event getEventById(int id);

    void createEvent(Event event);

}