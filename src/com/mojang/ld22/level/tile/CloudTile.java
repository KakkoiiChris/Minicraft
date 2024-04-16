package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;

public class CloudTile extends Tile {
    public CloudTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {
        var col = Color.get(444, 444, 555, 555);
        var transitionColor = Color.get(333, 444, 555, -1);

        var u = level.getTile(x, y - 1) == Tile.infiniteFall;
        var d = level.getTile(x, y + 1) == Tile.infiniteFall;
        var l = level.getTile(x - 1, y) == Tile.infiniteFall;
        var r = level.getTile(x + 1, y) == Tile.infiniteFall;

        var ul = level.getTile(x - 1, y - 1) == Tile.infiniteFall;
        var dl = level.getTile(x - 1, y + 1) == Tile.infiniteFall;
        var ur = level.getTile(x + 1, y - 1) == Tile.infiniteFall;
        var dr = level.getTile(x + 1, y + 1) == Tile.infiniteFall;

        if (!u && !l) {
            if (!ul)
                screen.render(x * 16, y * 16, 17, col, 0);
            else
                screen.render(x * 16, y * 16, 7, transitionColor, 3);
        }
        else
            screen.render(x * 16, y * 16, (l ? 6 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);

        if (!u && !r) {
            if (!ur)
                screen.render(x * 16 + 8, y * 16, 18, col, 0);
            else
                screen.render(x * 16 + 8, y * 16, 8, transitionColor, 3);
        }
        else
            screen.render(x * 16 + 8, y * 16, (r ? 4 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);

        if (!d && !l) {
            if (!dl)
                screen.render(x * 16, y * 16 + 8, 20, col, 0);
            else
                screen.render(x * 16, y * 16 + 8, 7 + 32, transitionColor, 3);
        }
        else
            screen.render(x * 16, y * 16 + 8, (l ? 6 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);

        if (!d && !r) {
            if (!dr)
                screen.render(x * 16 + 8, y * 16 + 8, 19, col, 0);
            else
                screen.render(x * 16 + 8, y * 16 + 8, 8 + 32, transitionColor, 3);
        }
        else
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 4 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);
    }

    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        if (!(item instanceof ToolItem tool)) {
            return false;
        }

        if (tool.type != ToolType.SHOVEL) {
            return false;
        }

        if (!player.payStamina(5)) {
            return false;
        }

        var count = random.nextInt(2) + 1;

        for (var i = 0; i < count; i++) {
            level.add(new ItemEntity(new ResourceItem(Resource.cloud), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
        }

        return true;
    }
}
