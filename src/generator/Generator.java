package generator;

import entities.Cell;
import entities.Field;

import java.util.*;

public class Generator {

    private int width;
    private int height;
    private Random random;
    private Field field;
    private long unvisited;


    public Generator(int height, int width, Long seed) {
        init(width, height, seed);
    }

    public Generator(int height, int width) {
        init(width, height, System.currentTimeMillis());
    }

    private void init(int width, int height, Long seed) {
        System.out.println("Seed is " + seed);
        unvisited = 0;
        field = new Field(height, width);
        field.init();
        this.width = width;
        this.height = height;
        random = new Random(seed);
    }

    private void generateField() {
        field.init();
        for (int i = 1; i < field.getField().length; i++) {
            for (int j = 1; j < field.getField().length; j++) {
                if(i % 2 == 1 && j % 2 == 1 && i != field.getField().length - 1 && j != field.getField().length - 1) {
                    field.setOrDeleteWall(j, i, false);
                    unvisited++;
                }
            }
        }
    }

    private void generateMaze() {
        Cell[][] cells = field.getField();
        Cell currentCell = cells[1][1];
        Cell neighbourCell;
        Stack<Cell> stack = new Stack<>();
        List<Cell> cellsUnvisited = getUnvisitedCells();
        do {
            cellsUnvisited.remove(currentCell);
            List<Cell> neighbours = getNeighbours(currentCell);
            if (neighbours.size() != 0) {
                int randNum = random.nextInt(neighbours.size());
                neighbourCell = neighbours.get(randNum);
                stack.push(currentCell);
                removeWall(currentCell, neighbourCell);
                currentCell = neighbourCell;
                currentCell.setVisited(true);
                unvisited--;
            } else if (stack.size() > 0) {
                currentCell = stack.pop();
            } else {
                int randNum = random.nextInt(cellsUnvisited.size() - 1);
                currentCell = cellsUnvisited.get(randNum);
            }
        }
        while (unvisited > 0);
    }

    private List<Cell> getUnvisitedCells() {
        Cell[][] cells = field.getField();
        List<Cell> cellArrayList = new LinkedList<>();
        for (Cell[] cell : cells) {
            for (int j = 0; j < cells.length; j++) {
                if (cell[j].toString().equals(".") && !cell[j].isVisited()) {
                    cellArrayList.add(cell[j]);
                }
            }
        }
        return cellArrayList;
    }

    private List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<>();
        for(Direction direction : Direction.values()){
            if(cell.getX() + direction.getCoords().getKey() * 2 > 0 &&
                    cell.getX() + direction.getCoords().getKey() * 2 < width &&
                    cell.getY() + direction.getCoords().getValue() * 2 > 0 &&
                    cell.getY() + direction.getCoords().getValue() * 2 < height){
                Cell mazeCellCurrent = field.getCell(cell.getX() + direction.getCoords().getKey() * 2, cell.getY() + direction.getCoords().getValue() * 2);
                if(mazeCellCurrent.toString().equals(".") && !mazeCellCurrent.isVisited()){
                    neighbours.add(mazeCellCurrent);
                }
            }
        }
        return neighbours;
    }

    private void removeWall(Cell currentCell, Cell neighbourCell) {
        int xDiff = neighbourCell.getX() - currentCell.getX();
        int yDiff = neighbourCell.getY() - currentCell.getY();
        int addX, addY;

        addX = (xDiff != 0) ? (xDiff / Math.abs(xDiff)) : 0;
        addY = (yDiff != 0) ? (yDiff / Math.abs(yDiff)) : 0;

        int newX = currentCell.getX() + addX; //wall coords
        int newY = currentCell.getY() + addY;

        field.setOrDeleteWall(newX, newY, false);
        field.getCell(newX, newY).setVisited(true);
    }

    public void generate() {
        generateField();
        generateMaze();
        field.setEndPoints(1, 1, false);
        field.setEndPoints(field.getField().length - 2, field.getField().length - 2, true);
    }

    public Field getField() {
        return field;
    }

}