package com.moviebooking.controller;

import com.moviebooking.model.*;
import com.moviebooking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * CONTROLLER LAYER (MVC)
 * Handles seat selection, booking, payment, confirmation, history,
 * and cancellation.
 *
 * GRASP Controller: handles all booking-related user interactions.
 * SOLID SRP: only booking workflow concern.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;
    private final ShowService showService;
    private final UserService userService;

    // ── Step 1: Seat Selection ───────────────────────────────────────────────
    @GetMapping("/select-seats/{showId}")
    public String selectSeats(@PathVariable Long showId, Model model) {
        Show show = showService.getShowById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found"));
        List<Seat> seats = showService.getSeatsByShow(showId);

        model.addAttribute("show", show);
        model.addAttribute("seats", seats);
        model.addAttribute("paymentMethods", Booking.PaymentMethod.values());
        return "user/seat-selection";
    }

    // ── Step 2: Process Booking (POST) ───────────────────────────────────────
    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam Long showId,
                                 @RequestParam List<Long> seatIds,
                                 @RequestParam String paymentMethod,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            Show show = showService.getShowById(showId)
                    .orElseThrow(() -> new IllegalArgumentException("Show not found"));

            Booking.PaymentMethod method = Booking.PaymentMethod.valueOf(paymentMethod);
            Booking booking = bookingService.bookTickets(user, show, seatIds, method);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Booking confirmed! Your booking ID is #" + booking.getId());
            return "redirect:/booking/confirmation/" + booking.getId();

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/booking/select-seats/" + showId;
        }
    }

    // ── Step 3: Booking Confirmation ─────────────────────────────────────────
    @GetMapping("/confirmation/{bookingId}")
    public String bookingConfirmation(@PathVariable Long bookingId,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      Model model) {
        Booking booking = bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        model.addAttribute("booking", booking);
        return "user/booking-confirmation";
    }

    // ── Booking History ──────────────────────────────────────────────────────
    @GetMapping("/history")
    public String bookingHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        model.addAttribute("bookings", bookingService.getBookingsByUser(user.getId()));
        return "user/booking-history";
    }

    // ── Cancel Booking ───────────────────────────────────────────────────────
    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        try {
            bookingService.cancelBooking(bookingId, user.getId());
            redirectAttributes.addFlashAttribute("message", "Booking #" + bookingId + " cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/booking/history";
    }
}
