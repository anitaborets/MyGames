package org.example.tiktaktoe;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Scanner;

import static org.example.common.Constants.*;

@Component
public class TicTacToe {
    final char SIGN_X = 'x';
    final char SIGN_O = 'o';
    final char SIGN_EMPTY = '.';
    char[][] table;
    Random random;
    Scanner sc;
    int countX = 0;
    int countO = 0;
    int free = 0;
    public static int TT_SIZE = 3;

    public static void main(String[] args) {
        new TicTacToe().play();
    }

    TicTacToe() {
        random = new Random();
        sc = new Scanner(System.in);
        table = new char[TT_SIZE][TT_SIZE];
        this.initTable();
    }

    public void play() {
        printTable();

        while (true) {
            this.manWalk();

            if (checkWin(SIGN_X)) {
                System.out.println(WIN);
                break;
            }

            if (this.isFullTable()) {
                System.out.println(REMIZA);
                break;
            }

            this.programWalk();
            this.printTable();

            if (checkWin(SIGN_O)) {
                System.out.println(GAME_OVER + "I am win!");
                break;
            }

            if (this.isFullTable()) {
                System.out.println(REMIZA);
                break;
            }

        }

        System.out.println(GAME_OVER);
        this.printTable();
    }

    void initTable() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.table[row][col] = SIGN_EMPTY;
            }
        }
    }

    void printTable() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                System.out.print("[" + this.table[row][col] + "] ");
            }
            System.out.print("\n");
        }
    }

    int getNumber() {
        int trying = 0;
        String buffer;

        while (true) {
            if (sc.hasNextInt()) {
                trying = sc.nextInt();
                break;
            } else {
                buffer = sc.next();
                System.out.println("incorrect input: " + buffer);
            }
        }

        return trying;
    }


    void manWalk() {
        int x, y;

        do {
            System.out.println("Input  X and Y (1..3):");
            x = getNumber() - 1;
            y = getNumber() - 1;
        } while (!this.isCellValidMan(x, y));

        table[y][x] = SIGN_X;
    }

    boolean isCellValidMan(int x, int y) {

        if (x < 0 || x > 2 || y < 0 || y > 2) {
            System.out.println("Please input number from (1..3)");
            return false;
        }

        if (this.table[y][x] != SIGN_EMPTY) {
            System.out.println("Tile is not empty.");
            return false;
        }

        return true;
    }


    boolean isCellValidProg(int x, int y) {

        if (x < 0 || x > 2 || y < 0 || y > 2) {
            return false;
        }

        return this.table[y][x] == SIGN_EMPTY;
    }


    boolean isFullTable() {

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (this.table[row][col] == SIGN_EMPTY) return false;

            }
        }

        return true;
    }


    void programWalk() {

        if (this.check1Diagonal()) return;

        if (this.check2Diagonal()) return;

        if (this.checkRows()) return;

        if (this.checkColumns()) return;

        this.randomWalk();

    }


    void count(int row, int col) {
        if (this.table[row][col] == SIGN_X) {
            this.countX++;
        }

        if (this.table[row][col] == SIGN_O) {
            this.countO++;
        }

        if (this.table[row][col] == SIGN_EMPTY) {
            this.free++;
        }
    }

    void initCount() {
        this.countX = 0;
        this.countO = 0;
        this.free = 0;
    }


    boolean isLosingNear() {
        return ((this.free == 1 && this.countX == 2) ? true : false);
    }


    boolean isWinNear() {
        return ((this.free == 1 && this.countO == 2) ? true : false);
    }


    boolean checkWin(char dot) {
        for (int i = 0; i < 3; i++) {
            if ((table[i][0] == dot && table[i][1] == dot && table[i][2] == dot) ||
                    (table[0][i] == dot && table[1][i] == dot && table[2][i] == dot))
                return true;
        }

        if ((table[0][0] == dot && table[1][1] == dot && table[2][2] == dot) ||
                (table[2][0] == dot && table[1][1] == dot && table[0][2] == dot))
            return true;

        return false;
    }


    void randomWalk() {
        int x, y;
        do {
            x = random.nextInt(3);
            y = random.nextInt(3);
        } while (!this.isCellValidProg(x, y));

        this.table[y][x] = SIGN_O;
    }


    boolean putO(int row, int col) {
        if (this.table[row][col] == SIGN_EMPTY) {
            this.table[row][col] = SIGN_O;
            return true;
        }
        return false;
    }

    // контроль первой диагонали
    boolean check1Diagonal() {
        this.initCount();
        for (int row = 0; row < 3; row++) {
            this.count(row, row);
        }

        if (this.isWinNear() || this.isLosingNear()) {
            for (int row = 0; row < 3; row++) {
                if (this.putO(row, row)) return true;
            }
        }

        return false;
    }

    // контроль второй диагонали
    boolean check2Diagonal() {
        this.initCount();
        int col = 2;
        for (int row = 0; row < 3; row++) {
            this.count(row, col);
            col--;
        }

        if (this.isWinNear() || this.isLosingNear()) {
            col = 2;
            for (int row = 0; row < 3; row++) {
                if (this.putO(row, col)) return true;
                col--;
            }
        }

        return false;
    }

    // контроль строк
    boolean checkRows() {
        for (int row = 0; row < 3; row++) {
            this.initCount();
            for (int col = 0; col < 3; col++) {
                this.count(row, col);
            }

            if (this.isWinNear() || this.isLosingNear()) {
                for (int col = 0; col < 3; col++) {
                    if (this.putO(row, col)) return true;
                }
            }

        }

        return false;
    }

    // контроль столбцов
    boolean checkColumns() {
        for (int col = 0; col < 3; col++) {
            this.initCount();
            for (int row = 0; row < 3; row++) {
                this.count(row, col);
            }

            if (this.isWinNear() || this.isLosingNear()) {
                for (int row = 0; row < 3; row++) {
                    if (this.putO(row, col)) return true;
                }
            }

        }

        return false;
    }
}
