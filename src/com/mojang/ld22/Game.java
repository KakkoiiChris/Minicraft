package com.mojang.ld22;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.gfx.SpriteSheet;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.screen.Menu;
import com.mojang.ld22.screen.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Objects;

public class Game extends Canvas implements Runnable {
    public static final String NAME = "Minicraft";
    public static final int HEIGHT = 120;
    public static final int WIDTH = 160;
    private static final int SCALE = 3;

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private boolean running = false;
    private Screen screen;
    private Screen lightScreen;
    private final InputHandler input = new InputHandler(this);

    private final int[] colors = new int[256];
    private int tickCount = 0;
    public int gameTime = 0;

    private Level level;
    private Level[] levels = new Level[5];
    private int currentLevel = 3;
    public Player player;

    public Menu menu;
    private int playerDeadTime;
    private int pendingLevelChange;
    private int wonTimer = 0;
    public boolean hasWon = false;

    public void setMenu(Menu menu) {
        this.menu = menu;

        if (menu != null) menu.init(this, input);
    }

    public void start() {
        running = true;

        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    public void resetGame() {
        playerDeadTime = 0;
        wonTimer = 0;
        gameTime = 0;
        hasWon = false;

        levels = new Level[5];
        currentLevel = 3;

        levels[4] = new Level(128, 128, 1, null);
        levels[3] = new Level(128, 128, 0, levels[4]);
        levels[2] = new Level(128, 128, -1, levels[3]);
        levels[1] = new Level(128, 128, -2, levels[2]);
        levels[0] = new Level(128, 128, -3, levels[1]);

        level = levels[currentLevel];
        player = new Player(this, input);
        player.findStartPos(level);

        level.add(player);

        for (var i = 0; i < 5; i++) {
            levels[i].trySpawn(5000);
        }
    }

    private void init() {
        var pp = 0;

        for (var r = 0; r < 6; r++) {
            for (var g = 0; g < 6; g++) {
                for (var b = 0; b < 6; b++) {
                    var rr = (r * 255 / 5);
                    var gg = (g * 255 / 5);
                    var bb = (b * 255 / 5);
                    var mid = (rr * 30 + gg * 59 + bb * 11) / 100;

                    var r1 = ((rr + mid) / 2) * 230 / 255 + 10;
                    var g1 = ((gg + mid) / 2) * 230 / 255 + 10;
                    var b1 = ((bb + mid) / 2) * 230 / 255 + 10;

                    colors[pp++] = r1 << 16 | g1 << 8 | b1;
                }
            }
        }

        try {
            var image = ImageIO.read(Objects.requireNonNull(Game.class.getResourceAsStream("/icons.png")));

            screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(image));
            lightScreen = new Screen(WIDTH, HEIGHT, new SpriteSheet(image));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        resetGame();

        setMenu(new TitleMenu());
    }

    public void run() {
        final var FPS = 60.0;
        final var NPU = 1E9 / FPS;

        var then = System.nanoTime();

        var delta = 0.0;
        var timer = 0.0;

        var frames = 0;
        var ticks = 0;

        init();

        while (running) {
            var now = System.nanoTime();
            var elapsed = (now - then) / NPU;
            then = now;

            delta += elapsed;
            timer += elapsed;

            var shouldRender = false;

            while (delta >= 1) {
                ticks++;

                tick();

                delta--;

                shouldRender = true;
            }

            if (shouldRender) {
                frames++;

                render();
            }

            if (timer >= FPS) {
                timer -= FPS;

                System.out.printf("%d ticks, %d fps%n", ticks, frames);

                frames = ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;

        if (!hasFocus()) {
            input.releaseAll();
        }
        else {
            if (!player.removed && !hasWon) gameTime++;

            input.tick();

            if (menu != null) {
                menu.tick();
            }
            else {
                if (player.removed) {
                    playerDeadTime++;

                    if (playerDeadTime > 60) {
                        setMenu(new DeadMenu());
                    }
                }
                else {
                    if (pendingLevelChange != 0) {
                        setMenu(new LevelTransitionMenu(pendingLevelChange));

                        pendingLevelChange = 0;
                    }
                }

                if (wonTimer > 0) {
                    if (--wonTimer == 0) {
                        setMenu(new WonMenu());
                    }
                }

                level.tick();

                Tile.tickCount++;
            }
        }
    }

    public void changeLevel(int dir) {
        level.remove(player);

        currentLevel += dir;

        level = levels[currentLevel];

        player.x = (player.x >> 4) * 16 + 8;
        player.y = (player.y >> 4) * 16 + 8;

        level.add(player);
    }

    public void render() {
        var bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        var xScroll = player.x - screen.w / 2;
        var yScroll = player.y - (screen.h - 8) / 2;

        if (xScroll < 16) xScroll = 16;
        if (yScroll < 16) yScroll = 16;
        if (xScroll > level.w * 16 - screen.w - 16) xScroll = level.w * 16 - screen.w - 16;
        if (yScroll > level.h * 16 - screen.h - 16) yScroll = level.h * 16 - screen.h - 16;

        if (currentLevel > 3) {
            var col = Color.get(20, 20, 121, 121);

            for (var y = 0; y < 14; y++) {
                for (var x = 0; x < 24; x++) {
                    screen.render(x * 8 - ((xScroll / 4) & 7), y * 8 - ((yScroll / 4) & 7), 0, col, 0);
                }
            }
        }

        level.renderBackground(screen, xScroll, yScroll);
        level.renderSprites(screen, xScroll, yScroll);

        if (currentLevel < 3) {
            lightScreen.clear(0);

            level.renderLight(lightScreen, xScroll, yScroll);

            screen.overlay(lightScreen, xScroll, yScroll);
        }

        renderGui();

        if (!hasFocus()) renderFocusNagger();

        for (var y = 0; y < screen.h; y++) {
            for (var x = 0; x < screen.w; x++) {
                var cc = screen.pixels[x + y * screen.w];

                if (cc < 255) pixels[x + y * WIDTH] = colors[cc];
            }
        }

        var g = bs.getDrawGraphics();

        g.fillRect(0, 0, getWidth(), getHeight());

        int ww = WIDTH * 3;
        int hh = HEIGHT * 3;
        int xo = (getWidth() - ww) / 2;
        int yo = (getHeight() - hh) / 2;

        g.drawImage(image, xo, yo, ww, hh, null);

        g.dispose();
        bs.show();
    }

    private void renderGui() {
        for (var y = 0; y < 2; y++) {
            for (var x = 0; x < 20; x++) {
                screen.render(x * 8, screen.h - 16 + y * 8, 384, Color.get(0, 0, 0, 0), 0);
            }
        }

        for (var i = 0; i < 10; i++) {
            if (i < player.health)
                screen.render(i * 8, screen.h - 16, 384, Color.get(0, 200, 500, 533), 0);
            else
                screen.render(i * 8, screen.h - 16, 384, Color.get(0, 100, 0, 0), 0);

            if (player.staminaRechargeDelay > 0) {
                if (player.staminaRechargeDelay / 4 % 2 == 0)
                    screen.render(i * 8, screen.h - 8, 385, Color.get(0, 555, 0, 0), 0);
                else
                    screen.render(i * 8, screen.h - 8, 385, Color.get(0, 110, 0, 0), 0);
            }
            else {
                if (i < player.stamina)
                    screen.render(i * 8, screen.h - 8, 385, Color.get(0, 220, 550, 553), 0);
                else
                    screen.render(i * 8, screen.h - 8, 385, Color.get(0, 110, 0, 0), 0);
            }
        }

        if (player.activeItem != null) {
            player.activeItem.renderInventory(screen, 80, screen.h - 16);
        }

        if (menu != null) {
            menu.render(screen);
        }
    }

    private void renderFocusNagger() {
        var msg = "Click to focus!";
        var xx = (WIDTH - msg.length() * 8) / 2;
        var yy = (HEIGHT - 8) / 2;
        var w = msg.length();
        var h = 1;

        screen.render(xx - 8, yy - 8, 416, Color.get(-1, 1, 5, 445), 0);
        screen.render(xx + w * 8, yy - 8, 416, Color.get(-1, 1, 5, 445), 1);
        screen.render(xx - 8, yy + 8, 416, Color.get(-1, 1, 5, 445), 2);
        screen.render(xx + w * 8, yy + 8, 416, Color.get(-1, 1, 5, 445), 3);

        for (var x = 0; x < w; x++) {
            screen.render(xx + x * 8, yy - 8, 417, Color.get(-1, 1, 5, 445), 0);
            screen.render(xx + x * 8, yy + 8, 417, Color.get(-1, 1, 5, 445), 2);
        }

        for (var y = 0; y < h; y++) {
            screen.render(xx - 8, yy, 418, Color.get(-1, 1, 5, 445), 0);
            screen.render(xx + w * 8, yy, 418, Color.get(-1, 1, 5, 445), 1);
        }

        if ((tickCount / 20) % 2 == 0) {
            Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
        }
        else {
            Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
        }
    }

    public void scheduleLevelChange(int dir) {
        pendingLevelChange = dir;
    }

    public static void main(String[] args) {
        var game = new Game();

        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        var frame = new JFrame(Game.NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    public void won() {
        wonTimer = 180;
        hasWon = true;
    }
}
