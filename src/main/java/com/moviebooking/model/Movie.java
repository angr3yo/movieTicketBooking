package com.moviebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    private String language;

    private int durationMinutes;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000)   // extended to hold inline SVG data URI
    private String posterUrl;

    private double rating;

    @Enumerated(EnumType.STRING)
    private MovieStatus status;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Show> shows;

    public enum MovieStatus {
        NOW_SHOWING, COMING_SOON, ENDED
    }
}
