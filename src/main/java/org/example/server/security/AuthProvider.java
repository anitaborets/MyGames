package org.example.server.security;

import org.example.server.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
 public class AuthProvider implements AuthenticationProvider {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();

        UserDetails userDet = (UserDetails) userDetailsService.loadUserByUsername(userName);
        String password = authentication.getCredentials().toString();
        if(!password.equals(userDet.getPassword()))
            throw new BadCredentialsException("Incorrect password");
        return new UsernamePasswordAuthenticationToken(userDet,password, Collections.EMPTY_LIST);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
