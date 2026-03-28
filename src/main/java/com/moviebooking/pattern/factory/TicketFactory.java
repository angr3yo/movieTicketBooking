package com.moviebooking.pattern.factory;

import com.moviebooking.model.Booking;
import com.moviebooking.model.Ticket;
import com.moviebooking.model.Seat;

/**
 * FACTORY PATTERN (Creational)
 * ─────────────────────────────
 * Abstracts the creation of Ticket objects.
 * GRASP Creator: Factory is the designated creator,
 * decoupling creation from business logic.
 *
 * SOLID Open/Closed: New ticket types can be added
 * by extending this without modifying existing code.
 */
public class TicketFactory {

    public enum TicketCategory {
        REGULAR, PREMIUM, VIP
    }

    /**
     * Factory method: creates the right Ticket subtype
     * based on seat category.
     */
    public static Ticket createTicket(Booking booking, Seat seat) {
        double basePrice = booking.getShow().getTicketPrice();

        double finalPrice = switch (seat.getType()) {
            case PREMIUM -> basePrice * 1.5;
            case VIP     -> basePrice * 2.0;
            default      -> basePrice;          // REGULAR
        };

        return Ticket.builder()
                .booking(booking)
                .seat(seat)
                .seatNumber(seat.getSeatNumber())
                .price(finalPrice)
                .build();
    }
}
