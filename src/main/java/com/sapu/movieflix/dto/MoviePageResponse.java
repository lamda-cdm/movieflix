package com.sapu.movieflix.dto;

import com.sapu.movieflix.model.Movie;

import java.util.List;

public record MoviePageResponse(List<MovieDto> movieDtos,Integer pageNumber,Integer pageSize,int totalElements,
                                int totalPages,boolean isLast) {

}
