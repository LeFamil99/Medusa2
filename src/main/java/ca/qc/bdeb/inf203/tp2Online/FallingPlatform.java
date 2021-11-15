package ca.qc.bdeb.inf203.tp2Online;

import javafx.scene.paint.Color;

public class FallingPlatform extends Platform {
    private boolean touched;

    public FallingPlatform(double y, int i) {
        super(y, Color.BLACK, i);
        touched = false;
    }

    @Override
    public void collisionEffect(Player player) {
        super.collisionEffect(player);
        touched = true;
    }

    @Override
    public void collisionEnd() {
        if(touched) {
            vy = 200;
        }
    }
}
