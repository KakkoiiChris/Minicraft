package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.AirWizard;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.level.Level;

public class CloudCactusTile extends Tile {
    public CloudCactusTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {
        var color = Color.get(444, 111, 333, 555);

        screen.render(x * 16, y * 16, 17 + 32, color, 0);
        screen.render(x * 16 + 8, y * 16, 18 + 32, color, 0);
        screen.render(x * 16, y * 16 + 8, 17 + 2 * 32, color, 0);
        screen.render(x * 16 + 8, y * 16 + 8, 18 + 2 * 32, color, 0);
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return e instanceof AirWizard;
    }

    public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
        hurt(level, x, y, 0);
    }

    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        if (!(item instanceof ToolItem tool)) {
            return false;
        }

        if (tool.type != ToolType.PICKAXE) {
            return false;
        }

        if (!player.payStamina(6 - tool.level)) {
            return false;
        }

        hurt(level, xt, yt, 1);

        return true;
    }

    public void hurt(Level level, int x, int y, int dmg) {
        var damage = level.getData(x, y) + 1;

        level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
        level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));

        if (dmg > 0) {
            if (damage >= 10) {
                level.setTile(x, y, Tile.cloud, 0);
            }
            else {
                level.setData(x, y, damage);
            }
        }
    }

    public void bumpedInto(Level level, int x, int y, Entity entity) {
        if (entity instanceof AirWizard) return;

        entity.hurt(this, x, y, 3);
    }
}
