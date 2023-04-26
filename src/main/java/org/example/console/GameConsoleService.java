package org.example.console;

import org.example.entity.Comment;
import org.example.entity.Rating;
import org.example.entity.Score;
import org.example.exceptions.CommentException;
import org.example.exceptions.RatingException;
import org.example.server.service.CommentService;
import org.example.server.service.RatingService;
import org.example.server.service.ScoreService;
import org.example.server.service.ScoreServiceJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.example.common.Constants.MINESWEEPER;
import static org.example.console.GameStudioConsole.userName;


@Component
public class GameConsoleService implements GameConsoleInterface {
    @Autowired
    CommentService commentService;
    @Autowired
    RatingService ratingService;
    @Autowired
    ScoreService scoreService;

    final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void inputComment(String game) throws CommentException {
        System.out.println("Input comment");
        String input;
        input = readLine();
        List<Comment> comments = null;
        Comment com = new Comment();
        com.setPlayer(userName);
        com.setGame(game);
        com.setComment(input);
        com.setCommentedOn(Timestamp.valueOf(LocalDateTime.now()));
        try {
            commentService.addComment(com);
        } catch (CommentException e) {
            return;
        }
        try {
            comments = commentService.getComments(game);
        } catch (CommentException e) {
            return;
        }
        for (Comment comment : comments) {
            System.out.println(comment);
        }
    }

    @Override
    public void setRating(String game) throws RatingException {

        System.out.println("Input rating for " + game + ": number from 0 to 5");
        int input;
        input = Integer.parseInt(Objects.requireNonNull(readLine()));

        if (input >= 0 && input <= 5) {
            Rating tempRating = new Rating();
            tempRating.setPlayer(userName);
            tempRating.setRating(input);
            tempRating.setGame(game);
            tempRating.setRatedOn(Timestamp.valueOf(LocalDateTime.now()));
            ratingService.setRating(tempRating);
        } else {
            System.out.println("Incorrect format");
            return;
        }
    }

    @Override
    public void getScore(String game) {
        try {
            List<Score> scoreList = scoreService.getBestScores(MINESWEEPER);
            for (Score sc : scoreList) {
                System.out.println(sc);
            }
        } catch(Exception e) {
            System.out.println("Couldn't get scores, check database connection.");
        }
    }

    @Override
    public void setScore(String game, long startMillis) {
        try {
            Score score = new Score();
            score.setPlayer(userName);
            score.setGame(game);
            score.setScore(ScoreServiceJDBC.getPlayingSeconds(startMillis));
            score.setPlayedOn(Timestamp.valueOf(LocalDateTime.now()));
            scoreService.insertScore(score);
        } catch (Exception e) {
            System.out.println("Couldn't save your score, check database connection.");
        }
    }
}

