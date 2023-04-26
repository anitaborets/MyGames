package org.example.repository;

import org.example.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    List<Player> findByName(String name);

    @Override
    List<Player> findAll();
}
