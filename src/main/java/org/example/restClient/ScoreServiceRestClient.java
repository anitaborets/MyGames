package org.example.restClient;

import org.example.entity.Score;
import org.example.exceptions.ScoreException;
import org.example.server.service.ScoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ScoreServiceRestClient implements ScoreService {
    @Value("${remote.server.api}")
    private String URL;

    private RestTemplate restTemplate;

    @Override
    public int insertScore(Score score) {
        restTemplate.postForEntity(URL, score, Score.class);
        return 1;
    }

    @Override
    public List<Score> getBestScores(String game) {
        return Arrays.asList(restTemplate.getForEntity(URL + "/" + game, Score[].class).getBody());
    }

    @Override
    public void reset() throws ScoreException {
        throw new ScoreException("This operation is not supported");
    }
}
