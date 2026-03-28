package com.moviebooking.service;

import com.moviebooking.model.*;
import com.moviebooking.repository.SeatRepository;
import com.moviebooking.repository.ShowRepository;
import com.moviebooking.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final TheatreRepository theatreRepository;

    @Transactional
    public Show createShow(Show show) {
        Show saved = showRepository.save(show);
        generateSeatsForShow(saved);
        return saved;
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public Optional<Show> getShowById(Long id) {
        return showRepository.findById(id);
    }

    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    public List<Show> getAvailableShowsByMovie(Long movieId) {
        return showRepository.findAvailableShowsByMovie(movieId);
    }

    public List<Seat> getSeatsByShow(Long showId) {
        return seatRepository.findByShowId(showId);
    }

    public List<Seat> getAvailableSeatsByShow(Long showId) {
        return seatRepository.findByShowIdAndStatus(showId, Seat.SeatStatus.AVAILABLE);
    }

    @Transactional
    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }

    private void generateSeatsForShow(Show show) {
        String[] rows = {"A","B","C","D","E","F","G","H","I","J","K","L"};
        int seatsPerRow = 10;
        List<Seat> seats = new ArrayList<>();
        for (String row : rows) {
            for (int i = 1; i <= seatsPerRow; i++) {
                Seat.SeatType type = switch (row) {
                    case "A","B"      -> Seat.SeatType.VIP;
                    case "C","D","E"  -> Seat.SeatType.PREMIUM;
                    default           -> Seat.SeatType.REGULAR;
                };
                seats.add(Seat.builder()
                        .show(show).row(row).seatIndex(i)
                        .seatNumber(row + i)
                        .status(Seat.SeatStatus.AVAILABLE)
                        .type(type).build());
            }
        }
        seatRepository.saveAll(seats);
        show.setAvailableSeats(seats.size());
        showRepository.save(show);
    }
}
