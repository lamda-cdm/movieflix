package com.sapu.movieflix.controller;

import com.sapu.movieflix.auth.entities.ForgotPassword;
import com.sapu.movieflix.auth.entities.User;
import com.sapu.movieflix.auth.services.EmailService;
import com.sapu.movieflix.auth.services.ForgotPasswordService;
import com.sapu.movieflix.auth.services.UserService;
import com.sapu.movieflix.auth.utils.ChangePassword;
import com.sapu.movieflix.dto.MailBody;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialNotFoundException;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/forgot")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final UserService userService;
    private final EmailService emailService;
    private final ForgotPasswordService forgotPasswordService;
    private final PasswordEncoder passwordEncoder;
    private Integer generateOtp(){
        Integer randomNumber = (int) (Math.random() * 900000) + 100000;
        return randomNumber;
    }
    @PostMapping("/verify/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user = userService.findUserByUserName(email).orElseThrow(()->{
            return new UsernameNotFoundException("No User found for this Email Address");
        });
        Integer otp = generateOtp();
        MailBody mailBody = new MailBody(user.getEmail(),"OTP FOR RESETTING PASSWORD",
                "Your Secret OTP IS FOR RESETTING PASSWORD IS "+otp+"\n Please don't share it with anyone");
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .user(user)
                .isChecked(false)
                .expirationTime(new Date(System.currentTimeMillis() + 300 * 1000))
                .build();
        if(!forgotPasswordService.findByUser(user).isEmpty()) {
            forgotPassword = forgotPasswordService.findByUser(user).get();
            forgotPassword.setOtp(otp);
            forgotPassword.setChecked(false);
            forgotPassword.setExpirationTime(new Date(System.currentTimeMillis() + 70 * 1000));
        }
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordService.saveForgotPassWord(forgotPassword);
        return ResponseEntity.ok("EMAIL SENT FOR VERIFICATION WITH OTP--"+otp);
    }
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp,@PathVariable String email) {
        User user = userService.findUserByUserName(email).orElseThrow(()->{
            return new UsernameNotFoundException("No User found for this Email Address");
        });
        ForgotPassword forgotPassword = forgotPasswordService.findByOtpAndUser(otp,user).orElseThrow(()->{
            return new RuntimeException("WRONG OTP Provided for -- "+email);
        });
        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordService.deleteById(forgotPassword.getFpid());
            return new ResponseEntity<>("OTP Has Expired", HttpStatus.EXPECTATION_FAILED);
        }
        forgotPassword.setChecked(true);
        forgotPasswordService.saveForgotPassWord(forgotPassword);
        return new ResponseEntity<>("OTP Verified Successfully",HttpStatus.OK);
    }
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@PathVariable String email, @RequestBody ChangePassword changePassword){
        ForgotPassword forgotPassword = forgotPasswordService.findByUser(userService.findUserByUserName(email).orElseThrow(()-> new RuntimeException("Email doesn't exist"))).get();
        if(!forgotPassword.isChecked()){
            return ResponseEntity.ok("SORRY WE COULDN'T AUTHENTICATE YOU!!");
        }
        forgotPassword.setChecked(false);
        if(!changePassword.password().equals(changePassword.repeatPassword())){
            return new ResponseEntity<>("The Re-Typed password is different",HttpStatus.OK);
        }
        String encodedPassword = passwordEncoder.encode(changePassword.repeatPassword());
        userService.updatePassword(email,encodedPassword);
        return ResponseEntity.ok("PASSWORD HAS BEEN RESET");
    }
}
