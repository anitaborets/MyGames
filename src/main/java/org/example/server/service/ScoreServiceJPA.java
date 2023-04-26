package org.example.server.service;

import org.example.entity.Score;
import org.example.exceptions.ScoreException;
import org.example.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreServiceJPA implements ScoreService {
    @Autowired
    ScoreRepository scoreRepository;
    Logger LOGGER = Logger.getLogger(ScoreServiceJPA.class.getName());

    @Override
    public int insertScore(Score score) {
        scoreRepository.save(score);
        LOGGER.log(Level.INFO, "score was added");
        return 1;
    }

    @Override
    public List<Score> getBestScores(String game) {
        List<Score> scores = scoreRepository.getTopByScore(game);
        for (Score s : scores) {
            System.out.println(s);
        }
        return scores;
    }

    @Override
    public void reset() throws ScoreException {
        scoreRepository.deleteAll();

    }
}
