package ca.qc.bdeb.inf203.tp2;

import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Input {
    static public HashMap<KeyCode, Boolean> map = new HashMap<>();

    static boolean isKeyPressed(KeyCode key) {
        return map.getOrDefault(key, false);
    }

    static void setKeyPressed(KeyCode key, boolean bool) {
        map.put(key, bool);
    }
}
