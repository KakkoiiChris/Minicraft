package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.screen.ListItem;

import java.util.ArrayList;
import java.util.List;

public abstract class Recipe implements ListItem {
    public List<Item> costs = new ArrayList<>();
    public boolean canCraft = false;
    public Item resultTemplate;

    public Recipe(Item resultTemplate) {
        this.resultTemplate = resultTemplate;
    }

    public Recipe addCost(Resource resource, int count) {
        costs.add(new ResourceItem(resource, count));

        return this;
    }

    public void checkCanCraft(Player player) {
        for (var item : costs) {
            if (item instanceof ResourceItem ri) {
                if (!player.inventory.hasResources(ri.resource, ri.count)) {
                    canCraft = false;

                    return;
                }
            }
        }

        canCraft = true;
    }

    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, resultTemplate.getSprite(), resultTemplate.getColor(), 0);

        var textColor = canCraft ? Color.get(-1, 555, 555, 555) : Color.get(-1, 222, 222, 222);

        Font.draw(resultTemplate.getName(), screen, x + 8, y, textColor);
    }

    public abstract void craft(Player player);

    public void deductCost(Player player) {
        for (var item : costs) {
            if (item instanceof ResourceItem ri) {
                player.inventory.removeResource(ri.resource, ri.count);
            }
        }
    }
}
