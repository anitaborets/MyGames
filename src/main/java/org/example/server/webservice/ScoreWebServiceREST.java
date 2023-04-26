package org.example.server.webservice;

import org.example.entity.Score;
import org.example.exceptions.ScoreException;
import org.example.repository.ScoreRepository;
import org.example.server.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.common.Constants.MINESWEEPER;

@RestController
@RequestMapping("api/score")
@EnableJpaRepositories
public class ScoreWebServiceREST {

    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoreRepository scoreRepository;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) throws ScoreException {
        return scoreService.getBestScores(MINESWEEPER);
    }

    @PostMapping
    public void addScore(@RequestBody Score score) throws ScoreException {
        scoreService.insertScore(score);
    }

    @DeleteMapping
    public void deleteAll() throws ScoreException {
        //len ako priklad
        //scoreService.reset();
    }


}
