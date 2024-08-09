package org.aniwoh.myspringbootblogapi.service.impl;

import jakarta.annotation.Resource;
import org.aniwoh.myspringbootblogapi.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

    @Resource
    private AccountRepository accountRepository; // 假设你有一个UserRepository来操作数据库



    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return accountRepository.findAccountByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .map(account -> User.builder()
                        .username(account.getUsername())
                        .password(account.getPassword())
                        .roles(mapLevelToRole(account.getLevel()))
                        .build());
    }

    private String mapLevelToRole(Integer level) {
        if (level == 0) {
            return "GUEST";
        } else if (level >= 7) {
            return "ADMIN";
        } else {
            return "USER";
        }
    }
}
