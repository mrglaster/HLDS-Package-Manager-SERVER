package ru.hldspm.web.security.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import ru.hldspm.web.repository.UserRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserRepository userRepository;


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


    @Configuration
    public static class WebSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

        private final DaoAuthenticationProvider authenticationProvider;

        @Autowired
        public WebSecurityConfig(DaoAuthenticationProvider authenticationProvider) {
            this.authenticationProvider = authenticationProvider;

        }


        @Override
        public void configure(HttpSecurity http) throws Exception {

            http
                    .authenticationProvider(authenticationProvider)
                        .authorizeHttpRequests()
                        .requestMatchers("/",  "/change-password", "/content", "/add-content", "/logout").authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()

                    .and()
                        .logout()
                            .invalidateHttpSession(true)
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login");
        }

    }
}
