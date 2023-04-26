package org.example.server.webcontroller;

import lombok.extern.slf4j.Slf4j;
import org.example.puzzle.PuzzleFifteenLogic;
import org.example.server.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static org.example.common.Constants.WIN;
import static org.example.puzzle.FieldInit.baseArrayInit;
import static org.example.puzzle.PuzzleFieldInit.*;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/puzzle")
@Slf4j
public class PuzzleController {
    private final PuzzleFifteenLogic puzzleFifteenLogic = new PuzzleFifteenLogic();
    static int size = 4;
    static int[][] grid;
    List<Integer> winCombination = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0);
    private String gameResult = WIN;
    public boolean checkWin = false;
    public int countOfMove = 0;
    private static final String SAVED_GAME = System.getProperty("user.home") + System.getProperty("file.separator") + "saved.game";
    public boolean isSaved = false;
    public boolean isLoaded = false;

    @RequestMapping
    public String puzzle(@RequestParam(required = false) Integer index) {
        isSaved = false;
        isLoaded = false;
        startOrUpdateGame(index);
        return "puzzle";
    }

    @RequestMapping("/new")
    public String newGame() {
        isSaved = false;
        isLoaded = false;
        startNewGame();
        return "redirect:/puzzle";
    }

    @RequestMapping("/save")
    public String saveGame() {
        isSaved = false;
        isLoaded = false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(SAVED_GAME);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(grid);
            objectOutputStream.writeObject(countOfMove);
            isSaved = true;
        } catch (IOException e) {
            isSaved = false;
            log.error("Game was not saved" + e.getMessage());
        }
        return "redirect:/puzzle";
    }

    @RequestMapping("/load")
    public String getSavedGame() throws IOException {

        isSaved = false;
        isLoaded = false;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVED_GAME))) {
            grid = (int[][]) ois.readObject();
            isLoaded = true;
            countOfMove = (int) ois.readObject();
            ;
        } catch (ClassNotFoundException e) {
            log.error("Saved game not exists" + e.getMessage());
            isLoaded = false;
            startNewGame();
        }
        return "redirect:/puzzle";

    }

    public String getPuzzleField() {
        if (grid == null) {
            startNewGame();
        }

        int index = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='puzzle'> \n");

        for (int row = 0; row < size; row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < size; column++) {
                sb.append("<td>\n");
                sb.append("<a href='/puzzle?index=" + index + "'>\n");
                index++;
                if (grid[row][column] == 0) {
                    sb.append("<span>" + " " + "</span>");
                } else {
                    sb.append("<span>" + grid[row][column] + "</span>");
                }
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    void startNewGame() {
        grid = new int[size][size];
        countOfMove = 0;

        baseArrayWebInit(grid);
        do {
            switchRandomTiles(grid);
        }
        while (isSolved(grid));
    }

    void startOrUpdateGame(Integer index) {
        int temp = 0, listIndex = 0;
        boolean isMove = false;
        if (grid == null) {
            startNewGame();
        }
        //if parameter was input
        if (index != null) {
            List<Integer> list = Arrays.stream(grid)
                    .flatMap(row -> Arrays.stream(row).boxed())
                    .collect(Collectors.toList());
            if (!Objects.equals(list, winCombination)) {
                //moveleft
                if ((index - 1) >= 0 && (index - 1) <= 15) {
                    if (list.get(index - 1) == 0) {
                        if (puzzleFifteenLogic.isCanMoveLeft(index)) {
                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index - 1, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }
                //moveright
                if ((index + 1) >= 0 && (index + 1) <= 15) {
                    if (list.get(index + 1) == 0) {
                        if (puzzleFifteenLogic.isCanMoveRight(index)) {
                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index + 1, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }
                //movedown
                if ((index + 4) >= 0 && (index + 4) <= 15) {
                    if (list.get(index + 4) == 0) {
                        if (puzzleFifteenLogic.isCanMoveDown(index)) {
                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index + 4, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }
                //moveup
                if ((index - 4) >= 0 && (index - 4) <= 15) {
                    if (list.get(index - 4) == 0) {
                        if (puzzleFifteenLogic.isCanMoveUp(index)) {
                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index - 4, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }

            }

            if (isMove) {
                countOfMove++;
                for (int r = 0; r < 4; r++) {
                    for (int c = 0; c < 4; c++) {
                        grid[r][c] = list.get(listIndex);
                        listIndex++;
                    }
                }
            }

            if (Objects.equals(list, winCombination)) {
                setGameResult(WIN);
                checkWin = true;
            }
        }
    }


    public int getCountOfMove() {
        return countOfMove;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }
}


