package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;

import java.util.Random;

public class Entity {
    protected final Random random = new Random();
    public int x, y;
    public int xr = 6;
    public int yr = 6;
    public boolean removed;
    public Level level;

    public void render(Screen screen) {
    }

    public void tick() {
    }

    public void remove() {
        removed = true;
    }

    public final void init(Level level) {
        this.level = level;
    }

    public boolean intersects(int x0, int y0, int x1, int y1) {
        return !(x + xr < x0 || y + yr < y0 || x - xr > x1 || y - yr > y1);
    }

    public boolean blocks(Entity e) {
        return false;
    }

    public void hurt(Mob mob, int dmg, int attackDir) {
    }

    public void hurt(Tile tile, int x, int y, int dmg) {
    }

    public boolean move(int xa, int ya) {
        if (xa != 0 || ya != 0) {
            var stopped = xa == 0 || !move2(xa, 0);

            if (ya != 0 && move2(0, ya)) stopped = false;

            if (!stopped) {
                var xt = x >> 4;
                var yt = y >> 4;

                level.getTile(xt, yt).steppedOn(level, xt, yt, this);
            }

            return !stopped;
        }

        return true;
    }

    protected boolean move2(int xa, int ya) {
        if (xa != 0 && ya != 0) throw new IllegalArgumentException("Move2 can only move along one axis at a time!");

        var xto0 = ((x) - xr) >> 4;
        var yto0 = ((y) - yr) >> 4;
        var xto1 = ((x) + xr) >> 4;
        var yto1 = ((y) + yr) >> 4;

        var xt0 = ((x + xa) - xr) >> 4;
        var yt0 = ((y + ya) - yr) >> 4;
        var xt1 = ((x + xa) + xr) >> 4;
        var yt1 = ((y + ya) + yr) >> 4;

        for (var yt = yt0; yt <= yt1; yt++) {
            for (var xt = xt0; xt <= xt1; xt++) {
                if (xt >= xto0 && xt <= xto1 && yt >= yto0 && yt <= yto1) continue;

                level.getTile(xt, yt).bumpedInto(level, xt, yt, this);

                if (!level.getTile(xt, yt).mayPass(level, xt, yt, this)) {
                    return false;
                }
            }
        }

        var wasInside = level.getEntities(x - xr, y - yr, x + xr, y + yr);
        var isInside = level.getEntities(x + xa - xr, y + ya - yr, x + xa + xr, y + ya + yr);

        for (var e : isInside) {
            if (e == this) continue;

            e.touchedBy(this);
        }

        isInside.removeAll(wasInside);

        for (var e : isInside) {
            if (e == this) continue;

            if (e.blocks(this)) {
                return false;
            }
        }

        x += xa;
        y += ya;

        return true;
    }

    protected void touchedBy(Entity entity) {
    }

    public boolean isBlockableBy(Mob mob) {
        return true;
    }

    public void touchItem(ItemEntity itemEntity) {
    }

    public boolean canSwim() {
        return false;
    }

    public boolean interact(Player player, Item item, int attackDir) {
        return item.interact(player, this, attackDir);
    }

    public boolean use(Player player, int attackDir) {
        return false;
    }

    public int getLightRadius() {
        return 0;
    }
}
