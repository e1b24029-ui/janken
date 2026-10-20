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
        .requestMatchers("/").permitAll()
        .anyRequest().authenticated());
    return http.build();
  }

  // ★ userDetailsService メソッドはクラス内に「1つだけ」にする
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

    // カンマ区切りで2人とも登録する
    return new InMemoryUserDetailsManager(user1, user2);
  }
}
