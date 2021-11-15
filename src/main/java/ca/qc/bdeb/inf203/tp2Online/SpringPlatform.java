package ca.qc.bdeb.inf203.tp2Online;

import javafx.scene.paint.Color;

public class SpringPlatform extends Platform {
    public SpringPlatform(double y, int i) {
        super(y, Color.LIGHTGREEN, i);
    }

    @Override
    public void collisionEffect(Player player) {
        player.bounce();
    }
}
