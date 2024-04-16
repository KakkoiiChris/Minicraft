package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;

public class RockTile extends Tile {
    public RockTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {
        var col = Color.get(444, 444, 333, 333);
        var transitionColor = Color.get(111, 444, 555, level.dirtColor);

        var u = level.getTile(x, y - 1) != this;
        var d = level.getTile(x, y + 1) != this;
        var l = level.getTile(x - 1, y) != this;
        var r = level.getTile(x + 1, y) != this;

        var ul = level.getTile(x - 1, y - 1) != this;
        var dl = level.getTile(x - 1, y + 1) != this;
        var ur = level.getTile(x + 1, y - 1) != this;
        var dr = level.getTile(x + 1, y + 1) != this;

        if (!u && !l) {
            if (!ul)
                screen.render(x * 16, y * 16, 0, col, 0);
            else
                screen.render(x * 16, y * 16, 7, transitionColor, 3);
        }
        else
            screen.render(x * 16, y * 16, (l ? 6 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);

        if (!u && !r) {
            if (!ur)
                screen.render(x * 16 + 8, y * 16, 1, col, 0);
            else
                screen.render(x * 16 + 8, y * 16, 8, transitionColor, 3);
        }
        else
            screen.render(x * 16 + 8, y * 16, (r ? 4 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);

        if (!d && !l) {
            if (!dl)
                screen.render(x * 16, y * 16 + 8, 2, col, 0);
            else
                screen.render(x * 16, y * 16 + 8, 7 + 32, transitionColor, 3);
        }
        else
            screen.render(x * 16, y * 16 + 8, (l ? 6 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);

        if (!d && !r) {
            if (!dr)
                screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
            else
                screen.render(x * 16 + 8, y * 16 + 8, 8 + 32, transitionColor, 3);
        }
        else
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 4 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return false;
    }

    public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
        hurt(level, x, y, dmg);
    }

    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        if (!(item instanceof ToolItem tool)) {
            return false;
        }

        if (tool.type != ToolType.PICKAXE) {
            return false;
        }

        if (!player.payStamina(4 - tool.level)) {
            return false;
        }

        hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10);

        return true;
    }

    public void hurt(Level level, int x, int y, int dmg) {
        var damage = level.getData(x, y) + dmg;

        level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
        level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));

        if (damage >= 50) {
            var count = random.nextInt(4) + 1;

            for (var i = 0; i < count; i++) {
                level.add(new ItemEntity(new ResourceItem(Resource.stone), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
            }

            count = random.nextInt(2);

            for (var i = 0; i < count; i++) {
                level.add(new ItemEntity(new ResourceItem(Resource.coal), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
            }

            level.setTile(x, y, Tile.dirt, 0);
        }
        else {
            level.setData(x, y, damage);
        }
    }

    public void tick(Level level, int xt, int yt) {
        var damage = level.getData(xt, yt);

        if (damage > 0) level.setData(xt, yt, damage - 1);
    }
}
