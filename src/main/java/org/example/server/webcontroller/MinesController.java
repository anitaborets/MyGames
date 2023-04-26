
package org.example.server.webcontroller;

import org.example.common.GameState;
import org.example.entity.Score;
import org.example.minesweeper.Settings;
import org.example.minesweeper.core.Clue;
import org.example.minesweeper.core.Field;
import org.example.minesweeper.core.Tile;
import org.example.server.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static org.example.common.Constants.MINESWEEPER;
import static org.example.console.GameStudioConsole.userName;

@Controller
@RequestMapping("/mines")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesController {
    @Autowired
    private ScoreService scoreService;
    private Field field = null;
    private boolean marking = false;


    //parametre pre input from user - row and column
    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column) {
        startOrUpdateGame(row, column);
        return "mines";
    }

    //open or mark Tile
    @RequestMapping("/chmode")
    public String changeMode() {
        changeGameMode();
        return "mines";
    }

    @RequestMapping("/new")
    public String newGame() {
        startNewGame(Settings.BEGINNER);
        return "mines";
    }

    @RequestMapping("/beginner")
    public String newGameAsBeginner() {
        startNewGame(Settings.BEGINNER);
        return "mines";
    }

    @RequestMapping("/im")
    public String newGameAsIntermediate() {
        startNewGame(Settings.INTERMEDIATE);
        return "mines";
    }

    @RequestMapping("/ex")
    public String newGameAsExpert() {
        startNewGame(Settings.EXPERT);
        return "mines";
    }

    //java Script - async
    @RequestMapping("minesjs")
    public String loadInAsynchMode() {
        startOrUpdateGame(null, null);
        return "minesjs";
    }
    @RequestMapping("/miska")
    public String minesMiska(){return "mines-miska";}
    @RequestMapping(value = "minesjs/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field processUserInputJson(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column) {
        startOrUpdateGame(row, column);
        //posielame json na web
        return field;
    }

    private boolean startOrUpdateGame(Integer row, Integer column) {
        boolean justFinished = false;
        if (field == null) {
            startNewGame(Settings.BEGINNER);
        }
        //if parametres were input
        if (row != null && column != null) {
            //ulozime stav hry
            GameState stateBeforeMove = field.getState();
            if (stateBeforeMove == GameState.PLAYING) {
                if (marking) {
                    //oznacit
                    field.markTile(row, column);
                } else {
                    //otvorit
                    field.openTile(row, column);
                }
            }

            //aby sa nedalo score zapisat opakovane
            if (field.getState() == GameState.SOLVED && stateBeforeMove != field.getState()) {
                justFinished = true;
                Score score = new Score();
                score.setGame(MINESWEEPER);
                score.setPlayer(userName);

                //todo
                score.setScore(250);
                score.setPlayedOn(Timestamp.valueOf(LocalDateTime.now()));
                scoreService.insertScore(score);
            }
        }
        return justFinished;
    }

    private void startNewGame(Settings settings) {
        field = new Field(settings.getRowCount(), settings.getColumnCount(), settings.getMineCount());
        marking = false;
    }

    public boolean isMarking() {
        return marking;
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='minefield'> \n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                sb.append("<td class='" + getTileClass(tile) + "'>\n");
                if (field.getState() == GameState.PLAYING) {
                    sb.append("<a href='/mines?row=" + row + "&column=" + column + "'>\n");
                    sb.append("<span>" + getTileText(tile) + "</span>");
                    sb.append("</a>\n");
                } else {
                    sb.append("<span>" + getTileText(tile) + "</span>");
                }
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    public String getTileText(Tile tile) {
        switch (tile.getState()) {
            case CLOSED -> {
                return "-";
            }
            case MARKED -> {
                return "M";
            }
            case OPEN -> {
                if (tile instanceof Clue) {
                    return String.valueOf(((Clue) tile).getValue());
                } else {
                    return "X";
                }
            }
            default -> throw new IllegalArgumentException("Unsupported tile state " + tile.getState());
        }
    }

    public String getTileClass(Tile tile) {
        switch (tile.getState()) {
            case OPEN -> {
                if (tile instanceof Clue)
                    return "open" + ((Clue) tile).getValue();
                else
                    return "mine";
            }
            case CLOSED -> {
                return "closed";
            }
            case MARKED -> {
                return "marked";
            }
            default -> throw new RuntimeException("Unexpected tile state");
        }
    }

    private void changeGameMode() {
        if (field == null) {
            startNewGame(Settings.BEGINNER);
        }
        marking = !marking;
    }

    public String getCurrentTime() {
        return new Date().toString();
    }

    public String getCountOfMines() {
        String countOfMines = "I do not know";
        countOfMines = String.valueOf(field.getRemainingMineCount());
        return countOfMines;
    }

    //for field with Thymeleaf
    public GameState getFieldStateForHtml() {
        return field.getState();
    }

    public Tile[][] getFieldTiles() {
        if (field == null) {
            return null;
        } else {
            return field.getTiles();
        }
    }

    public boolean isPlaying() {
        if (field == null) {
            return false;
        } else {
            return (field.getState() == GameState.PLAYING);
        }
    }

}

