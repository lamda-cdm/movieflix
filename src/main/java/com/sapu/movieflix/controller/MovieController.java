package com.sapu.movieflix.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapu.movieflix.dto.MovieDto;
import com.sapu.movieflix.dto.MoviePageResponse;
import com.sapu.movieflix.exceptions.EmptyFileException;
import com.sapu.movieflix.model.Movie;
import com.sapu.movieflix.service.MovieService;
import com.sapu.movieflix.utils.AppConstants;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart String req_string, @RequestPart MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDto movieDto = objectMapper.readValue(req_string, MovieDto.class);
        if(file.isEmpty()){
            throw new EmptyFileException("PLEASE UPLOAD THE Fucking FILE");
        }
        return new ResponseEntity<>(movieService.addMovie(movieDto,file), HttpStatus.CREATED);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<MovieDto>> getMovies(){
        return new ResponseEntity<>(movieService.getAllMovies(),HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getMovie(@PathVariable Integer id){
        Optional<MovieDto> res = movieService.getMovie(id);
        if(res.isEmpty()){
            return new ResponseEntity<>("NO MOVIE IS PRESENT OF THIS ID",HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(res.get(),HttpStatus.OK);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Integer id , @RequestPart String req_string,@RequestPart MultipartFile file) throws IOException {
        ObjectMapper om = new ObjectMapper();
        MovieDto movieDto = om.readValue(req_string,MovieDto.class);
        movieDto.setMovieId(id);
        if(file.isEmpty()){
            throw new EmptyFileException("FILE IS EMPTY");
        }
        return new ResponseEntity<>(movieService.updateMovie(id,movieDto,file),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer id) throws IOException {
        Optional<String> response = movieService.deleteMovie(id);
        if(response.isEmpty()){
            return new ResponseEntity<>("NO SUCH MOVIE PRESENT",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response.get(),HttpStatus.OK);
    }
    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                     @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize){
        return new ResponseEntity<>(movieService.getAllMoviesWithPagination(pageNumber,pageSize),HttpStatus.OK);
    }
    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationSort(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                         @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                         @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
                                                                         @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String dir){
        return new ResponseEntity<>(movieService.getAllMoviesPaginationAndSorting(pageNumber,pageSize,sortBy,dir),HttpStatus.OK);
    }
}
