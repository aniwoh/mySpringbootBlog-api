package org.aniwoh.myspringbootblogapi.config.JwtConfig;


import org.springframework.security.core.userdetails.UserDetails;

public interface TokenProvider {
    String generateToken(UserDetails userDetails);
}
