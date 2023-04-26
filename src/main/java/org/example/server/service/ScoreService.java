package org.example.server.service;
import org.example.entity.Score;
import org.example.exceptions.ScoreException;

import java.util.List;

public interface ScoreService {
    public void reset() throws ScoreException;
    public int insertScore(Score score);
    public List<Score> getBestScores(String game);
}
