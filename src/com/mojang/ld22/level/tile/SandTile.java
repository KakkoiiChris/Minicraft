package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;

public class SandTile extends Tile {
    public SandTile(int id) {
        super(id);

        connectsToSand = true;
    }

    public void render(Screen screen, Level level, int x, int y) {
        var col = Color.get(level.sandColor + 2, level.sandColor, level.sandColor - 110, level.sandColor - 110);
        var transitionColor = Color.get(level.sandColor - 110, level.sandColor, level.sandColor - 110, level.dirtColor);

        var u = !level.getTile(x, y - 1).connectsToSand;
        var d = !level.getTile(x, y + 1).connectsToSand;
        var l = !level.getTile(x - 1, y).connectsToSand;
        var r = !level.getTile(x + 1, y).connectsToSand;

        var steppedOn = level.getData(x, y) > 0;

        if (!u && !l) {
            if (!steppedOn)
                screen.render(x * 16, y * 16, 0, col, 0);
            else
                screen.render(x * 16, y * 16, 3 + 32, col, 0);
        }
        else
            screen.render(x * 16, y * 16, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);

        if (!u && !r) {
            screen.render(x * 16 + 8, y * 16, 1, col, 0);
        }
        else
            screen.render(x * 16 + 8, y * 16, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);

        if (!d && !l) {
            screen.render(x * 16, y * 16 + 8, 2, col, 0);
        }
        else
            screen.render(x * 16, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);

        if (!d && !r) {
            if (!steppedOn)
                screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
            else
                screen.render(x * 16 + 8, y * 16 + 8, 3 + 32, col, 0);
        }
        else
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
    }

    public void tick(Level level, int x, int y) {
        var d = level.getData(x, y);

        if (d > 0) level.setData(x, y, d - 1);
    }

    public void steppedOn(Level level, int x, int y, Entity entity) {
        if (entity instanceof Mob) {
            level.setData(x, y, 10);
        }
    }

    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        if (!(item instanceof ToolItem tool)) {
            return false;
        }

        if (tool.type != ToolType.SHOVEL) {
            return false;
        }

        if (!player.payStamina(4 - tool.level)) {
            return false;
        }

        level.setTile(xt, yt, Tile.dirt, 0);

        level.add(new ItemEntity(new ResourceItem(Resource.sand), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));

        return true;
    }
}
