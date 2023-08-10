package com.order.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Override
  public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(annotation.name());

    UsernamePasswordAuthenticationToken usernamePasswordAuthxToken =
            new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    createAuthorityList(annotation.role())
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