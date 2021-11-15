package ca.qc.bdeb.inf203.tp2Online;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class Player extends GameObject {
    private char direction;
    private double delay;
    private HashMap<String, Image> imgs;

    private boolean isGrounded;
    private double groundY;
    private double lastY;

    public Player(double x, double y) {
        super(x, y, 50, 50, 0, 0, 0, Main.GRAVITY);

        direction = 'd';
        delay = 0;
        isGrounded = false;
        groundY = 0;
        lastY = 0;

        imgs = new HashMap<>();
        for(int i = 1; i <= 6; i++) {
            imgs.put("d" + i, new Image("meduse" + i + ".png"));
            imgs.put("g" + i, new Image("meduse" + i + "-g.png"));
        }
    }

    @Override
    public void draw(GraphicsContext ctx, Camera camera) {
        double frameRate = 1d / 8;
        int frame = (int)((Math.floor(delay / frameRate) % 6) + 1);
        ctx.drawImage(imgs.get(Character.toString(direction) + frame), x, y + camera.getY(), w, h);
    }

    public void drawDebug(GraphicsContext ctx, Camera camera) {
        double frameRate = 1d / 8;
        int frame = (int)((Math.floor(delay / frameRate) % 6) + 1);
        ctx.drawImage(imgs.get(Character.toString(direction) + frame), x, y + camera.getY(), w, h);

        ctx.setFill(Color.rgb(255, 0, 0, 0.3));
        ctx.fillRect(getLeft(), getTop() + camera.getY(), w, h);
    }

    @Override
    public void update(double deltaTime, Camera camera) {
        delay += deltaTime;

        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        boolean jump = Input.isKeyPressed(KeyCode.UP) || Input.isKeyPressed(KeyCode.SPACE);

        //---------- X Update

        if(left) {
            ax = -1200;
            direction = 'g';
        }
        else if(right) {
            ax = 1200;
            direction = 'd';
        }
        else {
            ax = 0;

            int normalizedSpeed = vx > 0 ? 1 : -1;
            double breakingAcc = -normalizedSpeed * 1200;

            vx += breakingAcc * deltaTime;

            int newNormalizedSpeed = vx > 0 ? 1 : -1;

            if(newNormalizedSpeed != normalizedSpeed) {
                vx = 0;
            }
        }

        double maxSpeed = 500;

        if(vx > maxSpeed) {
            vx = maxSpeed;
        } else if (vx < -maxSpeed) {
            vx = -maxSpeed;
        }

        if(getLeft() < 0) {
            x = 0;
            vx = -vx;
        } else if(getRight() > Main.WIDTH) {
            x = Main.WIDTH - w;
            vx = -vx;
        }

        //---------- Y Update

        if(isGrounded) {
            if(jump) {
                vy = -600;
                isGrounded = false;
            } else vy = 0;

        }

        lastY = y;

        super.PhysicalUpdate(deltaTime);

        if((y + camera.getY()) < (0.25 * Main.HEIGHT)) {
            camera.moveY(-((y + camera.getY()) - (0.25 * Main.HEIGHT)));
        }

        if (isGrounded) this.y = Math.min(this.y, groundY - h);

        isGrounded = false;
    }

    public void setGround(double y) {
        isGrounded = true;
        groundY = y;
        this.y = Math.min(this.y, groundY - h);
    }

    public void bounce() {
        vy *= -1.5;

    }

    public double getLastBottomY() {
        return lastY + h;
    }

    public boolean isGrounded() {
        return isGrounded;
    }
}
