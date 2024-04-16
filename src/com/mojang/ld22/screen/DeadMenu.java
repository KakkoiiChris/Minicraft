package com.mojang.ld22.screen;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class DeadMenu extends Menu {
    private int inputDelay = 60;

    public void tick() {
        if (inputDelay > 0) {
            inputDelay--;
        }
        else if (input.attack.clicked || input.menu.clicked) {
            game.setMenu(new TitleMenu());
        }
    }

    public void render(Screen screen) {
        Font.renderFrame(screen, "", 1, 3, 18, 9);
        Font.draw("You died! Aww!", screen, 2 * 8, 4 * 8, Color.get(-1, 555, 555, 555));

        var seconds = game.gameTime / 60;
        var minutes = seconds / 60;
        var hours = minutes / 60;

        minutes %= 60;
        seconds %= 60;

        var timeString = (hours > 0)
            ? "%dh%s%dm".formatted(hours, minutes < 10 ? "0" : "", minutes)
            : "%dm %s%ds".formatted(minutes, seconds < 10 ? "0" : "", seconds);

        Font.draw("Time:", screen, 2 * 8, 5 * 8, Color.get(-1, 555, 555, 555));
        Font.draw(timeString, screen, (2 + 5) * 8, 5 * 8, Color.get(-1, 550, 550, 550));
        Font.draw("Score:", screen, 2 * 8, 6 * 8, Color.get(-1, 555, 555, 555));
        Font.draw("" + game.player.score, screen, (2 + 6) * 8, 6 * 8, Color.get(-1, 550, 550, 550));
        Font.draw("Press C to lose", screen, 2 * 8, 8 * 8, Color.get(-1, 333, 333, 333));
    }
}
