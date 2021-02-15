package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private TextureAtlas backgroundAtlas;
    private TextureRegion wall;
    private TextureRegion dot;
    private TextureRegion catnip;
    private TextureRegion catToy;
    private TextureAtlas pacCatAtlas;
    private TextureRegion catLeft;
    private TextureRegion catMiddle;
    private TextureRegion catRight;
    private Animation<TextureRegion> empoweredAnimationLeft;
    private Animation<TextureRegion> empoweredAnimationRight;
    private Animation<TextureRegion> empoweredAnimationMiddle;
    private float animationTime = 0.0f;
    private TextureAtlas empoweredPacCatLeft;
    private TextureAtlas empoweredPacCatRight;
    private TextureAtlas empoweredPacCatMiddle;
    private TextureAtlas ghostAtlas;
    private TextureRegion blinky;
    private TextureRegion pinky;
    private TextureRegion inky;
    private TextureRegion clyde;
    public static List<Integer> level1;
    private final int blockSize = 30; //pixel
    private Vector2 catPosition;
    private int pacCatIndex;
    private int direction = 0;
    private static final int numberOfRows = 28;
    private static final int numberOfCols = 30;
    int elementInGrid;

    private static final int DOT = 0;
    private static final int WAND = 1;
    private static final int CATNIP = 3;
    private static final int CATTOY = 5;

    private Vector2 blinkyPosition = new Vector2(12 * (blockSize + 1), 14 * blockSize);
    private Vector2 pinkyPosition = new Vector2(13 * (blockSize + 1), 14 * blockSize);
    private Vector2 inkyPosition = new Vector2(16 * (blockSize + 1), 14 * blockSize);
    private Vector2 clydePosition = new Vector2(17 * (blockSize + 1), 14 * blockSize);
    private int blinkyIndex;
    private int pinkyIndex;
    private int inkyIndex;
    private int clydeIndex;
    private int lastDirection = 1;

    private int score = 0;
    private boolean empowered = false;
    private final int MAX_HEARTS = 5;
    private int hearts = 3;
    int collectedDots = 0;
    int collectedToys = 0;


    public GameScreen() {
        backgroundAtlas = new TextureAtlas("background.atlas");
        wall = backgroundAtlas.findRegion("cloudwall2");
        dot = backgroundAtlas.findRegion("dot");
        catnip = backgroundAtlas.findRegion("catnip");
        catToy = backgroundAtlas.findRegion("mouse");
        batch = new SpriteBatch();
        pacCatAtlas = new TextureAtlas("pacCat.atlas");
        catLeft = pacCatAtlas.findRegion("cat-left");
        catMiddle = pacCatAtlas.findRegion("cat-middle");
        catRight = pacCatAtlas.findRegion("cat-right");
        empoweredPacCatLeft = new TextureAtlas("empoweredPacCat-left.atlas");
        empoweredPacCatRight = new TextureAtlas("empoweredPacCat-right.atlas");
        empoweredPacCatMiddle = new TextureAtlas("empoweredPacCat-middle.atlas");
        empoweredAnimationLeft = new Animation<>(0.2f, empoweredPacCatLeft.findRegions("glittercatLeft"), Animation.PlayMode.LOOP);
        empoweredAnimationRight = new Animation<>(0.2f, empoweredPacCatRight.findRegions("glittercatRight"), Animation.PlayMode.LOOP);
        empoweredAnimationMiddle = new Animation<>(0.2f, empoweredPacCatMiddle.findRegions("glittercatMiddle"), Animation.PlayMode.LOOP);
        ghostAtlas = new TextureAtlas("ghosts.atlas");
        blinky = ghostAtlas.findRegion("ghost1-left");
        pinky = ghostAtlas.findRegion("ghost2-left");
        inky = ghostAtlas.findRegion("ghost3-left");
        clyde = ghostAtlas.findRegion("ghost4-left");
        catPosition = new Vector2(14 * (blockSize + 1), 10 * blockSize);

        // 0 - pac-dots
        // 1 - wall
        // 2 - ghost-lair
        // 3 - catnip
        // 4 - empty
        // 5 - cattoy
        // 6 - cat startpoint
        // 30x29 field
        List<Integer> temp = new ArrayList<>(List.of(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
                                                     1,1,5,0,0,0,0,0,0,0,0,0,0,0,1,1,5,0,0,0,0,0,0,0,0,0,0,0,1,1,
                                                     1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,
                                                     1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,5,1,1,1,1,0,1,1,
                                                     1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,
                                                     1,1,0,0,0,0,0,5,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,
                                                     1,1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,1,1,
                                                     1,1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,1,1,
                                                     1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,
                                                     1,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1,1,
                                                     1,1,1,1,1,1,1,0,1,1,4,4,4,4,4,4,4,4,4,4,1,1,0,1,1,1,1,1,1,1,
                                                     1,1,1,1,1,1,1,0,1,1,4,1,1,1,2,2,1,1,1,4,1,1,0,1,1,1,1,1,1,1,
                                                     1,1,1,1,1,1,1,0,1,1,4,1,2,2,2,2,2,2,1,4,1,1,0,1,1,1,1,1,1,1,
                                                     1,3,5,0,0,0,0,0,0,0,4,1,2,2,2,2,2,2,1,4,0,0,0,0,0,0,5,3,1,1,
                                                     1,1,1,1,1,1,1,0,1,1,4,1,2,2,2,2,2,2,1,4,1,1,0,1,1,1,1,1,1,1,
                                                     1,1,1,1,1,1,1,0,1,1,4,1,1,1,1,1,1,1,1,4,1,1,0,1,1,1,1,1,1,1,
                                                     1,1,1,1,1,1,1,0,1,1,4,1,1,1,1,1,1,1,1,4,1,1,0,1,1,1,1,1,1,1,
                                                     1,1,5,0,0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,1,1,
                                                     1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,
                                                     1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,
                                                     1,1,5,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,1,1,0,0,5,1,1,
                                                     1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,
                                                     1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,
                                                     1,1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,5,0,0,0,1,1,0,0,0,0,0,0,1,1,
                                                     1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,
                                                     1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,
                                                     1,1,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,1,1,
                                                     1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
        level1 = new ArrayList<>(temp);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector2 oldPosition = this.catPosition;

        batch.begin();
        drawFullField();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {

            Vector2 newPosition = new Vector2(oldPosition.x - (blockSize + 1), oldPosition.y);
            int elementInGrid = getNewPosition(newPosition);
            pacCatIndex = getIndex(newPosition);

            checkMaze(elementInGrid, newPosition);
            checkScore(elementInGrid, pacCatIndex);

            animationTime += delta;
            if (empowered) {
                // animation
                TextureRegion currentFrame = empoweredAnimationLeft.getKeyFrame(animationTime);
                batch.draw(currentFrame, catPosition.x, catPosition.y, blockSize, blockSize);
            } else batch.draw(catLeft, catPosition.x, catPosition.y, blockSize, blockSize);
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {


            Vector2 newPosition = new Vector2(oldPosition.x + (blockSize + 1), oldPosition.y);
            elementInGrid = getNewPosition(newPosition);
            pacCatIndex = getIndex(newPosition);

            checkMaze(elementInGrid, newPosition);
            checkScore(elementInGrid, pacCatIndex);

            animationTime += delta;
            if (empowered) {
                // animation
                TextureRegion currentFrame = empoweredAnimationRight.getKeyFrame(animationTime);
                batch.draw(currentFrame, catPosition.x, catPosition.y, blockSize, blockSize);
            } else batch.draw(catRight, catPosition.x, catPosition.y, blockSize, blockSize);
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y + (blockSize));
            elementInGrid = getNewPosition(newPosition);
            pacCatIndex = getIndex(newPosition);

            checkMaze(elementInGrid, newPosition);
            checkScore(elementInGrid, pacCatIndex);

            animationTime += delta;
            if (empowered) {
                // animation
                TextureRegion currentFrame = empoweredAnimationRight.getKeyFrame(animationTime);
                batch.draw(currentFrame, catPosition.x, catPosition.y, blockSize, blockSize);
            } else batch.draw(catRight, catPosition.x, catPosition.y, blockSize, blockSize);
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {

            Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y - (blockSize));
            elementInGrid = getNewPosition(newPosition);
            pacCatIndex = getIndex(newPosition);

            checkMaze(elementInGrid, newPosition);
            checkScore(elementInGrid, pacCatIndex);

            animationTime += delta;
            if (empowered) {
                // animation
                TextureRegion currentFrame = empoweredAnimationLeft.getKeyFrame(animationTime);
                batch.draw(currentFrame, catPosition.x, catPosition.y, blockSize, blockSize);
            } else batch.draw(catLeft, catPosition.x, catPosition.y, blockSize, blockSize);
        }

        else {
            animationTime += delta;
            if (empowered) {
                // animation
                TextureRegion currentFrame = empoweredAnimationMiddle.getKeyFrame(animationTime);
                batch.draw(currentFrame, catPosition.x, catPosition.y, blockSize, blockSize);
            } else batch.draw(catMiddle, catPosition.x, catPosition.y, blockSize, blockSize);
        }

        pacCatIsDying(pacCatIndex);

        moveBlinky();
        movePinky();
        moveInky();
        moveClyde();

        // wait inbetween steps
        try {
            Thread.sleep(150L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        batch.end();
        //System.out.println("cat position = " + catPosition.x + " / " + catPosition.y);

        //back to menu Screen
        /*if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            PacCatGame.INSTANCE.setScreen(new MenuScreen());
        }*/
    }

    private void drawFullField() {
        int posX = 0;
        int posY = 811;

        for (int i = 0; i < level1.size(); i++) {
            // jump to next row at drawing
            if (i % 30 == 0 && i > 0 && posY - blockSize > 0) {
                    posY -= blockSize;
                    posX = 0;
            }

            switch (level1.get(i)) {
                case 0 ->  {//draw dot
                        batch.draw(dot, posX, posY, 30, 30);
                }
                case 1 -> {
                        //draw wall
                        batch.draw(wall, posX, posY, 30, 30);
                }
                case 5 ->
                        //draw CatToy
                        batch.draw(catToy, posX, posY, 30, 30);
                case 3 ->
                        //draw catnip
                        batch.draw(catnip, posX, posY, 30, 30);
            }
            posX += (blockSize + 1);
        }
    }

    public int getNewPosition(Vector2 newPosition) {
        // konvertieren um den Punkt im Grid zu bekommen, damit können wir einfach im array nachschauen
        // was für ein Element an der jeweiligen position stehen würde (Wand, Katzengras, ..)
        int column = (int) (newPosition.x / (blockSize + 1));
        int row = numberOfRows - ((int) (newPosition.y / (blockSize))) -1;// in einem array fangen wir bei 0 zu zählen an drum -1 bei den zeilen

        //System.out.println("Cat goes to column " + column + " and row " + row + ". the grid has value: " + elementInGrid);
        return level1.get(row * numberOfCols + column);
    }

    public int getIndex(Vector2 newPosition) {
        int column = (int) (newPosition.x / (blockSize + 1));
        int row = numberOfRows - ((int) (newPosition.y / (blockSize))) -1;

        return row * numberOfCols + column;
    }

    public void checkMaze(int elementInGrid, Vector2 newPosition) {
        if (elementInGrid == WAND) {
            //System.out.println("Hier ist ne wand!");
        } else {
            this.catPosition = newPosition;
        }
    }

    public void checkMazeBlinky(int elementInGrid, Vector2 newPosition) {
        if (elementInGrid == WAND) {
            System.out.println("Hier ist ne wand!");
        } else {
            this.blinkyPosition = newPosition;
        }
    }

    public void checkMazePinky(int elementInGrid, Vector2 newPosition) {
        if (elementInGrid == WAND) {
            System.out.println("Hier ist ne wand!");
        } else {
            this.pinkyPosition = newPosition;
        }
    }

    public void checkMazeInky(int elementInGrid, Vector2 newPosition) {
        if (elementInGrid == WAND) {
            System.out.println("Hier ist ne wand!");
        } else {
            this.inkyPosition = newPosition;
        }
    }

    public void checkMazeClyde(int elementInGrid, Vector2 newPosition) {
        if (elementInGrid == WAND) {
            System.out.println("Geist, hier ist ne wand!");
        } else {
            this.clydePosition = newPosition;
        }
    }

    public void checkScore(int elementInGrid, int pacCatIndex) {
        if (elementInGrid == DOT) {
            level1.set(pacCatIndex, 4);
            score += 10;
            System.out.println(score);

            collectedDots++;
            System.out.println(collectedDots);
        }
        else if (elementInGrid == CATNIP) {
            level1.set(pacCatIndex, 4);
            empowered = true;
            timer(15);
        }
        else if (elementInGrid == CATTOY) {
            level1.set(pacCatIndex, 4);
            score += 15;

            collectedToys++;
            System.out.println(collectedToys);
        }

        if (collectedDots == 233 && collectedToys == 14) {
            PacCatGame.INSTANCE.setScreen(new VictoryScreen());
        }
    }

    public void moveBlinky() {
        Vector2 oldPosition = this.blinkyPosition;
        int direction = ThreadLocalRandom.current().nextInt(1, 4 + 1);

        if (lastDirection != direction || blinkyIndex != pinkyIndex || blinkyIndex != inkyIndex || blinkyIndex != clydeIndex) {
            if (direction == 1) {
                Vector2 newPosition = new Vector2(oldPosition.x - (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazeBlinky(elementInGrid, newPosition);
                blinkyIndex = getIndex(newPosition);
                batch.draw(blinky, blinkyPosition.x, blinkyPosition.y, blockSize, blockSize);
            } else if (direction == 2) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y + blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazeBlinky(elementInGrid, newPosition);
                blinkyIndex = getIndex(newPosition);
                batch.draw(blinky, blinkyPosition.x, blinkyPosition.y, blockSize, blockSize);
            } else if (direction == 3) {
                Vector2 newPosition = new Vector2(oldPosition.x + (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazeBlinky(elementInGrid, newPosition);
                blinkyIndex = getIndex(newPosition);
                batch.draw(blinky, blinkyPosition.x, blinkyPosition.y, blockSize, blockSize);
            } else if (direction == 4) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y - blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazeBlinky(elementInGrid, newPosition);
                blinkyIndex = getIndex(newPosition);
                batch.draw(blinky, blinkyPosition.x, blinkyPosition.y, blockSize, blockSize);
            }
        } else moveBlinky();
        lastDirection = direction;

    }

    public void movePinky() {
        Vector2 oldPosition = this.pinkyPosition;
        int direction = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        if (lastDirection != direction || pinkyIndex != blinkyIndex || pinkyIndex != inkyIndex || pinkyIndex != clydeIndex) {
            if (direction == 1) {
                Vector2 newPosition = new Vector2(oldPosition.x - (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazePinky(elementInGrid, newPosition);
                pinkyIndex = getIndex(newPosition);
                batch.draw(pinky, pinkyPosition.x, pinkyPosition.y, blockSize, blockSize);
            } else if (direction == 2) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y + blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazePinky(elementInGrid, newPosition);
                pinkyIndex = getIndex(newPosition);
                batch.draw(pinky, pinkyPosition.x, pinkyPosition.y, blockSize, blockSize);
            } else if (direction == 3) {
                Vector2 newPosition = new Vector2(oldPosition.x + (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazePinky(elementInGrid, newPosition);
                pinkyIndex = getIndex(newPosition);
                batch.draw(pinky, pinkyPosition.x, pinkyPosition.y, blockSize, blockSize);
            } else if (direction == 4) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y - blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazePinky(elementInGrid, newPosition);
                pinkyIndex = getIndex(newPosition);
                batch.draw(pinky, pinkyPosition.x, pinkyPosition.y, blockSize, blockSize);
            }
        } else movePinky();
        lastDirection = direction;
    }

    public void moveInky() {
        Vector2 oldPosition = this.inkyPosition;
        int direction = ThreadLocalRandom.current().nextInt(1, 4 + 1);

        if (lastDirection != direction || inkyIndex != blinkyIndex || inkyIndex != pinkyIndex || inkyIndex != clydeIndex) {
            if (direction == 1) {
                Vector2 newPosition = new Vector2(oldPosition.x - (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazeInky(elementInGrid, newPosition);
                inkyIndex = getIndex(newPosition);
                batch.draw(inky, inkyPosition.x, inkyPosition.y, blockSize, blockSize);
            } else if (direction == 2) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y + blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazeInky(elementInGrid, newPosition);
                inkyIndex = getIndex(newPosition);
                batch.draw(inky, inkyPosition.x, inkyPosition.y, blockSize, blockSize);
            } else if (direction == 3) {
                Vector2 newPosition = new Vector2(oldPosition.x + (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazeInky(elementInGrid, newPosition);
                inkyIndex = getIndex(newPosition);
                batch.draw(inky, inkyPosition.x, inkyPosition.y, blockSize, blockSize);
            } else if (direction == 4) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y - blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazeInky(elementInGrid, newPosition);
                inkyIndex = getIndex(newPosition);
                batch.draw(inky, inkyPosition.x, inkyPosition.y, blockSize, blockSize);
            }
        } else moveInky();
        lastDirection = direction;
    }

    public void moveClyde() {
        Vector2 oldPosition = this.clydePosition;
        int direction = ThreadLocalRandom.current().nextInt(1, 4 + 1);

        if (lastDirection != direction || clydeIndex != blinkyIndex || clydeIndex != pinkyIndex || clydeIndex != inkyIndex) {
            if (direction == 1) {
                Vector2 newPosition = new Vector2(oldPosition.x - (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazeClyde(elementInGrid, newPosition);
                batch.draw(clyde, clydePosition.x, clydePosition.y, blockSize, blockSize);
                clydeIndex = getIndex(newPosition);

            } else if (direction == 2) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y + blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazeClyde(elementInGrid, newPosition);
                batch.draw(clyde, clydePosition.x, clydePosition.y, blockSize, blockSize);
                clydeIndex = getIndex(newPosition);

            } else if (direction == 3) {
                Vector2 newPosition = new Vector2(oldPosition.x + (blockSize + 1), oldPosition.y);
                elementInGrid = getNewPosition(newPosition);
                checkMazeClyde(elementInGrid, newPosition);
                batch.draw(clyde, clydePosition.x, clydePosition.y, blockSize, blockSize);
                clydeIndex = getIndex(newPosition);

            } else if (direction == 4) {
                Vector2 newPosition = new Vector2(oldPosition.x, oldPosition.y - blockSize);
                elementInGrid = getNewPosition(newPosition);
                checkMazeClyde(elementInGrid, newPosition);
                batch.draw(clyde, clydePosition.x, clydePosition.y, blockSize, blockSize);
                clydeIndex = getIndex(newPosition);

            }
        } else moveClyde();
        lastDirection = direction;
    }

    public void pacCatIsDying(int pacCatIndex) {
        if (pacCatIndex == blinkyIndex || pacCatIndex == pinkyIndex || pacCatIndex == inkyIndex || pacCatIndex == clydeIndex) {
            if (empowered) {
                if (pacCatIndex == blinkyIndex) {
                    blinkyPosition.x = 12 * (blockSize + 1);
                    blinkyPosition.y = 14 * blockSize;
                    score += 100;
                } else if (pacCatIndex == pinkyIndex) {
                    pinkyPosition.x = 13 * (blockSize + 1);
                    pinkyPosition.y = 14 * blockSize;
                } else if (pacCatIndex == inkyIndex) {
                    inkyPosition.x = 16 * (blockSize + 1);
                    inkyPosition.y = 14 * blockSize;
                } else if (pacCatIndex == clydeIndex) {
                    clydePosition.x = 17 * (blockSize + 1);
                    clydePosition.y = 14 * blockSize;
                }
            } else if (hearts > 0) {
                hearts -= 1;
                System.out.println("hearts left: " + hearts);
                try {
                    Thread.sleep(250L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                catPosition.x = 14 * (blockSize + 1);
                catPosition.y = 10 * blockSize;
            } else if (hearts == 0){
                PacCatGame.INSTANCE.setScreen(new GameOverScreen());
            }

        }
    }

    public void timer(int seconds) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                empowered = false;
                timer.cancel();
            }
        }, seconds * 1000L);
    }


    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }
}
