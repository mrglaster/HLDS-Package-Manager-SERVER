package ru.hldspm.web.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        return userDetailsManager;
    }


    @Bean
    public UserDetailsService userDetailsService(JdbcUserDetailsManager userDetailsManager) {
        try{
            UserDetails user = userDetailsManager.loadUserByUsername("admin");
        }
        catch(Exception e) {
            userDetailsManager.createUser(User.withUsername("admin")
                    .password(passwordEncoder().encode("root"))
                    .roles("ADMIN")
                    .build());
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
                    .requestMatchers("/", "/login", "/change-password").authenticated()
                    .anyRequest().permitAll()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
        }

    }
}
