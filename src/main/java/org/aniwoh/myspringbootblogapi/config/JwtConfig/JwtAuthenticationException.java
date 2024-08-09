package org.aniwoh.myspringbootblogapi.config.JwtConfig;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    JwtAuthenticationException(String msg) {
        super(msg);
    }
}
