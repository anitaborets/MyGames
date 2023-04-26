package org.example.server.config;


import org.example.server.security.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@ComponentScan(value = "org.example.*")
public class WebSecurityConfig {

    private final AuthProvider authProvider;

    @Autowired
    public WebSecurityConfig(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/comment").authenticated()
                        .anyRequest().permitAll())
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/");

        return http.build();
    }

//user-in-memory - for development,without DB

    //  @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
    //       UserDetails user =
    //             org.springframework.security.core.userdetails.User.builder()
    //     .username("user")
    //      .password("{noop}user")
    //       .roles("USER")
    //       .build();

    //   return new InMemoryUserDetailsManager(user);
    //  }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

}

