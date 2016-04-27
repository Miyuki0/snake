import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Snake {

    public final static double NewFoodTime = 5.0;
    public final static int WIDTH = 20;
    public final static int HEIGHT = 20;
    private double lastRefresh;
    private double time;
    private List<Point> snake;
    private Point food;

    private enum Case {SNAKE, VOID, FOOD, WALL}



    private enum Direction {GAUCHE, HAUT, DROITE, BAS}


    private Case[][] map;
    private Direction direction;

    public Snake() {
        map = new Case[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                map[i][j] = Case.VOID;
            }
        }
        direction = Direction.GAUCHE;
        snake = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            snake.add(new Point((WIDTH / 2) + i, HEIGHT / 2));
        }

    }

    public void mapUpdater() {
        for (Point p : snake) {
            map[p.y][p.x] = Case.SNAKE;
        }

        if(lastRefresh + NewFoodTime > time)
        {
            lastRefresh = time;
            foodGenerator();
        }

    }

    public void foodGenerator() {
        int x = (int)(Math.random()*WIDTH);
        int y = (int)(Math.random()*HEIGHT);

        if(map[y][x] == Case.VOID)
            map[y][x] = Case.FOOD;
        else
            foodGenerator();

    }
}