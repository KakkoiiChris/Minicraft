package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.Level;

public class StoneTile extends Tile {
    public StoneTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {
        var rc1 = 111;
        var rc2 = 333;
        var rc3 = 555;

        screen.render(x * 16, y * 16, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
        screen.render(x * 16 + 8, y * 16, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
        screen.render(x * 16, y * 16 + 8, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
        screen.render(x * 16 + 8, y * 16 + 8, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return false;
    }
}
