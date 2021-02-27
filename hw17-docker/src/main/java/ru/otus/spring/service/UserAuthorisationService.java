package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthorisationService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userService.getByLogin(login)
                .map(u -> User.withUsername(u.getName())
                        .password(u.getPassword())
                        .authorities(u.getLogin().equals("admin") ? "ADMIN" : "USER")
                        .accountExpired(false)
                        .credentialsExpired(false)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
    }
}
