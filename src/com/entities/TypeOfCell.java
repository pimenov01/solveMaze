package com.entities;

public enum TypeOfCell {
    /**
     * Used for text display of the maze.
     */
    WALL("+"), EXIT("X"), ENTRANCE("*"), EMPTY_CELL("0"), HOLE(".");

    String string;

    TypeOfCell(String name) {
        this.string = name;
    }

    @Override
    public String toString() {
        return string;
    }

}
