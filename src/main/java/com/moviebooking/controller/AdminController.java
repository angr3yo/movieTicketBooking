package com.moviebooking.controller;

import com.moviebooking.model.*;
import com.moviebooking.service.*;
import com.moviebooking.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MovieService movieService;
    private final ShowService showService;
    private final BookingService bookingService;
    private final UserService userService;
    private final TheatreRepository theatreRepository;

    // ── Dashboard ──────────────────────────────────────────────────────────────
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalMovies",    movieService.getAllMovies().size());
        model.addAttribute("totalBookings",  bookingService.getAllBookings().size());
        model.addAttribute("totalUsers",     userService.getAllUsers().size());
        model.addAttribute("totalShows",     showService.getAllShows().size());
        model.addAttribute("recentBookings", bookingService.getAllBookings().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ── Movies ─────────────────────────────────────────────────────────────────
    @GetMapping("/movies")
    public String listMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/movies";
    }

    @GetMapping("/movies/add")
    public String addMoviePage(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("statuses", Movie.MovieStatus.values());
        return "admin/movie-form";
    }

    @GetMapping("/movies/edit/{id}")
    public String editMoviePage(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        model.addAttribute("movie", movie);
        model.addAttribute("statuses", Movie.MovieStatus.values());
        return "admin/movie-form";
    }

    @PostMapping("/movies/save")
    public String saveMovie(@ModelAttribute Movie movie, RedirectAttributes ra) {
        if (movie.getId() != null) {
            movieService.updateMovie(movie);
            ra.addFlashAttribute("success", "Movie updated successfully.");
        } else {
            movie.setPosterUrl(generatePoster(movie.getTitle(), "#0f3460", "#e94560"));
            movieService.addMovie(movie);
            ra.addFlashAttribute("success", "Movie added successfully.");
        }
        return "redirect:/admin/movies";
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes ra) {
        movieService.deleteMovie(id);
        ra.addFlashAttribute("success", "Movie deleted.");
        return "redirect:/admin/movies";
    }

    // ── Shows ──────────────────────────────────────────────────────────────────
    @GetMapping("/shows")
    public String listShows(Model model) {
        model.addAttribute("shows",    showService.getAllShows());
        model.addAttribute("movies",   movieService.getAllMovies());
        model.addAttribute("theatres", theatreRepository.findAll());
        return "admin/shows";
    }

    @PostMapping("/shows/add")
    public String addShow(@RequestParam Long movieId,
                          @RequestParam Long theatreId,
                          @RequestParam String showTime,
                          @RequestParam double ticketPrice,
                          RedirectAttributes ra) {
        Movie movie = movieService.getMovieById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new IllegalArgumentException("Theatre not found"));

        Show show = Show.builder()
                .movie(movie)
                .theatre(theatre)
                .showTime(LocalDateTime.parse(showTime))
                .ticketPrice(ticketPrice)
                .availableSeats(0)
                .build();
        showService.createShow(show);
        ra.addFlashAttribute("success", "Show added with " + theatre.getTotalSeats() + " seats.");
        return "redirect:/admin/shows";
    }

    @PostMapping("/shows/delete/{id}")
    public String deleteShow(@PathVariable Long id, RedirectAttributes ra) {
        showService.deleteShow(id);
        ra.addFlashAttribute("success", "Show deleted.");
        return "redirect:/admin/shows";
    }

    // ── Theatres ───────────────────────────────────────────────────────────────
    @GetMapping("/theatres")
    public String listTheatres(Model model) {
        model.addAttribute("theatres", theatreRepository.findAll());
        return "admin/theatres";
    }

    @PostMapping("/theatres/save")
    public String saveTheatre(@RequestParam(required = false) Long id,
                              @RequestParam String name,
                              @RequestParam String location,
                              @RequestParam int totalSeats,
                              RedirectAttributes ra) {
        Theatre theatre = Theatre.builder()
                .id(id).name(name).location(location).totalSeats(totalSeats).build();
        theatreRepository.save(theatre);
        ra.addFlashAttribute("success", id != null ? "Theatre updated." : "Theatre added.");
        return "redirect:/admin/theatres";
    }

    @PostMapping("/theatres/delete/{id}")
    public String deleteTheatre(@PathVariable Long id, RedirectAttributes ra) {
        theatreRepository.deleteById(id);
        ra.addFlashAttribute("success", "Theatre deleted.");
        return "redirect:/admin/theatres";
    }

    // ── Bookings ───────────────────────────────────────────────────────────────
    @GetMapping("/bookings")
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings";
    }

    // ── Users ──────────────────────────────────────────────────────────────────
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }

    // ── Inline SVG poster for admin-added movies ───────────────────────────────
    private String generatePoster(String title, String bg, String accent) {
        String[] parts = title.split(" ", 2);
        String l1 = parts[0], l2 = parts.length > 1 ? parts[1] : "";
        String svg = String.format(
            "<svg xmlns='http://www.w3.org/2000/svg' width='300' height='450'>" +
            "<rect width='300' height='450' fill='%s'/>" +
            "<rect y='380' width='300' height='70' fill='%s'/>" +
            "<circle cx='150' cy='190' r='45' fill='%s' opacity='0.3'/>" +
            "<polygon points='134,168 134,212 172,190' fill='%s'/>" +
            "<text x='150' y='408' font-family='Arial' font-size='22' font-weight='bold' fill='white' text-anchor='middle'>%s</text>" +
            "<text x='150' y='432' font-family='Arial' font-size='15' fill='white' text-anchor='middle'>%s</text>" +
            "</svg>", bg, accent, accent, accent, esc(l1), esc(l2));
        String b64 = java.util.Base64.getEncoder()
                .encodeToString(svg.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return "data:image/svg+xml;base64," + b64;
    }

    private String esc(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
