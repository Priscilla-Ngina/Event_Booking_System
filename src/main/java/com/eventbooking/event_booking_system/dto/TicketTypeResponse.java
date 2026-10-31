package com.eventbooking.event_booking_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketTypeResponse {

    private final Long id;
    private final Long eventId;
    private final String name;
    private final BigDecimal price;
    private final Integer totalQuantity;
    private final Integer soldQuantity;
    private final Integer availableQuantity;
    private final LocalDateTime salesStartDate;
    private final LocalDateTime salesEndDate;
    private final Boolean active;

    public TicketTypeResponse(
            Long id,
            Long eventId,
            String name,
            BigDecimal price,
            Integer totalQuantity,
            Integer soldQuantity,
            LocalDateTime salesStartDate,
            LocalDateTime salesEndDate,
            Boolean active) {

        this.id = id;
        this.eventId = eventId;
        this.name = name;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.soldQuantity = soldQuantity;
        this.availableQuantity = totalQuantity - soldQuantity;
        this.salesStartDate = salesStartDate;
        this.salesEndDate = salesEndDate;
        this.active = active;
    }

    public Long getId() { return id; }
    public Long getEventId() { return eventId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public Integer getSoldQuantity() { return soldQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public LocalDateTime getSalesStartDate() { return salesStartDate; }
    public LocalDateTime getSalesEndDate() { return salesEndDate; }
    public Boolean getActive() { return active; }
}