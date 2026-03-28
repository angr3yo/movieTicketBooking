package com.moviebooking.repository;

import com.moviebooking.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieId(Long movieId);
    List<Show> findByTheatreId(Long theatreId);

    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId AND s.availableSeats > 0")
    List<Show> findAvailableShowsByMovie(Long movieId);
}
