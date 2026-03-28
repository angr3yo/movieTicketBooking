package com.moviebooking.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * MODEL LAYER (MVC)
 * Represents a seat in a show.
 * GRASP: Information Expert — knows its own availability
 */
@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    private String seatNumber;

    @Column(name = "seat_row")   // "row" is a reserved word in H2
    private String row;

    private int seatIndex;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type")  // "type" is a reserved word in H2
    private SeatType type;

    public enum SeatStatus {
        AVAILABLE, BOOKED, RESERVED
    }

    public enum SeatType {
        REGULAR, PREMIUM, VIP
    }
}
