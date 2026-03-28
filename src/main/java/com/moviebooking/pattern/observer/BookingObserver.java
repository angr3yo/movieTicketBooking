package com.moviebooking.pattern.observer;

import com.moviebooking.model.Booking;
import java.util.ArrayList;
import java.util.List;

/**
 * OBSERVER PATTERN (Behavioral)
 * ──────────────────────────────
 * Notifies all registered observers when a booking event occurs.
 * Decouples the booking service from notification logic.
 *
 * GRASP Low Coupling: BookingEventPublisher doesn't
 * know concrete notification types.
 * SOLID Open/Closed: Add new notification channels
 * (Push, WhatsApp) without modifying publishers.
 */

// ─── Observer Interface ──────────────────────────────────────────────────────
public interface BookingObserver {
    void onBookingConfirmed(Booking booking);
    void onBookingCancelled(Booking booking);
}

// ─── Concrete Observer: Email Notification ───────────────────────────────────
class EmailNotificationObserver implements BookingObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        String email = booking.getUser().getEmail();
        String movieTitle = booking.getShow().getMovie().getTitle();
        System.out.println("[EMAIL] Sent booking confirmation to " + email +
                " for movie: " + movieTitle +
                " | Booking ID: " + booking.getId() +
                " | Amount: ₹" + booking.getTotalAmount());
        // Integrate actual email service (JavaMailSender) here
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        String email = booking.getUser().getEmail();
        System.out.println("[EMAIL] Sent cancellation notice to " + email +
                " for Booking ID: " + booking.getId());
    }
}

// ─── Concrete Observer: SMS Notification ────────────────────────────────────
class SmsNotificationObserver implements BookingObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[SMS] Sent confirmation SMS for Booking ID: " + booking.getId());
        // Integrate SMS gateway (Twilio, MSG91) here
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("[SMS] Sent cancellation SMS for Booking ID: " + booking.getId());
    }
}

// ─── Subject (Publisher) ────────────────────────────────────────────────────
class BookingEventPublisher {
    private final List<BookingObserver> observers = new ArrayList<>();

    public void subscribe(BookingObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(BookingObserver observer) {
        observers.remove(observer);
    }

    public void notifyConfirmed(Booking booking) {
        observers.forEach(o -> o.onBookingConfirmed(booking));
    }

    public void notifyCancelled(Booking booking) {
        observers.forEach(o -> o.onBookingCancelled(booking));
    }
}
