package org.example.repository;

import org.example.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<Rating,Long> {
    @Override
    List<Rating> findAll();
    List<Rating> findFirstByPlayerAndGame(String player,String game);

    @Query(value = "SELECT AVG(rating) FROM rating WHERE game=?", nativeQuery = true)
    Double getRatingAverage(String game);

}
