package com.sapu.movieflix.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private Integer movieId;
    @NotBlank(message = "Please provide movie's Title")
    private String title;
    @NotBlank(message = "Please provide movie's Director")
    private String director;
    @NotBlank(message = "Please provide movie's Studio")
    private String studio;
    private Set<String> movieCast;
    @NotBlank(message = "Please provide movie's releaseYear")
    private Integer releaseYear;
    @NotBlank(message = "Please provide movie's poster")
    private String poster;
    @NotBlank(message = "Please provide movie's posterUrl")
    private String posterUrl;
}
