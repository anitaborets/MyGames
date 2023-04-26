package org.example.minesweeper.consoleui;

import org.example.common.Constants;
import org.example.console.GameConsoleService;
import org.example.exceptions.CommentException;
import org.example.exceptions.RatingException;
import org.example.exceptions.WrongFormatException;
import org.example.game.UserInterface;
import org.example.minesweeper.core.Field;
import org.example.minesweeper.core.Tile;
import org.example.server.service.CommentService;
import org.example.server.service.RatingService;
import org.example.server.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static org.example.common.Constants.MINESWEEPER;
import static org.example.common.GameState.SOLVED;


/**
 * Console user interface.
 */
@Component
public class ConsoleUI implements UserInterface {
    private Field field;
    public static long startMillis;
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    @Autowired
    CommentService commentService;
    @Autowired
    RatingService ratingService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    GameConsoleService gameConsoleService;


    @Override
    public void newGameStarted(Field field) {
        this.field = field;
        startMillis = System.currentTimeMillis();
        do {
            update();
            processInput();
            switch (field.getState()) {
                case FAILED -> {
                    update();
                    System.out.println(Constants.GAME_OVER);
                    gameConsoleService.getScore(MINESWEEPER);
                    System.exit(0);
                }
                case SOLVED -> {
                    System.out.println(Constants.WIN);
                    gameConsoleService.setScore(MINESWEEPER, startMillis);
                    gameConsoleService.getScore(MINESWEEPER);
                    System.exit(0);
                }
            }
        } while (true);
    }

    @Override
    public void update() {
        String closedTile = "\u2618";

        System.out.print("  ");
        System.out.println("Pocet min: " + field.getMineCount());
        System.out.println("  Ty si mysliš, že ešte ostava min: " + (field.getRemainingMineCount()));

        System.out.print("   ");
        for (int i = 0; i < field.getRowCount(); i++) {
            System.out.printf("%4s", i);
        }
        System.out.print("\n");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < field.getRowCount(); i++) {
            Character c = (char) (i + 65);
            System.out.print(c + "| ");
            for (int j = 0; j < field.getColumnCount(); j++) {
                if (field.getTiles()[i][j].getState().equals(Tile.State.MARKED)) {
                    System.out.printf("%3s", "M");
                    continue;
                }
                if (field.getTiles()[i][j].getState().equals(Tile.State.CLOSED)) {
                    System.out.printf("%4s", closedTile);
                    continue;

                } else {
                    System.out.printf("%3s", field.getTiles()[i][j]);
                    continue;
                }
            }
            System.out.print("| ");
            System.out.print("\n");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Please enter your selection: ").append("\n")
                .append("<EXIT> - EXIT").append("\n")
                .append("<MA1> - MARK A1").append("\n")
                .append("<OB4> - OPEN B4").append("\n")
                .append("<com> - input comment").append("\n")
                .append("<rat> - input rating of this game");
        System.out.println(builder);
    }

    @Override
    public void drawScene() {
    }


    private void processInput() {
        Scanner input = new Scanner(System.in);
        String choice = input.nextLine();

        try {
            handleInput(choice);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
            processInput();
        }

        if (choice.equalsIgnoreCase("EXIT")) {
            System.out.println("GOOD BY!");
            System.exit(0);
        }

        if (choice.equalsIgnoreCase("com")) {
            try {
                gameConsoleService.inputComment(MINESWEEPER);
                return;
            } catch (CommentException e) {
                processInput();
            }
        }

        if (choice.equalsIgnoreCase("rat")) {
            try {
                gameConsoleService.setRating(MINESWEEPER);
                System.out.println("rating " + ratingService.getAverageRating(MINESWEEPER));
                return;
            } catch (RatingException e) {
                return;
            }
        }
        char[] symbols = choice.toUpperCase(Locale.ROOT).toCharArray();
        char state = Character.toUpperCase(symbols[0]);
        int row = Character.getNumericValue(symbols[1]) - 10;
        int column = Integer.parseInt(String.valueOf(symbols[2]));

        switch (state) {
            case 'O' -> field.openTile(row, column);
            case 'M' -> field.markTile(row, column);
        }
    }

    private static void handleInput(String input) throws WrongFormatException {
        if (!isNull(input) && !input.isEmpty()) {
            Pattern pattern = Pattern.compile("([oOmM])([a-iA-I])([0-8])|[EXIT]|[exit]|[com]");
            Matcher matcher = pattern.matcher(input);

            if (!matcher.find()) {
                throw new WrongFormatException("Incorrect format");
            }
        }
    }

}


