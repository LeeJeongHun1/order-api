package com.order.config;

import com.order.entity.Role;
import com.order.entity.User;
import com.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElse(null);

        if (user == null) return null;

        Set<SimpleGrantedAuthority> authorities = getAuthority(user);
        CustomUser customUser = new CustomUser();
        customUser.setUserId(user.getSeq());
        customUser.setEmail(user.getEmail());
        customUser.setUsername(user.getName());
        customUser.setLoginCount(user.getLoginCount());
        customUser.setLastLoginAt(user.getLastLoginAt().orElse(null));
        customUser.setCreateAt(user.getCreateAt());
        customUser.setAuthorities(authorities);
        customUser.setEnabled(true);
        customUser.setAccountNonExpired(true);
        customUser.setAccountNonLocked(true);
        customUser.setCredentialsNonExpired(true);
        customUser.setPassword(user.getPasswd());
        customUser.setVerified(true);

//        if (user.getSocialLogin() == null && user.getEmailLogin() == null)
//            return customUser;

//        if (user.getLoginType() == User.LoginType.Email && user.getEmailLogin() != null) {
//        }
//        else if (user.getLoginType() == User.LoginType.Social && user.getSocialLogin() != null) {
//            customUser.setSocialLoginProvider(user.getSocialLogin().getProvider());
//            customUser.setVerified(true);
//        }
//
//        // 휴면 계정 상태 업데이트
//        if (user.getIsDormant()) {
//            dormantWithdrawalService.unsuspendUser(user.getId());
//        }

        return customUser;
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.name().toUpperCase()));
        return authorities;
    }

}

