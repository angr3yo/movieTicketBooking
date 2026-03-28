package com.moviebooking.config;

import com.moviebooking.model.*;
import com.moviebooking.repository.MovieRepository;
import com.moviebooking.repository.TheatreRepository;
import com.moviebooking.repository.UserRepository;
import com.moviebooking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ShowService showService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ── Users ──
        userRepository.save(User.builder()
                .name("Admin User").email("admin@movies.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN).build());

        userRepository.save(User.builder()
                .name("Arjun Kumar").email("arjun@example.com")
                .password(passwordEncoder.encode("user123"))
                .role(User.Role.USER).build());

        userRepository.save(User.builder()
                .name("Priya Sharma").email("priya@example.com")
                .password(passwordEncoder.encode("user123"))
                .role(User.Role.USER).build());

        // ── Movies ──
        Movie m1 = movieRepository.save(Movie.builder()
                .title("Kalki 2898 AD").genre("Sci-Fi").language("Telugu")
                .durationMinutes(181).rating(8.2)
                .description("A sci-fi epic set in the year 2898 AD, blending mythology and futuristic technology.")
                .posterUrl(poster("Kalki", "2898 AD", "#0f3460", "#e94560"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m2 = movieRepository.save(Movie.builder()
                .title("Pushpa 2: The Rule").genre("Action").language("Telugu")
                .durationMinutes(175).rating(8.6)
                .description("Pushpa Raj rises to power, defying the system in this high-octane sequel.")
                .posterUrl(poster("Pushpa 2", "The Rule", "#1a0a00", "#ff6b00"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m3 = movieRepository.save(Movie.builder()
                .title("Stree 2").genre("Horror Comedy").language("Hindi")
                .durationMinutes(135).rating(8.8)
                .description("The spirit returns. The town must face its worst fear once again.")
                .posterUrl(poster("Stree 2", "Horror Comedy", "#0a0a1a", "#9b5de5"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        movieRepository.save(Movie.builder()
                .title("Animal Park").genre("Thriller").language("Hindi")
                .durationMinutes(202).rating(7.4)
                .description("The saga of vengeance continues in this dark, visceral sequel.")
                .posterUrl(poster("Animal", "Park", "#1a0000", "#c1121f"))
                .status(Movie.MovieStatus.COMING_SOON).build());

        // ── Theatres ──
        Theatre t1 = theatreRepository.save(Theatre.builder()
                .name("PVR Cinemas - Orion Mall").location("Bengaluru").totalSeats(120).build());
        Theatre t2 = theatreRepository.save(Theatre.builder()
                .name("INOX - Garuda Mall").location("Bengaluru").totalSeats(120).build());

        // ── Shows ──
        showService.createShow(Show.builder().movie(m1).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(2)).ticketPrice(250).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m1).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(6)).ticketPrice(300).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m2).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(3)).ticketPrice(280).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m2).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(5)).ticketPrice(260).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m3).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(1)).ticketPrice(220).availableSeats(0).build());

        System.out.println("\n==============================================");
        System.out.println("  Sample data seeded successfully!");
        System.out.println("  Admin : admin@movies.com  / admin123");
        System.out.println("  User  : arjun@example.com / user123");
        System.out.println("==============================================\n");
    }

    /**
     * Minimal inline SVG poster — stays well under 500 chars encoded.
     * No external HTTP request needed; works in all browsers.
     */
    private String poster(String line1, String line2, String bg, String accent) {
        String svg = String.format(
            "<svg xmlns='http://www.w3.org/2000/svg' width='300' height='450'>" +
            "<rect width='300' height='450' fill='%s'/>" +
            "<rect y='380' width='300' height='70' fill='%s'/>" +
            "<circle cx='150' cy='190' r='45' fill='%s' opacity='0.3'/>" +
            "<polygon points='134,168 134,212 172,190' fill='%s'/>" +
            "<text x='150' y='408' font-family='Arial' font-size='22' font-weight='bold' fill='white' text-anchor='middle'>%s</text>" +
            "<text x='150' y='432' font-family='Arial' font-size='15' fill='white' text-anchor='middle'>%s</text>" +
            "</svg>",
            bg, accent, accent, accent,
            escXml(line1), escXml(line2)
        );
        String b64 = Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
        return "data:image/svg+xml;base64," + b64;
    }

    private String escXml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
