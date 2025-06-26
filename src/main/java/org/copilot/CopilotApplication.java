package org.copilot;


import org.copilot.model.User;
import org.copilot.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CopilotApplication {
    public static void main(String[] args) {
        SpringApplication.run(CopilotApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UserService userService) {
        return args -> {
            userService.saveUser("Alice");
            userService.saveUser("Bob");
            userService.saveUser("Charlie");
        };
    }
}
