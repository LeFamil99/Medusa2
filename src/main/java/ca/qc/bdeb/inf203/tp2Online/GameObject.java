package ca.qc.bdeb.inf203.tp2Online;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {
    protected double x, y;
    protected double w, h;

    protected double vx, vy;
    protected double ax, ay;

    public GameObject(double x, double y, double w, double h) {
        this(x, y, w, h, 0, 0, 0, 0);
    }

    public GameObject(double x, double y, double w, double h, double vx, double vy) {
        this(x, y, w, h, vx, vy, 0, 0);
    }

    public GameObject(double x, double y, double w, double h, double vx, double vy, double ax, double ay) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
    }

    public void update(double deltaTime, Camera camera) {
        update(deltaTime);
    }

    public void update(double deltaTime) {
        PhysicalUpdate(deltaTime);
    }

    public void PhysicalUpdate(double deltaTime) {
        vx += ax * deltaTime;
        vy += ay * deltaTime;

        x += vx * deltaTime;
        y += vy * deltaTime;
    }

    public abstract void draw(GraphicsContext ctx, Camera camera);

    public double getTop() {
        return y;
    }
    public double getBottom() {
        return y + h;
    }
    public double getLeft() {
        return x;
    }
    public double getRight() {
        return x + w;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }
}
