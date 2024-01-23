package ru.hldspm.web.security.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.hldspm.web.entities.ApiToken;
import ru.hldspm.web.repository.ApiTokensRepository;
import ru.hldspm.web.repository.UserRepository;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiTokensRepository apiTokensRepository;

    @Bean
    public JdbcUserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        return userDetailsManager;
    }


    @Bean
    public UserDetailsService userDetailsService(JdbcUserDetailsManager userDetailsManager) {
        if (userRepository.findByUsername("admin") == null){
            userDetailsManager.createUser(User.withUsername("admin")
                  .password(passwordEncoder().encode("password"))
                  .roles("ADMIN")
                  .build());
            ApiToken currentToken = new ApiToken();
            currentToken.setToken(UUID.randomUUID().toString());
            currentToken.setUploader(userRepository.findByUsername("admin"));
            apiTokensRepository.save(currentToken);

            System.out.println("[HLDS PM] Administrator created. Default credentials: login=admin, password=password");
            System.out.println("[HLDS PM] We recommend you to change it on  /change-password");
        }
        return userDetailsManager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsManager userDetailsManager) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsManager);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeHttpRequests()
                .requestMatchers("/login")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()

                    .and()
                        .logout()
                            .invalidateHttpSession(true)
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login");
        return http.build();
    }

}
