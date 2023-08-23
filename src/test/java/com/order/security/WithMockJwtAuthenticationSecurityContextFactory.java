package com.order.security;

import com.order.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {

    //    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//    @Autowired
//    private JwtProvider jwtProvider;

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

//        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(annotation.name());
        CustomUser customUser = new CustomUser();
        customUser.setUserId(annotation.id());
        customUser.setEmail(annotation.name());
        customUser.setAuthorities(createAuthorityList(annotation.role()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                annotation.name(), "test!1", createAuthorityList(annotation.role())
        );

//        String accessToken = jwtProvider.createAccessToken(authentication);

        UsernamePasswordAuthenticationToken usernamePasswordAuthxToken =
                new UsernamePasswordAuthenticationToken(
                        customUser,
                        null,
                        ((UserDetails) customUser).getAuthorities()
                );
//    JwtAuthenticationToken authentication =
//      new JwtAuthenticationToken(
//        new JwtAuthentication(annotation.id(), annotation.name()),
//        null,

//      );
        context.setAuthentication(usernamePasswordAuthxToken);
        return context;
    }

}