package com.eventbooking.event_booking_system.dto;

import java.math.BigDecimal;

public class BookingItemResponse {

    private Long ticketTypeId;
    private String ticketTypeName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public BookingItemResponse(Long ticketTypeId, String ticketTypeName,
                               Integer quantity, BigDecimal unitPrice,
                               BigDecimal subtotal) {
        this.ticketTypeId = ticketTypeId;
        this.ticketTypeName = ticketTypeName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public Long getTicketTypeId() { return ticketTypeId; }
    public String getTicketTypeName() { return ticketTypeName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
}