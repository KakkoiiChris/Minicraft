package com.mojang.ld22.level.tile;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.Level;

public class StairsTile extends Tile {
    private final boolean leadsUp;

    public StairsTile(int id, boolean leadsUp) {
        super(id);

        this.leadsUp = leadsUp;
    }

    public void render(Screen screen, Level level, int x, int y) {
        var color = Color.get(level.dirtColor, 0, 333, 444);

        var xt = 0;

        if (leadsUp) xt = 2;

        screen.render(x * 16, y * 16, xt + 2 * 32, color, 0);
        screen.render(x * 16 + 8, y * 16, xt + 1 + 2 * 32, color, 0);
        screen.render(x * 16, y * 16 + 8, xt + 3 * 32, color, 0);
        screen.render(x * 16 + 8, y * 16 + 8, xt + 1 + 3 * 32, color, 0);
    }
}
