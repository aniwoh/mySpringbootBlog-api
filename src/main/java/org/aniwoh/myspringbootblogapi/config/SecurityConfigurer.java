package org.aniwoh.myspringbootblogapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfigurer {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable) //关闭csrf认证
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/user/login","/user/register").permitAll()
                        .pathMatchers("/article/upload").hasRole("ADMIN")
                        .pathMatchers("/article/**").permitAll())
                .build();
    }

    @Bean
    public WebSessionManager webSessionManager() {
        return new DefaultWebSessionManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
