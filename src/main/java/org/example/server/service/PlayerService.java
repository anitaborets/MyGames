package org.example.server.service;

import org.example.entity.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerService {
    void addPlayer(Player player);

    List<Player> getPlayers();

    Optional<Player> getPlayerById(Long id);

    List<Player> getPlayerByName(String name);

    void reset();
}
