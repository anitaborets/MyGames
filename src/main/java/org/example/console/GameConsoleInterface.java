package org.example.console;

import org.example.exceptions.CommentException;
import org.example.exceptions.RatingException;
import org.example.exceptions.ScoreException;

public interface GameConsoleInterface {

    void inputComment(String game) throws CommentException;

    void setRating(String game) throws RatingException;

    void getScore(String game) throws ScoreException;
    void setScore(String game,long startMillis) throws ScoreException;

}
