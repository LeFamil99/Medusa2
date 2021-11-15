package ca.qc.bdeb.inf203.tp2Online;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Bubble extends GameObject {
    private Color color;

    public Bubble(double x) {
        super(x, Main.HEIGHT, 0, 0, 0, 0);
        this.color = Color.rgb(0, 0, 255, 0.4);
        init();
    }

    private void init() {
        Random rnd = new Random();
        x += rnd.nextDouble() * 40 - 20;
        double diameter = rnd.nextDouble() * 30 + 10;
        w = diameter;
        h = diameter;
        vy = -(rnd.nextDouble() * 100 + 350);
    }


    @Override
    public void draw(GraphicsContext ctx, Camera camera) {
        ctx.setFill(color);
        ctx.fillOval(x, y, w, h);
    }
}
