package org.example;

import org.example.console.GameConsoleService;
import org.example.console.GameStudioConsole;
import org.example.server.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class GameStudioClient {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioClient.class, args);
        //new SpringApplicationBuilder(GameStudioClient.class)
        //		.web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runnerSimple() {
        return args -> {
            System.out.println("SpringClient: Hello in GAME STUDIO");
        };
    }


    @Bean
    public CommandLineRunner runnerMines(GameStudioConsole gameStudioConsole, GameConsoleService gameConsoleService) {
        return args -> {
            gameStudioConsole.play();
        };
    }
}
