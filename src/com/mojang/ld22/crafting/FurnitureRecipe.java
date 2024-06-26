package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.Furniture;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.FurnitureItem;

import java.lang.reflect.InvocationTargetException;

public class FurnitureRecipe extends Recipe {
    private final Class<? extends Furniture> clazz;

    public FurnitureRecipe(Class<? extends Furniture> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super(new FurnitureItem(clazz.getConstructor().newInstance()));

        this.clazz = clazz;
    }

    public void craft(Player player) {
        try {
            player.inventory.add(0, new FurnitureItem(clazz.getConstructor().newInstance()));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
