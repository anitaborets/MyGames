package org.example.repository;

import org.example.entity.Score;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends CrudRepository<Score,Long> {

    @Override
    List<Score> findAll();

    @Query(value = "SELECT * FROM score WHERE game=? ORDER BY score desc LIMIT 10", nativeQuery = true)
    List<Score> getTopByScore(String game);
}
