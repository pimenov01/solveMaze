package com.entities;

import javafx.util.Pair;

public class Field {

    Cell[][] field;
    int width;
    int height;
    Pair<Integer, Integer> entrance;

    public Field(int height, int width) {
        this.width = width;
        this.height = height;
        field = new Cell[height][width];
    }

    public void init() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.initField(x, y);
            }
        }
    }

    /**
     * Basically, a lot of interesting things happen here:
     * if we want to simplify the maze, we can remove lines
     * 46-69 and add line 71. In this case, the maze will lose
     * some holes in the walls, which will simplify it, because
     * there will be fewer forks.
     * In this case, seed will work more correctly,
     * because holes wont be added to the walls.
     * And mazes with the same size and seed will always be the same.
     */
    @Override
    public String toString() {
        String[] answer;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.append(field[i][j].toString());
            }
            result.append("\n");
        }
        answer = result.toString().split("\n");
        StringBuilder result1 = new StringBuilder();
        for (int k = 0; k < height; k++) {
            if (k >= 1 && k < height - 1) {
                int random = (int) (Math.random() * height);
                int random1 = (int) (Math.random() * height);
                char[] arr = answer[k].toCharArray();
                for (int l = 1; l < arr.length - 1; l++) {
                    if (l == random) {
                        if (arr[random] == '+') {
                            arr[random] = '.';
                        }
                    }
                    if (l == random1) {
                        if (arr[random1] == '+') {
                            arr[random1] = '.';
                        }
                    }
                }
                answer[k] = String.valueOf(arr);
            }
            result1.append(answer[k]);
            result1.append('\n');
        }
        //System.out.println(result.toString());//show maze in the console
        //return result.toString(); //simplify maze (for girls)
        //System.out.println(result1.toString()); //show maze in the console
        return result1.toString();
    }

    private void initField(int x, int y) {
        field[y][x] = new Cell(x, y, TypeOfCell.WALL);
    }

    /**
     * Replace the cell that you can pass through in the maze with a wall or replace the wall with a passage.
     * @param x x coordinate of the current cell
     * @param y y coordinate of the current cell
     * @param isWall if we want the cell to become a wall we set isWall = true
     */
    public void setOrDeleteWall(int x, int y, Boolean isWall) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            if (isWall)
                field[y][x] = new Cell(x, y, TypeOfCell.WALL);
            else
                field[y][x] = new Cell(x, y, TypeOfCell.HOLE);
    }

    /**
     * Set the entrance and exit in the maze.
     * By default, they are located diagonally from Northwest to Southeast.
     * @param x x coordinate of the current cell
     * @param y y coordinate of the current cell
     * @param isExit if we want the cell to become a exit we set isExit = true
     */
    public void setEndPoints(int x, int y, Boolean isExit) {
        if (!isExit) {
            if (x == 1 && y == 1) {
                field[y][x] = new Cell(x, y, TypeOfCell.ENTRANCE);
                entrance = new Pair<>(x, y);
            }
        } else {
            if (x == width - 2 && y == height - 2) {
                field[y][x] = new Cell(x, y, TypeOfCell.EXIT);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return field[y][x];
    }

    public Cell[][] getField() {
        return field;
    }

}