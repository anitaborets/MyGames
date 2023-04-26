package org.example.game;

import org.example.minesweeper.core.Field;

import java.util.Random;

public interface UserInterface {
    void newGameStarted(Field field);

    void update();

    void drawScene();

    static int getRandomNumber(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }

}
