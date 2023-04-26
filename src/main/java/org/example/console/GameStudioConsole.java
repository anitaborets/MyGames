package org.example.console;

import org.example.exceptions.WrongFormatException;
import org.example.minesweeper.Minesweeper;
import org.example.puzzle.PuzzleFifteen;
import org.example.server.service.PlayerServiceJPA;
import org.example.server.service.RatingService;
import org.example.server.service.ScoreService;
import org.example.tiktaktoe.TicTacToe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;


@Component
public class GameStudioConsole {
    Properties properties = System.getProperties();
    public static final String userName = System.getProperty("user.name");
    @Autowired
    Minesweeper minesweeper;
    @Autowired
    PuzzleFifteen puzzleFifteen;

    @Autowired
    TicTacToe ticTacToe;
    @Autowired
    PlayerServiceJPA playerService;
    @Autowired
    RatingService ratingService;
    @Autowired
    ScoreService scoreService;

    public static final String GREETING = "Choice the game please: " + "\n" +
            "<1> - MINESWEEPER" + "\n" +
            "<2> - PUZZLE-FIFTEEN" + "\n" +
            "<3> - TIC-TAC-TOE" + "\n";

    public GameStudioConsole() {
    }

    public void play() {
        addPlayer();

        System.out.println(GREETING);
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();

        switch (choice) {
            case 1 -> minesweeper.play();
            case 2 -> puzzleFifteen.play();
            case 3 -> ticTacToe.play();
            default -> System.exit(0);
        }
    }

    public void addPlayer() {
        String email;
        System.out.println("Input your mail please if you want");
        try {
            email = inputEmail();
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Input your name if you want");
        Scanner input = new Scanner(System.in);
        String inputUserName = input.nextLine();

        playerService.createPlayerIfNotExists(email, inputUserName);

    }

    private String inputEmail() throws WrongFormatException {
        Scanner input = new Scanner(System.in);
        String inputUserMail = input.nextLine();
        if (!isNull(inputUserMail) && !inputUserMail.isEmpty()) {
            Pattern pattern = Pattern.compile("^(.+)@(\\S+)$");
            Matcher matcher = pattern.matcher(inputUserMail);

            if (!matcher.find()) {
                throw new WrongFormatException("Incorrect format");
            }
            properties.setProperty("user.email", inputUserMail);
        }
        return inputUserMail;
    }

}
