package az.inci.bmsanbar.security.config;

import az.inci.bmsanbar.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry ->
                        registry.requestMatchers(
                                        "/v4/authenticate",
                                        "/v4/app-version/**",
                                        "/download",
                                        "/swagger/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v2/download",
                                        "/v3/download",
                                        "/v3/api-docs*/**",
                                        "/error").permitAll()
                                .requestMatchers("/v4/**").authenticated()
                                .anyRequest().denyAll())
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
