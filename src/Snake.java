import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Snake {

    public final static int NEWFOODTIME = 5000;
    public final static long MAPUPDATETIME = 5;
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
        food = new Point(0,0);


        ScheduledExecutorService mapUpdate = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService foodGenerate = Executors.newSingleThreadScheduledExecutor();

        mapUpdate.scheduleAtFixedRate((Runnable) this::mapUpdater, 0, MAPUPDATETIME, TimeUnit.SECONDS);

        foodGenerate.scheduleAtFixedRate((Runnable) this::foodGenerator, 0, NEWFOODTIME, TimeUnit.SECONDS);
        }


    public void mapUpdater() {
        for (Point p : snake) {
            map[p.y][p.x] = Case.SNAKE;
        }
        if((map[snake.get(0).y][snake.get(0).x] != Case.FOOD))
        {
            map[snake.get(snake.size()-1).x][snake.get(snake.size()-1).x] = Case.VOID;
            snake.remove(snake.size()-1);
        }

    }

    public void foodGenerator() {
        int x = (int)(Math.random()*WIDTH);
        int y = (int)(Math.random()*HEIGHT);

        if(map[y][x] == Case.VOID) {
            map[y][x] = Case.FOOD;
            map[food.y][food.x]=Case.VOID;
            mapUpdater();
            food.setLocation(x,y);
        }
        else
            foodGenerator();

    }
}