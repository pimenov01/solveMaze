package com.controller;

import com.entities.MazeView;
import com.generator.Generator;

public class Controller {

    private int height;
    private int width;
    private Long seed;

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *  If seed is specified, a maze with dimensions and
     *  seed is generated and submitted for drawing.
     *  Otherwise, the current time in MS is used instead of seed
     *  (the detailed work in the Generator class)
     *  and the maze is submitted for drawing in the same way.
     */
    public MazeView start() {
            Generator generator;
            if (seed != null) {
                generator = new Generator(height, width, seed);
            } else {
                generator = new Generator(height, width);
            }
            generator.generate();
            return new MazeView(height, width, generator.getField().toString().split("\n"));
    }
}
