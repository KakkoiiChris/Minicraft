package com.mojang.ld22.screen;

import com.mojang.ld22.crafting.Recipe;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.sound.Sound;

import java.util.ArrayList;
import java.util.List;

public class CraftingMenu extends Menu {
    private final Player player;
    private int selected = 0;

    private final List<Recipe> recipes;

    public CraftingMenu(List<Recipe> recipes, Player player) {
        this.recipes = new ArrayList<>(recipes);
        this.player = player;

        for (var i = 0; i < recipes.size(); i++) {
            this.recipes.get(i).checkCanCraft(player);
        }

        this.recipes.sort((r1, r2) -> {
            if (r1.canCraft && !r2.canCraft) return -1;
            if (!r1.canCraft && r2.canCraft) return 1;
            return 0;
        });
    }

    public void tick() {
        if (input.menu.clicked) game.setMenu(null);

        if (input.up.clicked) selected--;
        if (input.down.clicked) selected++;

        var len = recipes.size();

        if (len == 0) selected = 0;
        if (selected < 0) selected += len;
        if (selected >= len) selected -= len;

        if (input.attack.clicked && len > 0) {
            var r = recipes.get(selected);

            r.checkCanCraft(player);

            if (r.canCraft) {
                r.deductCost(player);
                r.craft(player);

                Sound.craft.play();
            }

            for (var recipe : recipes) {
                recipe.checkCanCraft(player);
            }
        }
    }

    public void render(Screen screen) {
        Font.renderFrame(screen, "Have", 12, 1, 19, 3);
        Font.renderFrame(screen, "Cost", 12, 4, 19, 11);
        Font.renderFrame(screen, "Crafting", 0, 1, 11, 11);

        renderItemList(screen, 0, 1, 11, 11, recipes, selected);

        if (!recipes.isEmpty()) {
            var recipe = recipes.get(selected);

            var hasResultItems = player.inventory.count(recipe.resultTemplate);

            var xo = 13 * 8;

            screen.render(xo, 2 * 8, recipe.resultTemplate.getSprite(), recipe.resultTemplate.getColor(), 0);

            Font.draw("" + hasResultItems, screen, xo + 8, 2 * 8, Color.get(-1, 555, 555, 555));

            var costs = recipe.costs;

            for (var i = 0; i < costs.size(); i++) {
                var item = costs.get(i);

                var yo = (5 + i) * 8;

                screen.render(xo, yo, item.getSprite(), item.getColor(), 0);

                var requiredAmt = 1;

                if (item instanceof ResourceItem ri) {
                    requiredAmt = ri.count;
                }

                var has = player.inventory.count(item);

                var color = Color.get(-1, 555, 555, 555);

                if (has < requiredAmt) {
                    color = Color.get(-1, 222, 222, 222);
                }

                if (has > 99) has = 99;

                Font.draw("%d/%d".formatted(requiredAmt, has), screen, xo + 8, yo, color);
            }
        }
    }
}
