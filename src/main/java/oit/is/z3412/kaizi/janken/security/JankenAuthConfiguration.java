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
            .requestMatchers("/janken/**").authenticated()
            .anyRequest().permitAll())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/janken/**"));
    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
    // 実行環境のエンコーダで確実に「isdev」をハッシュ化して登録
    String hashedPass = encoder.encode("isdev");

    UserDetails user1 = User.withUsername("user1")
        .password(hashedPass)
        .roles("USER")
        .build();

    UserDetails user2 = User.withUsername("user2")
        .password(hashedPass)
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(user1, user2);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
