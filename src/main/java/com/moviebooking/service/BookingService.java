package com.moviebooking.service;

import com.moviebooking.model.*;
import com.moviebooking.pattern.factory.TicketFactory;
import com.moviebooking.pattern.observer.BookingObserver;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.SeatRepository;
import com.moviebooking.repository.ShowRepository;
import com.moviebooking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Core booking logic.
 * Applies: GRASP Creator, Observer Pattern, Strategy Pattern, SOLID SRP.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    private final List<BookingObserver> observers = new ArrayList<>();

    public void registerObserver(BookingObserver observer) {
        observers.add(observer);
    }

    @Transactional
    public Booking bookTickets(User user, Show show, List<Long> seatIds,
                               Booking.PaymentMethod paymentMethod) {

        List<Seat> seats = seatRepository.findAllById(seatIds);
        for (Seat seat : seats) {
            if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is not available.");
            }
        }

        Booking booking = Booking.builder()
                .user(user)
                .show(show)
                .bookingTime(LocalDateTime.now())
                .status(Booking.BookingStatus.PENDING)
                .paymentMethod(paymentMethod)
                .build();
        booking = bookingRepository.save(booking);

        List<Ticket> tickets = new ArrayList<>();
        double total = 0;
        for (Seat seat : seats) {
            Ticket ticket = TicketFactory.createTicket(booking, seat);
            tickets.add(ticketRepository.save(ticket));
            total += ticket.getPrice();
            seat.setStatus(Seat.SeatStatus.BOOKED);
            seatRepository.save(seat);
        }

        booking.setTickets(tickets);
        booking.setTotalAmount(total);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

        show.setAvailableSeats(show.getAvailableSeats() - seats.size());
        showRepository.save(show);

        final Booking confirmed = booking;
        observers.forEach(o -> o.onBookingConfirmed(confirmed));

        return booking;
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        if (!booking.getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot cancel another user's booking.");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);

        for (Ticket ticket : booking.getTickets()) {
            Seat seat = ticket.getSeat();
            seat.setStatus(Seat.SeatStatus.AVAILABLE);
            seatRepository.save(seat);
        }

        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats() + booking.getTickets().size());
        showRepository.save(show);

        final Booking cancelled = bookingRepository.save(booking);
        observers.forEach(o -> o.onBookingCancelled(cancelled));
        return cancelled;
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
}
