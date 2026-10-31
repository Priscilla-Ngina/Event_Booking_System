package com.eventbooking.event_booking_system.dto;


import com.eventbooking.event_booking_system.entity.Event;
import java.time.LocalDateTime;

public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private String venue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private Event.EventStatus status;

    public EventResponse(
            Long id,
            String title,
            String description,
            String venue,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime createdAt,
            Event.EventStatus status) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.status = status;
    }

public  Long getId() {
	return id;
}

public String getTitle() {
	return title;
}
public String getDescription() {
	return description;
}

public String getVenue() {
    return venue;
}

public LocalDateTime getStartDate() {
    return startDate;
}

public LocalDateTime getEndDate() {
    return endDate;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public Event.EventStatus getStatus() {
    return status;
}


}
