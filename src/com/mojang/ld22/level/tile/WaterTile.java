package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.Level;

import java.util.Random;

public class WaterTile extends Tile {
    public WaterTile(int id) {
        super(id);

        connectsToSand = true;
        connectsToWater = true;
    }

    private final Random wRandom = new Random();

    public void render(Screen screen, Level level, int x, int y) {
        wRandom.setSeed((tickCount + (x / 2 - y) * 4311L) / 10 * 54687121L + x * 3271612L + y * 3412987161L);

        var col = Color.get(5, 5, 115, 115);
        var transitionColor1 = Color.get(3, 5, level.dirtColor - 111, level.dirtColor);
        var transitionColor2 = Color.get(3, 5, level.sandColor - 110, level.sandColor);

        var u = !level.getTile(x, y - 1).connectsToWater;
        var d = !level.getTile(x, y + 1).connectsToWater;
        var l = !level.getTile(x - 1, y).connectsToWater;
        var r = !level.getTile(x + 1, y).connectsToWater;

        var su = u && level.getTile(x, y - 1).connectsToSand;
        var sd = d && level.getTile(x, y + 1).connectsToSand;
        var sl = l && level.getTile(x - 1, y).connectsToSand;
        var sr = r && level.getTile(x + 1, y).connectsToSand;

        if (!u && !l) {
            screen.render(x * 16, y * 16, wRandom.nextInt(4), col, wRandom.nextInt(4));
        }
        else
            screen.render(x * 16, y * 16, (l ? 14 : 15) + (u ? 0 : 1) * 32, (su || sl) ? transitionColor2 : transitionColor1, 0);

        if (!u && !r) {
            screen.render(x * 16 + 8, y * 16, wRandom.nextInt(4), col, wRandom.nextInt(4));
        }
        else
            screen.render(x * 16 + 8, y * 16, (r ? 16 : 15) + (u ? 0 : 1) * 32, (su || sr) ? transitionColor2 : transitionColor1, 0);

        if (!d && !l) {
            screen.render(x * 16, y * 16 + 8, wRandom.nextInt(4), col, wRandom.nextInt(4));
        }
        else
            screen.render(x * 16, y * 16 + 8, (l ? 14 : 15) + (d ? 2 : 1) * 32, (sd || sl) ? transitionColor2 : transitionColor1, 0);

        if (!d && !r) {
            screen.render(x * 16 + 8, y * 16 + 8, wRandom.nextInt(4), col, wRandom.nextInt(4));
        }
        else
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 16 : 15) + (d ? 2 : 1) * 32, (sd || sr) ? transitionColor2 : transitionColor1, 0);
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return e.canSwim();
    }

    public void tick(Level level, int xt, int yt) {
        var xn = xt;
        var yn = yt;

        if (random.nextBoolean())
            xn += random.nextInt(2) * 2 - 1;
        else
            yn += random.nextInt(2) * 2 - 1;

        if (level.getTile(xn, yn) == Tile.hole) {
            level.setTile(xn, yn, this, 0);
        }
    }
}
