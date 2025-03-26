package com.sapu.movieflix.auth.repositories;

import com.sapu.movieflix.auth.entities.ForgotPassword;
import com.sapu.movieflix.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {
    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
    @Query("select fp from ForgotPassword fp where fp.user=?1")
    Optional<ForgotPassword> findByUser(User user);
}
