package com.sapu.movieflix.auth.services;

import com.sapu.movieflix.auth.entities.RefreshToken;
import com.sapu.movieflix.auth.entities.User;
import com.sapu.movieflix.auth.repositories.RefreshTokenRepository;
import com.sapu.movieflix.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Name not found with email : "+username));
        RefreshToken refreshToken = user.getRefreshToken();
        if(refreshToken==null) {
            long refreshTokenValidity = 600 * 1000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }else if(refreshToken.getExpirationTime().compareTo(Instant.now())<0){
            long refreshTokenValidity = 600 * 1000;
            refreshToken.setRefreshToken(UUID.randomUUID().toString());
            refreshToken.setExpirationTime(Instant.now().plusMillis(refreshTokenValidity));
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }
    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken  refToken =  refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("Refresh Token Not Found"));
        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token Expired");
        }else{
            return refToken;
        }
    }
}
