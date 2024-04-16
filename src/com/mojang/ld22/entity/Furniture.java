package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.item.PowerGloveItem;

public class Furniture extends Entity {
    private int pushTime = 0;
    private int pushDir = -1;
    public int col, sprite;
    public String name;
    private Player shouldTake;

    public Furniture(String name) {
        this.name = name;

        xr = 3;
        yr = 3;
    }

    @Override
    public void tick() {
        if (shouldTake != null) {
            if (shouldTake.activeItem instanceof PowerGloveItem) {
                remove();

                shouldTake.inventory.add(0, shouldTake.activeItem);
                shouldTake.activeItem = new FurnitureItem(this);
            }

            shouldTake = null;
        }

        switch (pushDir) {
            case 0 -> move(0, 1);
            case 1 -> move(0, -1);
            case 2 -> move(-1, 0);
            case 3 -> move(1, 0);
        }

        pushDir = -1;

        if (pushTime > 0) pushTime--;
    }

    @Override
    public void render(Screen screen) {
        screen.render(x - 8, y - 8 - 4, sprite * 2 + 8 * 32, col, 0);
        screen.render(x, y - 8 - 4, sprite * 2 + 8 * 32 + 1, col, 0);
        screen.render(x - 8, y - 4, sprite * 2 + 8 * 32 + 32, col, 0);
        screen.render(x, y - 4, sprite * 2 + 8 * 32 + 33, col, 0);
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    @Override
    protected void touchedBy(Entity entity) {
        if (entity instanceof Player && pushTime == 0) {
            pushDir = ((Player) entity).dir;
            pushTime = 10;
        }
    }

    public void take(Player player) {
        shouldTake = player;
    }
}
