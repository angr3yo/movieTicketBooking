package com.moviebooking.controller;

import com.moviebooking.model.Movie;
import com.moviebooking.service.MovieService;
import com.moviebooking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER LAYER (MVC)
 * Handles movie browsing, searching, and detail viewing.
 *
 * GRASP Controller: dedicated controller for movie use cases.
 * SOLID SRP: only movie browsing concern.
 */
@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ShowService showService;

    // ── Home / Movie Listing ─────────────────────────────────────────────────
    @GetMapping({"/", "/movies"})
    public String listMovies(@RequestParam(required = false) String search,
                             @RequestParam(required = false) String genre,
                             Model model) {
        List<Movie> movies;

        if (search != null && !search.isBlank()) {
            movies = movieService.searchMovies(search);
            model.addAttribute("search", search);
        } else if (genre != null && !genre.isBlank()) {
            movies = movieService.getMoviesByGenre(genre);
            model.addAttribute("selectedGenre", genre);
        } else {
            movies = movieService.getNowShowingMovies();
        }

        model.addAttribute("movies", movies);
        model.addAttribute("genres", List.of("Action", "Sci-Fi", "Horror Comedy",
                                              "Thriller", "Romance", "Drama"));
        return "user/movies";
    }

    // ── Movie Search ─────────────────────────────────────────────────────────
    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam String keyword, Model model) {
        model.addAttribute("movies", movieService.searchMovies(keyword));
        model.addAttribute("keyword", keyword);
        return "user/movies";
    }

    // ── Movie Detail + Available Shows ───────────────────────────────────────
    @GetMapping("/movies/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        model.addAttribute("movie", movie);
        model.addAttribute("shows", showService.getAvailableShowsByMovie(id));
        return "user/movie-detail";
    }
}
