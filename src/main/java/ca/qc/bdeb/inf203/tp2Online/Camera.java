package ca.qc.bdeb.inf203.tp2Online;

public class Camera {
    private double y;
    private double vy;
    private double ay;

    public Camera(double y, double ay) {
        this.y = y;
        this.vy = 0;
        this.ay = ay;
    }

    public void update(double deltaTime, boolean debug) {
        if(!debug) {
            vy += Math.min(ay * deltaTime, 60);
            y += vy * deltaTime;
        }
    }

    public double getY() {
        return y;
    }

    public void moveY(double y) {
        this.y += y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
