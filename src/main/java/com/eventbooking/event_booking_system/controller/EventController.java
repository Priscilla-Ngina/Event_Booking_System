package com.eventbooking.event_booking_system.controller;

import com.eventbooking.event_booking_system.dto.EventRequest;
import com.eventbooking.event_booking_system.dto.EventResponse;
import com.eventbooking.event_booking_system.service.EventService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public EventResponse createEvent(
            @Valid @RequestBody EventRequest request,
            Authentication authentication) {

        return eventService.createEvent(
                authentication.getName(), request);
    }

    @GetMapping
    public List<EventResponse> getPublishedEvents() {
        return eventService.getPublishedEvents();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventResponse> getOrganizerEvents(
            Authentication authentication) {
        return eventService.getOrganizerEvents(authentication.getName());
    }

    @PutMapping("/{eventId}/publish")
    @PreAuthorize("hasRole('ORGANIZER')")
    public EventResponse publishEvent(
            @PathVariable Long eventId,
            Authentication authentication) {

        return eventService.publishEvent(
                eventId,
                authentication.getName()
        );
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public EventResponse updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventRequest request,
            Authentication authentication) {

        return eventService.updateEvent(
                eventId,
                authentication.getName(),
                request
        );
    }

    @PutMapping("/{eventId}/cancel")
    @PreAuthorize("hasRole('ORGANIZER')")
    public EventResponse cancelEvent(
            @PathVariable Long eventId,
            Authentication authentication) {

        return eventService.cancelEvent(
                eventId,
                authentication.getName()
        );
    }

}
