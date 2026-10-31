package com.eventbooking.event_booking_system.controller;

import com.eventbooking.event_booking_system.dto.TicketTypeRequest;
import com.eventbooking.event_booking_system.dto.TicketTypeResponse;
import com.eventbooking.event_booking_system.service.TicketTypeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    public TicketTypeController(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    @PostMapping("/events/{eventId}/ticket-types")
    @PreAuthorize("hasRole('ORGANIZER')")
    public TicketTypeResponse createTicketType(
            @PathVariable Long eventId,
            @Valid @RequestBody TicketTypeRequest request,
            Authentication authentication) {
        return ticketTypeService.createTicketType(
                eventId, authentication.getName(), request);
    }

    @GetMapping("/events/{eventId}/ticket-types")
    public List<TicketTypeResponse> getAvailableTicketTypes(
            @PathVariable Long eventId) {
        return ticketTypeService.getAvailableTicketTypes(eventId);
    }

    @PutMapping("/ticket-types/{ticketTypeId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public TicketTypeResponse updateTicketType(
            @PathVariable Long ticketTypeId,
            @Valid @RequestBody TicketTypeRequest request,
            Authentication authentication) {

        return ticketTypeService.updateTicketType(
                ticketTypeId,
                authentication.getName(),
                request
        );
    }

    @PatchMapping("/ticket-types/{ticketTypeId}/status")
    @PreAuthorize("hasRole('ORGANIZER')")
    public TicketTypeResponse updateTicketTypeStatus(
            @PathVariable Long ticketTypeId,
            @RequestParam Boolean active,
            Authentication authentication) {

        return ticketTypeService.updateTicketTypeStatus(
                ticketTypeId,
                authentication.getName(),
                active
        );
    }

    @GetMapping("/events/{eventId}/ticket-types/manage")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<TicketTypeResponse> getOrganizerTicketTypes(
            @PathVariable Long eventId,
            Authentication authentication) {

        return ticketTypeService.getOrganizerTicketTypes(
                eventId,
                authentication.getName()
        );
    }
}