package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.Level;

public class HoleTile extends Tile {
    public HoleTile(int id) {
        super(id);

        connectsToSand = true;
        connectsToWater = true;
        connectsToLava = true;
    }

    public void render(Screen screen, Level level, int x, int y) {
        var col = Color.get(111, 111, 110, 110);
        var transitionColor1 = Color.get(3, 111, level.dirtColor - 111, level.dirtColor);
        var transitionColor2 = Color.get(3, 111, level.sandColor - 110, level.sandColor);

        var u = !level.getTile(x, y - 1).connectsToLiquid();
        var d = !level.getTile(x, y + 1).connectsToLiquid();
        var l = !level.getTile(x - 1, y).connectsToLiquid();
        var r = !level.getTile(x + 1, y).connectsToLiquid();

        var su = u && level.getTile(x, y - 1).connectsToSand;
        var sd = d && level.getTile(x, y + 1).connectsToSand;
        var sl = l && level.getTile(x - 1, y).connectsToSand;
        var sr = r && level.getTile(x + 1, y).connectsToSand;

        if (!u && !l) {
            screen.render(x * 16, y * 16, 0, col, 0);
        }
        else
            screen.render(x * 16, y * 16, (l ? 14 : 15) + (u ? 0 : 1) * 32, (su || sl) ? transitionColor2 : transitionColor1, 0);

        if (!u && !r) {
            screen.render(x * 16 + 8, y * 16, 1, col, 0);
        }
        else
            screen.render(x * 16 + 8, y * 16, (r ? 16 : 15) + (u ? 0 : 1) * 32, (su || sr) ? transitionColor2 : transitionColor1, 0);

        if (!d && !l) {
            screen.render(x * 16, y * 16 + 8, 2, col, 0);
        }
        else
            screen.render(x * 16, y * 16 + 8, (l ? 14 : 15) + (d ? 2 : 1) * 32, (sd || sl) ? transitionColor2 : transitionColor1, 0);

        if (!d && !r) {
            screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
        }
        else
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 16 : 15) + (d ? 2 : 1) * 32, (sd || sr) ? transitionColor2 : transitionColor1, 0);
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return e.canSwim();
    }
}
