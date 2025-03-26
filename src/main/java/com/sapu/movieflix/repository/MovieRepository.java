package com.sapu.movieflix.repository;

import com.sapu.movieflix.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {
    @Query("SELECT movieId from Movie ")
    public List<Integer> fun();
}
