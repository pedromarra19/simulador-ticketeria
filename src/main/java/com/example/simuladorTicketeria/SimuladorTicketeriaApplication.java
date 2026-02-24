package com.example.simuladorTicketeria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SimuladorTicketeriaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SimuladorTicketeriaApplication.class, args);
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Permite qualquer origem (apenas para desenvolvimento!)
                        .allowedMethods("*") // Permite todos os métodos
                        .allowedHeaders("*") // Permite todos os headers
                        .allowCredentials(false) // Não pode ser true com allowedOrigins="*"
                        .maxAge(3600);
            }
        };
    }
}