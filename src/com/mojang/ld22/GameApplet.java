package com.mojang.ld22;

import java.applet.Applet;
import java.awt.*;

public class GameApplet extends Applet {
    private final Game game = new Game();

    public void init() {
        setLayout(new BorderLayout());

        add(game, BorderLayout.CENTER);
    }

    public void start() {
        game.start();
    }

    public void stop() {
        game.stop();
    }
}
