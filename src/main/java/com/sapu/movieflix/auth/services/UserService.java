package com.sapu.movieflix.auth.services;

import com.sapu.movieflix.auth.entities.User;
import com.sapu.movieflix.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Optional<User> findUserByUserName(String email){
        return userRepository.findByEmail(email);
    }
    public void updatePassword(String email,String password){
        userRepository.updatePassword(email,password);
    }
}
