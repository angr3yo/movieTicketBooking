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
        
        Movie m4 = movieRepository.save(Movie.builder()
                .title("Dhurandhar").genre("Action").language("Hindi")
                .durationMinutes(150).rating(8.0)
                .description("A fearless warrior rises against all odds to protect his people and honor.")
                .posterUrl(poster("Dhurandhar", "Action", "#1a1a1a", "#ff3c38"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m5 = movieRepository.save(Movie.builder()
                .title("La La Land").genre("Romance").language("English")
                .durationMinutes(128).rating(8.0)
                .description("A jazz musician and an aspiring actress fall in love while pursuing their dreams.")
                .posterUrl(poster("La La Land", "Romance", "#1d3557", "#f1c40f"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m6 = movieRepository.save(Movie.builder()
                .title("Forrest Gump").genre("Drama").language("English")
                .durationMinutes(142).rating(8.8)
                .description("The life journey of Forrest Gump, a man with a kind heart and extraordinary experiences.")
                .posterUrl(poster("Forrest Gump", "Drama", "#2c3e50", "#ecf0f1"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m7 = movieRepository.save(Movie.builder()
                .title("The Social Network").genre("Drama").language("English")
                .durationMinutes(120).rating(7.7)
                .description("The story behind the creation of Facebook and the legal battles that followed.")
                .posterUrl(poster("Social Network", "Drama", "#0a0a0a", "#3a86ff"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m8 = movieRepository.save(Movie.builder()
                .title("The Conjuring").genre("Horror").language("English")
                .durationMinutes(112).rating(7.5)
                .description("Paranormal investigators help a family terrorized by a dark presence.")
                .posterUrl(poster("Conjuring", "Horror", "#000000", "#6a040f"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m9 = movieRepository.save(Movie.builder()
                .title("K.G.F Chapter 1").genre("Action, Thriller").language("Kannada")
                .durationMinutes(155).rating(8.2)
                .description("A young man rises from poverty to become the most feared gangster in Kolar Gold Fields.")
                .posterUrl(poster("KGF", "Chapter 1", "#3a0ca3", "#f77f00"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m10 = movieRepository.save(Movie.builder()
                .title("Interstellar").genre("Sci-Fi").language("English")
                .durationMinutes(169).rating(8.6)
                .description("A team of explorers travel through a wormhole in space to ensure humanity's survival.")
                .posterUrl(poster("Interstellar", "Sci-Fi", "#0b132b", "#5bc0be"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m11 = movieRepository.save(Movie.builder()
                .title("Joker").genre("Drama").language("English")
                .durationMinutes(122).rating(8.4)
                .description("A mentally troubled comedian embarks on a downward spiral into chaos and crime.")
                .posterUrl(poster("Joker", "Drama", "#1a1a1a", "#e63946"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());

        Movie m12 = movieRepository.save(Movie.builder()
                .title("Avengers: Endgame").genre("Action").language("English")
                .durationMinutes(181).rating(8.4)
                .description("The Avengers assemble once more to reverse Thanos' actions and restore balance.")
                .posterUrl(poster("Endgame", "Avengers", "#0a0a23", "#ffd60a"))
                .status(Movie.MovieStatus.NOW_SHOWING).build());
        
        movieRepository.save(Movie.builder()
        .title("Avatar 3").genre("Sci-Fi").language("English")
        .durationMinutes(190).rating(8.5)
        .description("The journey on Pandora continues as new clans and threats emerge in this epic sequel.")
        .posterUrl(poster("Avatar", "3", "#001f3f", "#00b4d8"))
        .status(Movie.MovieStatus.COMING_SOON).build());

        movieRepository.save(Movie.builder()
                .title("Salaar Part 2").genre("Action").language("Telugu")
                .durationMinutes(170).rating(8.3)
                .description("The intense saga continues as alliances are tested and power struggles escalate.")
                .posterUrl(poster("Salaar", "Part 2", "#2b2d42", "#ef233c"))
                .status(Movie.MovieStatus.COMING_SOON).build());

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
        // Dhurandhar (m4)
        showService.createShow(Show.builder().movie(m4).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(2)).ticketPrice(240).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m4).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(5)).ticketPrice(260).availableSeats(0).build());

        // La La Land (m5)
        showService.createShow(Show.builder().movie(m5).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(3)).ticketPrice(220).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m5).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(6)).ticketPrice(240).availableSeats(0).build());

        // Forrest Gump (m6)
        showService.createShow(Show.builder().movie(m6).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(4)).ticketPrice(230).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m6).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(7)).ticketPrice(250).availableSeats(0).build());

        // Social Network (m7)
        showService.createShow(Show.builder().movie(m7).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(2)).ticketPrice(210).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m7).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(5)).ticketPrice(230).availableSeats(0).build());

        // Conjuring (m8)
        showService.createShow(Show.builder().movie(m8).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(8)).ticketPrice(260).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m8).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(10)).ticketPrice(280).availableSeats(0).build());

        // KGF Chapter 1 (m9)
        showService.createShow(Show.builder().movie(m9).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(3)).ticketPrice(270).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m9).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(6)).ticketPrice(290).availableSeats(0).build());

        // Interstellar (m10)
        showService.createShow(Show.builder().movie(m10).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(2)).ticketPrice(260).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m10).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(6)).ticketPrice(280).availableSeats(0).build());

        // Joker (m11)
        showService.createShow(Show.builder().movie(m11).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(3)).ticketPrice(240).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m11).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(7)).ticketPrice(260).availableSeats(0).build());

        // Avengers Endgame (m12)
        showService.createShow(Show.builder().movie(m12).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(4)).ticketPrice(300).availableSeats(0).build());
        showService.createShow(Show.builder().movie(m12).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(8)).ticketPrice(320).availableSeats(0).build());
                // Extra mixed shows (to reach 20)

        // Dhurandhar
        showService.createShow(Show.builder().movie(m4).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(9)).ticketPrice(250).availableSeats(0).build());

        // La La Land
        showService.createShow(Show.builder().movie(m5).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(11)).ticketPrice(230).availableSeats(0).build());

        // Forrest Gump
        showService.createShow(Show.builder().movie(m6).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(12)).ticketPrice(240).availableSeats(0).build());

        // Social Network
        showService.createShow(Show.builder().movie(m7).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(13)).ticketPrice(220).availableSeats(0).build());

        // Conjuring
        showService.createShow(Show.builder().movie(m8).theatre(t1)
                .showTime(LocalDateTime.now().plusHours(14)).ticketPrice(270).availableSeats(0).build());

        // KGF
        showService.createShow(Show.builder().movie(m9).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(15)).ticketPrice(300).availableSeats(0).build());

        // Bonus 2 more (to make exactly 20)
        showService.createShow(Show.builder().movie(m4).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(16)).ticketPrice(260).availableSeats(0).build());

        showService.createShow(Show.builder().movie(m8).theatre(t2)
                .showTime(LocalDateTime.now().plusHours(18)).ticketPrice(290).availableSeats(0).build());

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
