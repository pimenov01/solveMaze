package entities;

public class Cell {

    boolean visited;
    TypeOfCell type;
    int x;
    int y;
    public Cell(int x, int y, TypeOfCell type) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.visited = false;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

}