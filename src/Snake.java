import java.awt.*;
import java.util.*;



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

        //on parcours une premiere fois la map pour mettre toutes les cases comme étant des murs
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                map[i][j] = Case.MUR;
            }
        }
        //on remplace toutes les cases par des cases vides, en excluant la bordure
        for (int i = 1; i < HEIGHT-1; i++) {
            for (int j = 1; j < WIDTH-1; j++) {
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

        //Ajouter les déplacements ici

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

    // testGameOver teste si la partie est perdue
    public void testGameOver(){
        //parcours de snake pour savoir si l'une des cases est un mur
        for (Point p : snake){
            if(map[p.y][p.x]== Case.WALL){
                //la partie est perdue, on lance un game over
                gameOver();
            }
        }
        //on teste si la tête a les même coordonnées qu'un point du snake (autre que la tête donc)
        for(int i = 1; i<snake.size(); i++){
            if((snake.get(0).x == snake.get(i).x) && (snake.get(0).y == snake.get(i).y)){
                gameOver();
            }

        }


    }
    //méthode gameOver appelée quand la partie est perdue !!!!! A FAIRE !!!!!
    public void gameOver(){
        //remplir la méthode gameOver
    }
}