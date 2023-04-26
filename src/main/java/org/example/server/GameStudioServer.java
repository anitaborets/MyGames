package org.example.server;

import org.example.server.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan(basePackages = "org.example.*")
@EnableJpaRepositories("org.example.*")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class);
    }

    @Bean
    public CommentService commentService() {
        CommentServiceJPA commentService = new CommentServiceJPA();
        return commentService;
    }

    @Bean
    public ScoreService scoreService() {
        ScoreService scoreService = new ScoreServiceJPA();
        return scoreService;
    }

    @Bean
    public RatingService ratingService() {
       RatingService ratingService = new RatingServiceJPA();
        return ratingService;
    }

    @Bean
    public RestTemplate restTemplate (){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

}
