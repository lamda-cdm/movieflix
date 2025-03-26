package com.sapu.movieflix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer movieId;
    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please provide movie's Title")
    private String title;
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's Director")
    private String director;
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's Studio")
    private String studio;
    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;
    @Column(nullable = false)
    private Integer releaseYear;
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's poster")
    private String poster;

}
