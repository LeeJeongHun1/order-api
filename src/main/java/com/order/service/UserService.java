package com.order.service;

import com.order.security.CustomUser;
import com.order.security.CustomUserDetailsService;
import com.order.security.jwt.JwtProvider;
import com.order.dto.user.LoginRequest;
import com.order.dto.user.LoginResult;
import com.order.dto.user.UserDto;
import com.order.entity.User;
import com.order.exception.NotFoundException;
import com.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    public LoginResult login(LoginRequest loginRequest) {
        CustomUser customUser = customUserDetailsService.loadUserByUsername(loginRequest.getPrincipal());
        if (customUser == null) {
            throw new NotFoundException("Bad principal");
        }

        if (!passwordEncoder.matches(loginRequest.getCredentials(), customUser.getPassword())) {
            throw new IllegalArgumentException("Bad credential");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser.getEmail(), customUser.getPassword(), customUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User(
                customUser.getUserId(),
                customUser.getUsername(),
                customUser.getEmail(),
                customUser.getPassword(),
                customUser.getLoginCount(),
                customUser.getLastLoginAt(),
                customUser.getCreateAt());
        user.afterLoginSuccess();
        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(authentication);
        return new LoginResult(accessToken, user);
    }

    public UserDto getUser(Long userId) {
        return userRepository.findById(userId)
                .map(UserDto::new)
                .orElseThrow(() -> new NotFoundException("Could nof found user for " + userId));
    }
}
