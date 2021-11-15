package ca.qc.bdeb.inf203.tp2;

import javafx.scene.paint.Color;

public class MovingPlatform extends Platform {

    double speed;
    double pivotDelay;
    double delay;

    public MovingPlatform(double y, int i) {
        super(y, Color.rgb(184, 25, 36), i);

        this.speed = 120;
        this.pivotDelay = 2.5;
        this.delay = pivotDelay / 2;
    }

    @Override
    public void update(double deltaTime) {
        delay += deltaTime;
        int rawDirection = (int) Math.floor(delay / pivotDelay) % 2;
        int direction = rawDirection == 0 ? 1 : -1;
        vx = direction * speed;

        super.update(deltaTime);
    }


}
