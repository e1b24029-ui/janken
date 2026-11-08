package oit.is.z3412.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/"))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/janken/**", "/match/**", "/fight/**", "/wait/**").authenticated()
            .anyRequest().permitAll())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()));
    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
    String hashedPass = encoder.encode("isdev");

    UserDetails honda = User.withUsername("ほんだ")
        .password(hashedPass)
        .roles("USER")
        .build();

    UserDetails igaki = User.withUsername("いがき")
        .password(hashedPass)
        .roles("USER")
        .build();

    UserDetails user1 = User.withUsername("user1")
        .password(hashedPass)
        .roles("USER")
        .build();

    UserDetails user2 = User.withUsername("user2")
        .password(hashedPass)
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(honda, igaki, user1, user2);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
