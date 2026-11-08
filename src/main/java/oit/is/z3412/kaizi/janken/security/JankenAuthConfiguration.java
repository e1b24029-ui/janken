package oit.is.z3412.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .defaultSuccessUrl("/janken", true));
    http.logout(logout -> logout
        .logoutSuccessUrl("/"));
    http.authorizeHttpRequests(authz -> authz
        .requestMatchers("/", "/h2-console/**").permitAll()
        .anyRequest().authenticated());
    http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
    http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    UserDetails user1 = User.withUsername("user1")
        .password("{noop}isdev")
        .roles("USER")
        .build();

    UserDetails user2 = User.withUsername("ほんだ")
        .password("{noop}isdev")
        .roles("USER")
        .build();

    UserDetails user3 = User.withUsername("いがき")
        .password("{noop}isdev")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(user1, user2, user3);
  }
}
