package ca.qc.bdeb.inf203.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Platform extends GameObject{

    protected Color color;
    protected int i;
    protected boolean touched;

    public Platform(double x, double y, double w, double h, Color color, int i) {
        super(x, y, w, h);
        this.color = color;
        this.i = i;
        this.touched = false;
    }

    public Platform(double x, double y, Color color, int i) {
        this(x, y, 0, 10, color, i);

        Random rnd = new Random();
        this.w = rnd.nextDouble() * 95 + 80;
    }

    public Platform(double y, Color color, int i) {
        this(0, y, color, i);

        Random rnd = new Random();
        this.x = rnd.nextDouble() * (Main.WIDTH - this.w);
    }

    public Platform(double y, int i) {
        this(0, y, Color.rgb(230, 134, 58), i);

        Random rnd = new Random();
        this.x = rnd.nextDouble() * (Main.WIDTH - this.w);
    }


    public void update(double deltaTime) {
        super.PhysicalUpdate(deltaTime);


    }

    public void draw(GraphicsContext ctx, Camera camera) {
        ctx.setFill(color);
        ctx.fillRect(x, y + camera.getY(), w, h);
    }

    public void drawDebug(GraphicsContext ctx, Camera camera) {
        ctx.setFill(color);
        if(touched) {
            ctx.setFill(Color.YELLOW);
        }
        ctx.fillRect(x, y + camera.getY(), w, h);
    }

    public void verifyCollision(Player player) {
        if(player.vy >= 0 && player.getBottom() >= getTop() && player.getRight() > getLeft() && player.getLeft() < getRight() && player.getLastBottomY() <= getTop()) {
            collisionEffect(player);
        } else collisionEnd();
    }

    public void collisionEffect(Player player) {
        player.setGround(getTop());
        touched = true;
    }

    public void collisionEnd() {
        touched = false;
    }

    public void checkForDelete(Game game, Camera camera) {
        if(getTop() > Main.HEIGHT - camera.getY() + 35) {
            game.deletePlatform(i);
        }
    }
}
