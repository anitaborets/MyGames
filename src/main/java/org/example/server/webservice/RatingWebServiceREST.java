package org.example.server.webservice;

import org.example.entity.Rating;
import org.example.exceptions.RatingException;
import org.example.repository.RatingRepository;
import org.example.server.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/rating")
@EnableJpaRepositories
public class RatingWebServiceREST {

    @Autowired
    RatingService ratingService;
    @Autowired
    RatingRepository ratingRepository;

    @GetMapping("/{game}")
    public Double getAverageRating(@PathVariable String game) throws RatingException {
        return ratingService.getAverageRating(game);
    }

    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game, @PathVariable String player) throws RatingException {
        return ratingService.getRating(game, player);
    }

    @PostMapping
    public void setRating(@RequestBody Rating rating) throws RatingException {
        ratingService.setRating(rating);
    }

}
