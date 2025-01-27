package az.inci.bmsanbar.config;

import az.inci.bmsanbar.security.domain.ApiUser;
import az.inci.bmsanbar.security.domain.Role;
import az.inci.bmsanbar.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;

    @Value("${api.username}")
    private String apiUsername;

    @Value("${api.password}")
    private String apiPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            if (userRepository.findByUsername(apiUsername).isEmpty())
                userRepository.save(ApiUser.builder()
                        .username(apiUsername)
                        .role(Role.ADMIN)
                        .password(passwordEncoder().encode(apiPassword))
                        .build());
        };
    }
}
