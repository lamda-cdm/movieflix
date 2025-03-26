package com.sapu.movieflix.service;

import com.sapu.movieflix.dto.MovieDto;
import com.sapu.movieflix.dto.MoviePageResponse;
import com.sapu.movieflix.exceptions.MovieDoesntExist;
import com.sapu.movieflix.model.Movie;
import com.sapu.movieflix.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieServiceImpl implements MovieService{
    private final MovieRepository movieRepository;
    private final FileService fileService;
    @Value("${project.poster}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }
    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        String fileName = fileService.uploadFile(path, file);
        movieDto.setPoster(fileName);
        Movie movie = new Movie();
        movie.setMovieCast(movieDto.getMovieCast());
        movie.setDirector(movieDto.getDirector());
        movie.setStudio(movieDto.getStudio());
        movie.setPoster(movieDto.getPoster());
        movie.setTitle(movieDto.getTitle());
        movie.setReleaseYear(movieDto.getReleaseYear());
        Movie savedMovie = movieRepository.save(movie);
        String posterUrl = (baseUrl+"/file/"+fileName);
        return new MovieDto(savedMovie.getMovieId(), savedMovie.getTitle(),
                savedMovie.getDirector(),savedMovie.getStudio(),savedMovie.getMovieCast(),savedMovie.getReleaseYear()
        ,savedMovie.getPoster(),posterUrl);
    }

    @Override
    public Optional<MovieDto> getMovie(Integer id) throws RuntimeException{
        if(!movieRepository.existsById(id)){
            throw new MovieDoesntExist("No movie Found");
        }
        Movie movie = movieRepository.getReferenceById(id);
        String posterUrl = (baseUrl+"/file/"+movie.getPoster());
        return Optional.of(new MovieDto(movie.getMovieId(), movie.getTitle(),
                movie.getDirector(),movie.getStudio(),movie.getMovieCast(),movie.getReleaseYear()
                ,movie.getPoster(),posterUrl));
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> moviesList = movieRepository.findAll();
        List<MovieDto>response = new ArrayList<>();
        for (Movie movie:moviesList){
            String posterUrl = (baseUrl+"/file/"+movie.getPoster());
            MovieDto movieDto = new MovieDto(movie.getMovieId(), movie.getTitle(),
                    movie.getDirector(),movie.getStudio(),movie.getMovieCast(),movie.getReleaseYear()
                    ,movie.getPoster(),posterUrl);
            response.add(movieDto);
        }
        return response;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws RuntimeException, IOException {
        if(!movieRepository.existsById(movieId)){
            throw new MovieDoesntExist("No movie is present");
        }
        Movie mv = movieRepository.findById(movieId).get();
        movieDto.setMovieId(mv.getMovieId());
        String fileName = mv.getPoster();
        if(file!=null) {
            Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));
            fileName = fileService.uploadFile(path, file);
        }
        mv.setPoster(fileName);
        movieDto.setPoster(fileName);
        movieDto.setPosterUrl(baseUrl + "/file/" + fileName);
        mv.setMovieCast(movieDto.getMovieCast());
        mv.setDirector(movieDto.getDirector());
        mv.setStudio(movieDto.getStudio());
        mv.setTitle(movieDto.getTitle());
        mv.setReleaseYear(movieDto.getReleaseYear());
        movieRepository.save(mv);
        return movieDto;
    }

    @Override
    public Optional<String> deleteMovie(Integer movieId) throws IOException,RuntimeException {
        Optional<Movie> check = movieRepository.findById(movieId);
        if (check.isEmpty()){
            throw new MovieDoesntExist("No movie of this Id");
        }
        Movie mv = check.get();
        if(mv.getPoster()!=null && Files.exists(Paths.get(path+File.separator+mv.getPoster()))){
            Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));
        }
        movieRepository.delete(mv);
        return Optional.of("SUCCESSFULLY DELETED WITH ID "+mv.getMovieId());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDto>response = new ArrayList<>();
        for (Movie movie:movies){
            String posterUrl = (baseUrl+"/file/"+movie.getPoster());
            MovieDto movieDto = new MovieDto(movie.getMovieId(), movie.getTitle(),
                    movie.getDirector(),movie.getStudio(),movie.getMovieCast(),movie.getReleaseYear()
                    ,movie.getPoster(),posterUrl);
            response.add(movieDto);
        }
        return new MoviePageResponse(response,pageNumber,pageSize, (int) moviePages.getTotalElements(),
                (int) moviePages.getTotalPages(),moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                                                                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDto>response = new ArrayList<>();
        for (Movie movie:movies){
            String posterUrl = (baseUrl+"/file/"+movie.getPoster());
            MovieDto movieDto = new MovieDto(movie.getMovieId(), movie.getTitle(),
                    movie.getDirector(),movie.getStudio(),movie.getMovieCast(),movie.getReleaseYear()
                    ,movie.getPoster(),posterUrl);
            response.add(movieDto);
        }
        return new MoviePageResponse(response,pageNumber,pageSize, (int) moviePages.getTotalElements(),
                (int) moviePages.getTotalPages(),moviePages.isLast());
    }
}
