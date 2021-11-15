package ca.qc.bdeb.inf203.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class Game {
    private Player player;
    private Platform[] platforms = new Platform[6];
    private Bubble[][] bubbleGroups = new Bubble[3][5];
    private Camera camera = new Camera(0, 2);
    private double spawnY;
    private double delay;
    private double endDelay;
    private boolean pause;
    private boolean activeGame;
    private boolean debug;

    /*public Game() {
        player = new Player(150, 270);
        platforms[0] = (new Platform(125, 435, 100, 10, Color.WHITE, 0));
        delay = 0;
        pause = false;
        activeGame = true;
        spawnY = 335;

        for (int i = 1; i < platforms.length; i++) {
            spawnPlatform(i);
        }
        spawnBubbleGroup();
    }*/

    public void newGame() {
        camera.setY(0);
        player = new Player(150, 270);
        platforms[0] = (new Platform(125, 435, 100, 10, Color.WHITE, 0));
        delay = 0;
        pause = false;
        activeGame = true;
        spawnY = 335;
        endDelay = 0;
        debug = false;


        for (int i = 1; i < platforms.length; i++) {
            spawnPlatform(i);
        }
        spawnBubbleGroups();
    }

    public void spawnPlatform(int i) {
        Random rnd = new Random();
        double choice =  rnd.nextDouble();
        if(choice < 0.15) {
            platforms[i] = (new FallingPlatform(spawnY, i));
        } else if(choice < 0.3) {
            platforms[i] = (new SpringPlatform(spawnY, i));
        } else if (choice < 0.5) {
            platforms[i] = (new MovingPlatform(spawnY, i));
        } else platforms[i] = (new Platform(spawnY, i));
        spawnY -= 100;
    }

    public void spawnBubbleGroups() {
        for(int i = 0; i < 3; i++) {
            spawnBubbleGroup(i);
        }
    }

    public void spawnBubbleGroup(int i) {
        Random rnd = new Random();
        double x = rnd.nextDouble() * Main.WIDTH;

        for(int j = 0; j < bubbleGroups[i].length; j++) {
            bubbleGroups[i][j] = new Bubble(x);
        }
    }

    public void update(double deltaTime) {
        if(!pause) {
            delay += deltaTime;
            player.update(deltaTime, camera);
            for (Bubble[] bubbles : bubbleGroups) {
                for(Bubble bubble : bubbles) {
                    bubble.update(deltaTime);
                }
            }
            for (Platform platform : platforms) {
                platform.update(deltaTime);
                platform.verifyCollision(player);
                platform.checkForDelete(this, camera);
            }
            camera.update(deltaTime, debug);

            final double BUBBLE_DELAY = 3;
            if(delay > BUBBLE_DELAY) {
                delay -= BUBBLE_DELAY;
                spawnBubbleGroups();
            }
        }
        if(!activeGame) {
            endDelay += deltaTime;
            if(endDelay > 3) {
                Main.endGame((int)Math.floor(camera.getY()));
            }
        }
        verifyLoss();
    }

    private void verifyLoss() {
        if(player.getTop() + camera.getY() > Main.HEIGHT) {
            pause = true;
            activeGame = false;
        }
    }

    public void draw(GraphicsContext ctx) {
        ctx.setFill(Color.DARKBLUE);
        ctx.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        for (Bubble[] bubbles : bubbleGroups) {
            for(Bubble bubble : bubbles) {
                bubble.draw(ctx, camera);
            }
        }

        if(debug) {
            for (Platform platform : platforms) {
                platform.drawDebug(ctx, camera);
            }
            player.drawDebug(ctx, camera);

            text(ctx, "Position = (" + (int)player.getLeft() + ", " + (int)player.getTop() + ")", 10, 10, 13, Color.WHITE, TextAlignment.LEFT);
            text(ctx, "Vitesse = (" + (int)player.getVx() + ", " + (int)player.getVy() + ")", 10, 26, 14, Color.WHITE, TextAlignment.LEFT);
            text(ctx, "Acc = (" + (int)player.getAx() + ", " + (int)player.getAy() + ")", 10, 42, 14, Color.WHITE, TextAlignment.LEFT);
            text(ctx, "Touche le sol? " + (player.isGrounded() ? "oui" : "non"), 10, 58, 14, Color.WHITE, TextAlignment.LEFT);
        } else {
            for (Platform platform : platforms) {
                platform.draw(ctx, camera);
            }
            player.draw(ctx, camera);
        }

        text(ctx, (int)Math.floor(camera.getY()) + "px", Main.WIDTH / 2, 50, 30, Color.WHITE, TextAlignment.CENTER);

        if(!activeGame) {
            text(ctx, "Game Over", Main.WIDTH / 2, Main.HEIGHT / 2, 35, Color.RED, TextAlignment.CENTER);
        }
    }

    public void text(GraphicsContext ctx, String str, double x, double y, double size, Color color, TextAlignment align) {
        ctx.setTextAlign(align);
        ctx.setFont(Font.font("Courier New", FontWeight.BOLD, size));
        ctx.setFill(color);

        ctx.fillText(str, x, y);
    }

    public void deletePlatform(int i) {
        spawnPlatform(i);
    }

    public void toggleDebug() {
        debug = !debug;
    }
}
