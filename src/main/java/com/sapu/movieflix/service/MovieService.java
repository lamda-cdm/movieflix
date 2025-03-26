package com.sapu.movieflix.service;

import com.sapu.movieflix.dto.MovieDto;
import com.sapu.movieflix.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;
    Optional<MovieDto> getMovie(Integer id);
    List<MovieDto> getAllMovies();
    MovieDto updateMovie(Integer movieId,MovieDto movieDto,MultipartFile file) throws IOException;
    Optional<String> deleteMovie(Integer movieId) throws IOException;

    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber,Integer pageSize);
    MoviePageResponse getAllMoviesPaginationAndSorting(Integer pageNumber,Integer pageSize,String sortBy,String dir);

}
