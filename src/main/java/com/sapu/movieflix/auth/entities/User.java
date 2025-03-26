package com.sapu.movieflix.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;
    @NotNull(message = "The Email field can't be blank")
    @Email(message = "Please enter in proper format")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "The name can't be blank")
    private String name;
    @NotBlank(message = "The UserName can't be blank")
    @Column(unique = true)
    private String userName;
    @NotBlank(message = "The password field cannot be blank")
    @Size(min = 3,message = "The password must have 3 characters")
    private String password;
    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
