package org.example.server.service;

import lombok.Data;
import org.example.entity.Rating;
import org.example.exceptions.RatingException;
import org.example.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public class RatingServiceJPA implements RatingService {

    @Autowired
    RatingRepository ratingRepository;
    Logger LOGGER = Logger.getLogger(RatingServiceJPA.class.getName());

    //we want to get ID from getRatinf.
    // getRating return rating not id becouse it is conclution of interface RatingService
    Rating ratingIfExist = null;

    @Override
    public int setRating(Rating rating) throws RatingException {
        System.out.println(rating.getGame());
        int value = getRating(rating.getGame(), rating.getPlayer());
        if (value != -1) {
            rating.setId(ratingIfExist.getId());
            ratingRepository.save(rating);
            return 0;
        } else {
            ratingRepository.save(rating);
            return 1;
        }
    }

    @Override
    public double getAverageRating(String game) throws RatingException {
        double rating = -1;
        rating = ratingRepository.getRatingAverage(game);
        System.out.println(rating);
        return rating;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating = -1;
        List<Rating> ratingList = ratingRepository.findFirstByPlayerAndGame(player, game);
        if (!ratingList.isEmpty()) {
            LOGGER.log(Level.INFO, "Rating already exists");
            Rating ratingFromDB = ratingList.get(0);
            ratingIfExist = ratingFromDB;
            rating = ratingFromDB.getRating();
        }
        return rating;
    }

    @Override
    public void reset() throws RatingException {
        ratingRepository.deleteAll();
    }
}
