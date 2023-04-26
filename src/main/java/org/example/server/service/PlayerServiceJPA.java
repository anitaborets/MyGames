package org.example.server.service;

import lombok.Data;
import org.example.entity.Player;
import org.example.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.console.GameStudioConsole.userName;

@Data
@Component
public class PlayerServiceJPA implements PlayerService {
    Logger LOGGER = Logger.getLogger(PlayerServiceJPA.class.getName());
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void addPlayer(Player player) {
        playerRepository.save(player);
        LOGGER.log(Level.INFO, "player was added succesfully");
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> playerList;
        playerList = playerRepository.findAll();
        return playerList;
    }

    @Override
    public Optional<Player> getPlayerById(Long id) {
        Optional<Player> optional = playerRepository.findById(id);
        Player player = null;
        if (optional.isPresent()) {
            player = optional.get();
        } else {
            LOGGER.log(Level.WARNING, "player not exists");
        }
        assert player != null;
        return Optional.of(player);
    }

    @Override
    public List<Player> getPlayerByName(String name) {
        List<Player> players = playerRepository.findByName(name);
        return players;
    }


    @Override
    public void reset() {
        playerRepository.deleteAll();
    }

    public void createPlayerIfNotExists(String email, String userNameFromInput) {
        if (email.isEmpty() && userNameFromInput.isEmpty()) {
            getPlayerByName(userName);
            if (!getPlayerByName(userName).isEmpty()) {
                return;
            }
        } else {
            Player player = new Player();
            player.setName(userName);
            player.setEmail(email);
            player.setUserName(userNameFromInput);
            addPlayer(player);
        }
    }
}
