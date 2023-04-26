package org.example.server.webcontroller;

import org.example.entity.Rating;
import org.example.entity.Score;
import org.example.exceptions.RatingException;
import org.example.exceptions.ScoreException;
import org.example.repository.RatingRepository;
import org.example.repository.ScoreRepository;
import org.example.server.EntityDTO.RatingDTO;
import org.example.server.service.RatingService;
import org.example.server.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.common.Constants.*;
import static org.example.console.GameStudioConsole.userName;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class StatisticsWebController {
    @Autowired
    ScoreService scoreService;

    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;

    //@RequestMapping("/comment")
    @GetMapping("/score")
    public String greeting(Model model) throws ScoreException, RatingException {
        model.addAttribute("score", new Score());
        List<Score> list = scoreService.getBestScores(MINESWEEPER);
        model.addAttribute("list", list);
        List<Score> listPuzzle = scoreService.getBestScores(PUZZLE_FIFTEEN);
        model.addAttribute("listPuzzle", listPuzzle);
        List<Score> listToes = scoreService.getBestScores(TIC_TAC_TOE);
        model.addAttribute("listToes", listToes);

        model.addAttribute("rating", new Rating());
        double ratingMines = ratingService.getAverageRating(MINESWEEPER);
        double ratingPuzzle = ratingService.getAverageRating(PUZZLE_FIFTEEN);
        double ratingToe = ratingService.getAverageRating(TIC_TAC_TOE);
        model.addAttribute("ratingMines", ratingMines);
        model.addAttribute("ratingPuzzle", ratingPuzzle);
        model.addAttribute("ratingToe", ratingToe);
        return "score.html";
    }

    @PostMapping(value = "/score/addrating")
    public String addComment(@ModelAttribute("rating") RatingDTO rating) throws RatingException {

        if (rating != null) {
            Rating ratingForSave = new Rating();
            if (rating.getGame().equals("MINESWEEPER")) {
                ratingForSave.setGame(MINESWEEPER);
            } else if (rating.getGame().equals("PUZZLE_FIFTEEN")) {
                ratingForSave.setGame(PUZZLE_FIFTEEN);
            } else if (rating.getGame().equals("TIC_TAC_TOE")) {
                ratingForSave.setGame(TIC_TAC_TOE);
            }
            ratingForSave.setRating(rating.getRating());
            ratingForSave.setPlayer(userName);
            ratingForSave.setRatedOn(Timestamp.valueOf(LocalDateTime.now()));
            ratingService.setRating(ratingForSave);
        }
        return "redirect:/score";
    }
}
