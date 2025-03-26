package com.sapu.movieflix.auth.services;

import com.sapu.movieflix.auth.entities.ForgotPassword;
import com.sapu.movieflix.auth.entities.User;
import com.sapu.movieflix.auth.repositories.ForgotPasswordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {
    private final ForgotPasswordRepository forgotPasswordRepository;
    public Integer saveForgotPassWord(ForgotPassword forgotPassword){
        forgotPasswordRepository.save(forgotPassword);
        return forgotPassword.getOtp();
    }
    public Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user){
        return forgotPasswordRepository.findByOtpAndUser(otp,user);
    }
    public void deleteById(Integer id){
        forgotPasswordRepository.deleteById(id);
    }
    public Optional<ForgotPassword> findByUser(User user){
        return forgotPasswordRepository.findByUser(user);
    }
}
