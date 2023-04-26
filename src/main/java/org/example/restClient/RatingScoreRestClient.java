package org.example.restClient;

import org.example.entity.Rating;
import org.example.exceptions.RatingException;
import org.example.server.service.RatingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
public class RatingScoreRestClient implements RatingService {
    @Value("${remote.server.api}")
    private String URL;

    private RestTemplate restTemplate;

    @Override
    public int setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(URL + "/rating", rating, Rating.class);
        return 1;
    }

    @Override
    public double getAverageRating(String game) throws RatingException {
        return restTemplate.getForEntity(URL + "/rating/" + game, Double.class)
                .getBody();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return restTemplate.getForEntity(URL + "/rating/" + game + "/" + player, Integer.class)
                .getBody();
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}
