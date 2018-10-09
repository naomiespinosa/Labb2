import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static java.lang.System.*;

/*
 *  Program to simulate segregation.
 *  See : http://nifty.stanford.edu/2014/mccown-schelling-model-segregation/
 *
 * NOTE:
 * - JavaFX first calls method init() and then method start() far below.
 * - To test uncomment call to test() first in init() method!
 *
 */
// Extends Application because of JavaFX (just accept for now)
public class Neighbours extends Application {
    final Random rand = new Random();

    // Enumeration type for the Actors
    enum Actor {
        BLUE, RED, NONE   // NONE used for empty locations
    }

    // Enumeration type for the state of an Actor
    enum State {
        UNSATISFIED,
        SATISFIED,
        NA     // Not applicable (NA), used for NONEs
    }

    // Below is the *only* accepted instance variable (i.e. variables outside any method)
    // This variable may *only* be used in methods init() and updateWorld()
    Actor[][] world;              // The world is a square matrix of Actors


    // This is the method called by the timer to update the world
    // (i.e move unsatisfied) approx each 1/60 sec.
    void updateWorld() {
        // % of surrounding neighbours that are like me
        final double threshold = 0.7;
        moveActor(world,threshold);
    }

    // This method initializes the world variable with a random distribution of Actors
    // Method automatically called by JavaFX runtime (before graphics appear)
    // Don't care about "@Override" and "public" (just accept for now)
    @Override
    public void init() {
        // test();    // <------
        // ---------- Uncomment to TEST!

        // %-distribution of RED, BLUE and NONE
        double[] dist = {0.25, 0.25, 0.50};
        // Number of locations (places) in world (square)
        int nLocations = 900;
        // TODO
        world = makeMatrix(nLocations, dist);
        // Should be last
        fixScreenSize(nLocations);
        out.print("hej");
    }


    // ------- Methods ------------------

    // TODO write the methods here, implement/test bottom up
    //---------------- index for moving Actor---------------------------
    /*int[] getUnsArr(int[]emptyArray, int[]unsatisfiedArray){
        shuffleEmptyArray(emptyArray, emptyArray.length);
        for(int index = 0; index < unsatisfiedArray.length; index++){
            emptyArray[index] = unsatisfiedArray[index];
        }
    return emptyArray;
    } */
    //-----------------moving actor-------------------------------
    void moveActor(Actor[][]world, double th){
      State[][]moodWorld = createMoodWorld(world,th);
      int[] unsArr = unsatisfiedArray(moodWorld);
      int[]emptyArr = emptyArray(world);
      int[]finalEmptyArr = shuffleEmptyArray(emptyArr);

      for (int index = 0 ; index <= unsArr.length -1; index++) {

          int rowE = (finalEmptyArr[index] / world.length);
          int colE = (finalEmptyArr[index] % world.length);

          int rowU = (unsArr[index] / world.length) ;
          int colU = (unsArr[index] % world.length);


          world[rowE][colE] = world[rowU][colU];
          world[rowU][colU] = Actor.NONE;

      }



    }

    //------Array of empty and unsatisfied Actors--------------------
    int [] emptyArray(Actor[][]world){
        int[]emptyArray = new int[(world.length*world.length)/2];
        int i = 0;
        int count = 0;
        for(int row = 0; row < world.length; row++){
            for(int col = 0; col < world[0].length;col++){
                if(world[row][col] == Actor.NONE){
                    emptyArray[count] = i;
                    count++;
                }i++;
            }
        }
    return emptyArray;
}
    int [] unsatisfiedArray(State[][]moodWorld){
        int count = unsatisfiedActors(moodWorld);
        int[]unsatisfiedArray = new int[count];
        int i = 0;
        int index = 0;
        for(int row = 0; row < moodWorld.length; row++){
            for(int col = 0; col < moodWorld.length;col++){
                if(moodWorld[row][col] == State.UNSATISFIED){
                    unsatisfiedArray[index] = i;
                    index++;
                }i++;
            }
        }
        return unsatisfiedArray;
    }

    int unsatisfiedActors(State[][]moodWorld) {
        int count = 0;
        for (int row = 0; row < moodWorld.length; row++) {
            for (int col = 0; col < moodWorld[0].length; col++) {
                if (moodWorld[row][col] == State.UNSATISFIED) {
                    count++;
                }
            }
        }
    return count++;
    }




    //------make world #2--------------------
    State[][] createMoodWorld(Actor[][]world, double th){
        State[][] moodWorld = new State[world.length][world[0].length];
        for(int row = 0; row < world.length; row++){
            for(int col = 0; col < world.length; col++){
                if (checkSatisfaction(world, row, col, th)) {
                    moodWorld[row][col] = State.SATISFIED;
                }else if(!checkSatisfaction(world, row, col, th)){
                    moodWorld[row][col] = State.UNSATISFIED;
                }else{
                    moodWorld[row][col] = State.NA;

                }
            }
        }
        return moodWorld;
    }






    //----------MAKE WORLD---------------

    Actor[][] makeMatrix(int nLocations, double[] dist) {

        Actor[] newArr = getActors(nLocations, dist);
        int row = 0;
        int column = 0;
        int i = 0;
        int matrixlength = (int) (long) round(sqrt(newArr.length));
        Actor[][] matris = new Actor[matrixlength][matrixlength];
        while (row < matris.length && column < matris[0].length) {

            matris[row][column] = newArr[i];
            column++;
            if (column == matris[0].length) {
                row = row + 1;
                column = 0;
            }

            i++;
        }
        return matris;
    }


    Actor[] shuffleActorArray(Actor[] arr, int nLocations) {
        for (int i = arr.length - 1; i > 0; i--) {
            int r = rand.nextInt(nLocations);
            Actor save = arr[i];
            arr[i] = arr[r];
            arr[r] = save;

        }
        return arr;
    }

    int[] shuffleEmptyArray(int[] arr) {
        int count = arr.length;
        for (int i = count - 1; i > 0; i--) {
            int r = rand.nextInt(count);
            int save = arr[i];
            arr[i] = arr[r];
            arr[r] = save;
        }
        return arr;
    }


    Actor[] getActors(int nLocations, double[] dist) {
        Actor[] actors = new Actor[nLocations];
        double nRed = round(nLocations * dist[0]);
        double nBlue = round(nLocations * dist[1]);
        double nNone = round(nLocations * dist[2]);
        int i = 0;
        while (nRed > 0) {
            actors[i] = Actor.RED;
            nRed--;
            i++;
        }
        while (nBlue > 0) {
            actors[i] = Actor.BLUE;
            nBlue--;
            i++;
        }
        while (i < actors.length) {
            actors[i] = Actor.NONE;
            i++;
        }
        actors = shuffleActorArray(actors, nLocations);
        return actors;
    }

    //------------LOGIC METHODS-----------

    boolean checkSatisfaction (Actor[][] world, int row, int col, double th) {
        int nRed = 0;
        int nBlue = 0;
        boolean S = true;

        for (int x = row - 1; x <= row + 1; x++) {
            for (int y = col - 1; y <= col + 1; y++) {
                if (isValidLocation(x, y, world.length)) {
                    if ((x != row || y != col) && world[x][y] == Actor.RED) {
                        nRed++;
                    } else if ((x != row || y != col) && world[x][y] == Actor.BLUE) {
                        nBlue++;
                    }
                }
            }
        }
        if (world[row][col] == Actor.RED) {
            S = checkThreshold(nRed, nBlue, th); // nRed first if wanna test red
        } else if (world[row][col] == Actor.BLUE) {
            S = checkThreshold(nBlue, nRed, th);   // nBlue first if wanna test blu
        }

        return S;
    }

    boolean checkThreshold(double nTest, double nOther, double th) {
        double total = nTest + nOther;
        double checkPercent = nTest / total;

        if (checkPercent >= th) {
            return true;
        } else {
            return false;
        }
    }

    void moveActor(Actor[][] world, int x, int y) {
        int rx = rand.nextInt(world.length);
        int ry = rand.nextInt((world[0].length));
        if (world[rx][ry] == Actor.NONE) {
            world[rx][ry] = world[x][y];
            world[x][y] = Actor.NONE;
        }// else

    }

    boolean isValidLocation(int row, int col, int size) {
        return row >= 0 && col >= 0 && row < size && col < size;

    }


    // ------- Testing -------------------------------------

    // Here you run your tests i.e. call your logic methods
    // to see that they really work
    void test() {
        // A small hard coded world for testing
        Actor[][] testWorld = new Actor[][]{
                {Actor.RED, Actor.RED, Actor.NONE},
                {Actor.NONE, Actor.BLUE, Actor.NONE},
                {Actor.RED, Actor.NONE, Actor.BLUE}
        };
        double th = 0.5;   // Simple threshold used for testing
        int size = testWorld.length;

        // TODO test methods
        out.println(isValidLocation(0, 0, size));
        out.println(!isValidLocation(-1, 0, size));
        out.println(!isValidLocation(4, 0, size));
        out.println(checkSatisfaction(testWorld,0,0,th));
        State[][] moodworld = createMoodWorld(testWorld, th);
        out.println(moodworld[1][1]);
        moveActor(testWorld,th);
        exit(0);
    }

    // Helper method for testing (NOTE: reference equality)
    <T> int count(T[] arr, T toFind) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == toFind) {
                count++;
            }
        }
        return count;
    }

    // *****   NOTHING to do below this row, it's JavaFX stuff  ******

    double width = 400;   // Size for window
    double height = 400;
    long previousTime = nanoTime();
    final long interval = 450000000;
    double dotSize;
    final double margin = 50;

    void fixScreenSize(int nLocations) {
        // Adjust screen window depending on nLocations
        dotSize = (width - 2 * margin) / sqrt(nLocations);
        if (dotSize < 1) {
            dotSize = 2;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Build a scene graph
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().addAll(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Create a timer
        AnimationTimer timer = new AnimationTimer() {
            // This method called by FX, parameter is the current time
            public void handle(long currentNanoTime) {
                long elapsedNanos = currentNanoTime - previousTime;
                if (elapsedNanos > interval) {
                    updateWorld();
                    renderWorld(gc, world);
                    previousTime = currentNanoTime;
                }
            }
        };

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation");
        primaryStage.show();

        timer.start();  // Start simulation
    }


    // Render the state of the world to the screen
    public void renderWorld(GraphicsContext g, Actor[][] world) {
        g.clearRect(0, 0, width, height);
        int size = world.length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                double x = dotSize * col + margin;
                double y = dotSize * row + margin;

                if (world[row][col] == Actor.RED) {
                    g.setFill(Color.RED);
                } else if (world[row][col] == Actor.BLUE) {
                    g.setFill(Color.BLUE);
                } else {
                    g.setFill(Color.WHITE);
                }
                g.fillOval(x, y, dotSize, dotSize);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}