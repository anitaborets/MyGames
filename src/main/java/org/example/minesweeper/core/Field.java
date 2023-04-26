package org.example.minesweeper.core;
import org.example.common.GameState;

import java.util.Arrays;
import java.util.Random;

public class Field {
    private final Tile[][] tiles;
    private final int rowCount;
    private final int columnCount;
    private final int mineCount;
    private GameState state = GameState.PLAYING;

    private boolean justFinished;

    public boolean isJustFinished() {
        return justFinished;
    }

    public void setJustFinished(boolean justFinished) {
        this.justFinished = justFinished;
    }

    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        generate();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Clue && ((Clue) tile).getValue() == 0) {
                openAdjacentTiles(row, column);
            }

            if (tile instanceof Mine) {
                tile.setState(Tile.State.OPEN);
                for (int r = 0; r < rowCount; r++) {
                    for (int c = 0; c < columnCount; c++) {
                        openTile(r, c);
                    }
                }
                state = GameState.FAILED;
                return;
            }

            if (isSolved()) {
                state = GameState.SOLVED;
                for (int r = 0; r < rowCount; r++) {
                    for (int c = 0; c < columnCount; c++) {
                        openTile(r, c);
                    }
                }
                return;
            }
        }
    }

    public void markTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.MARKED);
            return;
        } else if (tile.getState() == Tile.State.MARKED) {
            tile.setState(Tile.State.CLOSED);
            return;
        }
    }

    public int getRemainingMineCount() {
        return getMineCount() - getNumberOf(Tile.State.MARKED);
    }

    private void generate() {
        Random random = new Random();
        int count;
        if (mineCount < rowCount * columnCount) {
            count = mineCount;
        } else {
            return;
        }
        while (count > 0) {
            int r = random.nextInt(rowCount);
            int c = random.nextInt(columnCount);
            if (tiles[r][c] == null) {
                tiles[r][c] = new Mine();
                count--;
            }
        }

        //pocet min v susednych dlazdiciac
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                if (tiles[r][c] == null) {
                    tiles[r][c] = new Clue(countAdjacentMines(r, c));
                }
            }
        }
    }

    public boolean isSolved() {
        return ((rowCount * columnCount) - getNumberOf(Tile.State.OPEN) == mineCount);
    }

    private int getNumberOf(Tile.State state) {
        int count = 0;

        //with stream
        count = (int) Arrays.stream(tiles).flatMap(f -> Arrays.stream(f).filter(c -> c.getState().equals(state))).count();

        //without stream
//        for (int r = 0; r < rowCount; r++) {
//            for (int c = 0; c < columnCount; c++) {
//                if (tiles[r][c].getState() == state) {
//                    count++;
//                }
//            }

        return count;
    }

    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private void openAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        openTile(actRow, actColumn);
                    }
                }
            }
        }
    }

    private boolean outOfField(int nextRow, int nextColumn) {
        return nextRow < 0 || nextColumn < 0
                || nextRow >= rowCount
                || nextColumn >= columnCount;
    }

    public Tile[][] getTilesForController() {
        return tiles;
    }

}
