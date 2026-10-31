package com.eventbooking.event_booking_system.service;

import com.eventbooking.event_booking_system.dto.EventRequest;
import com.eventbooking.event_booking_system.dto.EventResponse;
import com.eventbooking.event_booking_system.entity.Event;
import com.eventbooking.event_booking_system.entity.User;
import com.eventbooking.event_booking_system.repository.EventRepository;
import com.eventbooking.event_booking_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventResponse createEvent(String organizerEmail, EventRequest request) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (organizer.getRole() != User.Role.ORGANIZER) {
            throw new IllegalStateException("Only approved organizers can create events");
        }

        if (!request.getStartDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event start date must be in the future");
        }

        if (!request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException("Event end date must be after start date");
        }

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setVenue(request.getVenue());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());

        Event savedEvent = eventRepository.save(event);

        return new EventResponse(
                savedEvent.getId(),
                savedEvent.getTitle(),
                savedEvent.getDescription(),
                savedEvent.getVenue(),
                savedEvent.getStartDate(),
                savedEvent.getEndDate(),
                savedEvent.getCreatedAt(),
                savedEvent.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getPublishedEvents() {

        return eventRepository.findByStatusOrderByStartDateAsc(
                        Event.EventStatus.PUBLISHED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getOrganizerEvents(String organizerEmail) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return eventRepository.findByOrganizer_IdOrderByStartDateAsc(organizer.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public EventResponse publishEvent(Long eventId, String organizerEmail) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new IllegalStateException("You can only publish your own events");
        }

        if (event.getStatus() != Event.EventStatus.DRAFT) {
            throw new IllegalStateException("Only draft events can be published");
        }

        event.setStatus(Event.EventStatus.PUBLISHED);

        return mapToResponse(event);
    }

    @Transactional
    public EventResponse updateEvent(
            Long eventId,
            String organizerEmail,
            EventRequest request) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new IllegalStateException("You can only update your own events");
        }

        if (event.getStatus() != Event.EventStatus.DRAFT) {
            throw new IllegalStateException("Only draft events can be updated");
        }

        if (!request.getStartDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event start date must be in the future");
        }

        if (!request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException("Event end date must be after start date");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setVenue(request.getVenue());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());

        return mapToResponse(event);
    }

    @Transactional
    public EventResponse cancelEvent(Long eventId, String organizerEmail) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new IllegalStateException("You can only cancel your own events");
        }

        if (event.getStatus() == Event.EventStatus.CANCELLED) {
            throw new IllegalStateException("Event is already cancelled");
        }

        event.setStatus(Event.EventStatus.CANCELLED);

        return mapToResponse(event);
    }

    private EventResponse mapToResponse(Event event) {

        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getVenue(),
                event.getStartDate(),
                event.getEndDate(),
                event.getCreatedAt(),
                event.getStatus()
        );
    }

}
