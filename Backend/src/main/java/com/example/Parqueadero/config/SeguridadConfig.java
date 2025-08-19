package com.example.Parqueadero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain filtro(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Evitar error creando un UserDetailsManager vacío
    @Bean
    public UserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager();
    }
}

    
    
    
    
    
    
    
    

