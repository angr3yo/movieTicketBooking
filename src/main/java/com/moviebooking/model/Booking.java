package com.moviebooking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MODEL LAYER (MVC)
 * Represents a ticket booking made by a user.
 *
 * GRASP Creator: Booking creates Ticket objects
 * GRASP Information Expert: Booking has all info needed to compute total price
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    private LocalDateTime bookingTime;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // GRASP Creator: Booking creates Ticket objects
    public Ticket createTicket(Seat seat) {
        return Ticket.builder()
                .booking(this)
                .seat(seat)
                .seatNumber(seat.getSeatNumber())
                .price(this.show.getTicketPrice())
                .build();
    }

    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED
    }

    public enum PaymentMethod {
        UPI, CARD, NET_BANKING, WALLET
    }
}
