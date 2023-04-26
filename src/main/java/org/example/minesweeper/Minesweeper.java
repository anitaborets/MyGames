package org.example.minesweeper;

import org.example.common.Constants;
import org.example.exceptions.RatingException;
import org.example.minesweeper.consoleui.ConsoleUI;
import org.example.minesweeper.core.Field;
import org.example.repository.CommentRepository;
import org.example.server.service.CommentService;
import org.example.server.service.RatingService;
import org.example.server.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.minesweeper.Settings.*;

@Component
public class Minesweeper {
    Settings settings;
    Logger LOGGER = Logger.getLogger(Minesweeper.class.getName());
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository repository;
    @Autowired
    private ConsoleUI userInterface;
    @Autowired
    RatingService ratingService;
    @Autowired
    ScoreService scoreService;

    public Minesweeper() {
    }

    public void play() {
        System.out.println("WELCOME TO MINESWEEPER");

       try {
            System.out.println("rating " + ratingService.getAverageRating(Constants.MINESWEEPER));
       } catch (RatingException e) {
            e.printStackTrace();
       }

        System.out.println("Select level of game");
        StringBuilder builder = new StringBuilder();
        builder.append("Please enter your selection: ").append("\n")
                .append("<B> - BEGINNER").append("\n")
                .append("<I> -INTERMEDIATE").append("\n")
                .append("<E> - EXPERT").append("\n");
        System.out.println(builder);
        Scanner input = new Scanner(System.in);
        String choice = input.nextLine();

        switch (choice) {
            case "B" -> settings = BEGINNER;
            case "I" -> settings = INTERMEDIATE;
            case "E" -> settings = EXPERT;
            default -> {
                try {
                    settings = Settings.load();
                } catch (IOException | ClassNotFoundException e) {
                    LOGGER.log(Level.WARNING, e.getMessage());
                    settings = BEGINNER;
                }
            }
        }
        try {
            settings.save();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        try {
            settings = Settings.load();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            settings = BEGINNER;
        }
        Field field = new Field(
                settings.getRowCount(),
                settings.getColumnCount(),
                settings.getMineCount());
        userInterface.newGameStarted(field);
    }
}
