package com.proyectoMaycollins.LlantasApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableScheduling
public class LlantasApiApplication {

    private static final Logger log = LoggerFactory.getLogger(LlantasApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LlantasApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner onStartup() {
        return args -> {
            log.info("Se ha iniciado correctamente el sistema.");
            System.out.println("Se ha iniciado correctamente el sistema.");
        };
    }
}
