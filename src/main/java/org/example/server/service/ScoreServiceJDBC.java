package org.example.server.service;

import com.zaxxer.hikari.HikariDataSource;
import org.example.common.Constants;
import org.example.entity.Score;
import org.example.exceptions.ScoreException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ScoreServiceJDBC implements ScoreService {
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS score (id INT PRIMARY KEY Generated Always as Identity, player VARCHAR(32) NOT NULL,  game VARCHAR(32) NOT NULL, score INT NOT NULL, playedOn TIMESTAMP)";
    private static final String GET_ALL = "SELECT * FROM score WHERE game LIKE ? ORDER BY score desc LIMIT 10";
    private static final String DELETE = "TRUNCATE score";
    private static final String INSERT = "INSERT INTO score (player,game,score,playedOn) VALUES (?, ?, ?, ?)";
    Connection con = null;
    PreparedStatement pst = null;
    HikariDataSource ds = HikariCPDataSource.getHikariDataSource();
    Logger LOGGER = Logger.getLogger(ScoreServiceJDBC.class.getName());

    public void createScoreTable() throws ScoreException {
        try (Connection con = DriverManager.getConnection(Constants.URL, Constants.USER, Constants.PASSWORD)) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(CREATE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.out.println(e.getMessage());
            throw new ScoreException(e.getMessage());
        }
    }

    public int insertScore(Score score) {
        int count = 0;

        try (Connection con = DriverManager.getConnection(Constants.URL, Constants.USER, Constants.PASSWORD)){
           // con = ds.getConnection();
            pst = con.prepareStatement(INSERT);
            pst.setString(1, score.getPlayer());
            pst.setString(2, score.getGame());
            pst.setInt(3, score.getScore());
            pst.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
            count = pst.executeUpdate();
            System.out.println(count);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return count;
    }

    public List<Score> getBestScores(String game) {
        List<Score> scores = new ArrayList<>();
        try {
            con = ds.getConnection();
            pst = con.prepareStatement(GET_ALL);
            ResultSet results = pst.executeQuery();
            while (results.next()) {
                Score score = new Score();
                score.setId(results.getInt(1));
                score.setPlayer(results.getString(2));
                score.setGame(results.getString(3));
                score.setScore(results.getInt(4));
                score.setPlayedOn(results.getTimestamp(5));
                scores.add(score);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return scores;
    }
@Override
    public void reset() throws ScoreException {
        try {
            con = ds.getConnection();
            Statement statement = con.createStatement();
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ScoreException(e.getMessage());
        }
    }

    public static int getPlayingSeconds(long startMillis) {
        return (int) ((System.currentTimeMillis() - startMillis) / 1000);
    }
}
