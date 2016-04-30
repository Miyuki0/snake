import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Snake {

    public boolean end;
    public final static int NEWFOODTIME = 5;
    public final static int MAPUPDATETIME = 500;
    public final static int WIDTH = 20;
    public final static int HEIGHT = 20;
    private List<Point> snake;

    private Point food;
    ScheduledExecutorService foodGenerate;
    ScheduledExecutorService mapUpdate;


    private enum Case {SNAKE, VOID, FOOD, WALL}

    public enum Direction {GAUCHE, HAUT, DROITE, BAS}


    private Case[][] map;
    private Direction direction;

    public Snake() {
        map = new Case[HEIGHT][WIDTH];

        //on parcours une premiere fois la map pour mettre toutes les cases comme étant des murs
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                map[i][j] = Case.WALL;
            }
        }
        //on remplace toutes les cases par des cases vides, en excluant la bordure
        for (int i = 1; i < HEIGHT - 1; i++) {
            for (int j = 1; j < WIDTH - 1; j++) {
                map[i][j] = Case.VOID;
            }
        }


        direction = Direction.GAUCHE;
        snake = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            snake.add(new Point(10 + i, HEIGHT / 2));
        }
        food = new Point(0, 0);
        end = true;


    }

    public void start() {
        mapUpdate = Executors.newSingleThreadScheduledExecutor();
        foodGenerate = Executors.newSingleThreadScheduledExecutor();
        mapUpdate.scheduleAtFixedRate((Runnable) () -> mapUpdater(), 0, MAPUPDATETIME, TimeUnit.MILLISECONDS);
        foodGenerate.scheduleAtFixedRate((Runnable) () -> foodGenerator(false), 0, NEWFOODTIME, TimeUnit.SECONDS);

    }

    public void stop() {
        mapUpdate.shutdown();
        foodGenerate.shutdown();
        end=false;
    }

    public void reinitFoodGenerate() {
        foodGenerate.shutdown();
        foodGenerate = Executors.newSingleThreadScheduledExecutor();
        foodGenerate.scheduleAtFixedRate((Runnable) () -> foodGenerator(false), NEWFOODTIME, NEWFOODTIME, TimeUnit.SECONDS);
    }


    public void mapUpdater() {

        List<Point> cSnake = new ArrayList<>();
        int adding = snake.size() - 1;
        Point s = new Point(snake.get(0));


        switch (direction) {
            //Et on tourne biensur !
            case DROITE:
                cSnake.add(new Point(snake.get(0).x + 1, snake.get(0).y));
                if (map[snake.get(0).y][snake.get(0).x + 1] == Case.WALL) {
                    stop();
                    return;
                }
                break;
            case HAUT:
                cSnake.add(new Point(snake.get(0).x, snake.get(0).y - 1));
                if (map[snake.get(0).y-1][snake.get(0).x] == Case.WALL) {
                    stop();
                    return;
                }
                break;

            case GAUCHE:
                cSnake.add(new Point(snake.get(0).x - 1, snake.get(0).y));
                if (map[snake.get(0).y][snake.get(0).x - 1] == Case.WALL) {
                    stop();
                    return;
                }
                break;
            case BAS:
                cSnake.add(new Point(snake.get(0).x, snake.get(0).y + 1));
                if (map[snake.get(0).y+1][snake.get(0).x] == Case.WALL) {
                    stop();
                    return;
                }
                break;
            default:
        }

        if (s.equals(food)) {
            adding++;
            //foodGenerator(true);
        } // Il faut générer un nouveau
        else {
            map[snake.get(snake.size() - 1).y][snake.get(snake.size() - 1).x] = Case.VOID;// /!\ pb
        }
        for (int i = 0; i < adding; i++) {
            cSnake.add(snake.get(i));
        }


        snake = cSnake;

        // On avance tout ce petit corps


        //Ah oui la map. Faut pas oublier de
        for (Point p : snake) {
            map[p.y][p.x] = Case.SNAKE;
        }
    }

    public void foodGenerator(boolean calledBySnake) {
        int x = (int) (Math.random() * WIDTH);
        int y = (int) (Math.random() * HEIGHT);

        if (map[y][x] == Case.VOID) {
            map[y][x] = Case.FOOD;
            if (calledBySnake) {
                reinitFoodGenerate();
                //Si pb avec le serpent à l'affichage (coupé) le pb viendra d'ici
            } else {
                map[food.y][food.x] = Case.VOID;
            }
            food.setLocation(x, y);

        } else
            foodGenerator(calledBySnake);


    }

    public void setDirection(Direction d) {

        if(((direction == Direction.GAUCHE || direction == Direction.DROITE) && (d == Direction.HAUT || d==Direction.BAS))||
                (direction == Direction.HAUT || direction == Direction.BAS) && (d == Direction.GAUCHE || d == Direction.DROITE))
            direction = d;
    }


    //méthode gameOver appelée quand la partie est perdue !!!!! A FAIRE !!!!!
    public void gameOver() {
        System.out.println("perdu");
    }

    public Case[][] getMap() {
        return map;
    }
}