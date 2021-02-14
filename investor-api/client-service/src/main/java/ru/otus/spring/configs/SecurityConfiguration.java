package ru.otus.spring.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.spring.controllers.CustomAccessDeniedHandler;
import ru.otus.spring.dispatcher.UserAuthorisationService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserAuthorisationService userDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/index", "/login", "/sub").permitAll()
                .and()
                .authorizeRequests().antMatchers("/users/**", "/user/**").hasAuthority("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/author/**", "/authors/**", "/book/**", "/books/**").authenticated()
                .and()
                .formLogin()
                .and().sessionManagement().disable()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}