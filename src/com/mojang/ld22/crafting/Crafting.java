package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.*;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public class Crafting {
    public static final List<Recipe> anvilRecipes = new ArrayList<>();
    public static final List<Recipe> ovenRecipes = new ArrayList<>();
    public static final List<Recipe> furnaceRecipes = new ArrayList<>();
    public static final List<Recipe> workbenchRecipes = new ArrayList<>();

    static {
        try {
            workbenchRecipes.add(new FurnitureRecipe(Lantern.class).addCost(Resource.wood, 5).addCost(Resource.slime, 10).addCost(Resource.glass, 4));

            workbenchRecipes.add(new FurnitureRecipe(Oven.class).addCost(Resource.stone, 15));
            workbenchRecipes.add(new FurnitureRecipe(Furnace.class).addCost(Resource.stone, 20));
            workbenchRecipes.add(new FurnitureRecipe(Workbench.class).addCost(Resource.wood, 20));
            workbenchRecipes.add(new FurnitureRecipe(Chest.class).addCost(Resource.wood, 20));
            workbenchRecipes.add(new FurnitureRecipe(Anvil.class).addCost(Resource.ironIngot, 5));

            workbenchRecipes.add(new ToolRecipe(ToolType.SWORD, 0).addCost(Resource.wood, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.AXE, 0).addCost(Resource.wood, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.HOE, 0).addCost(Resource.wood, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.PICKAXE, 0).addCost(Resource.wood, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.SHOVEL, 0).addCost(Resource.wood, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.SWORD, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.AXE, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.HOE, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.PICKAXE, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            workbenchRecipes.add(new ToolRecipe(ToolType.SHOVEL, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));

            anvilRecipes.add(new ToolRecipe(ToolType.SWORD, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.AXE, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.HOE, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.PICKAXE, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.SHOVEL, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));

            anvilRecipes.add(new ToolRecipe(ToolType.SWORD, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.AXE, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.HOE, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.PICKAXE, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            anvilRecipes.add(new ToolRecipe(ToolType.SHOVEL, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));

            anvilRecipes.add(new ToolRecipe(ToolType.SWORD, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 50));
            anvilRecipes.add(new ToolRecipe(ToolType.AXE, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 50));
            anvilRecipes.add(new ToolRecipe(ToolType.HOE, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 50));
            anvilRecipes.add(new ToolRecipe(ToolType.PICKAXE, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 50));
            anvilRecipes.add(new ToolRecipe(ToolType.SHOVEL, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 50));

            furnaceRecipes.add(new ResourceRecipe(Resource.ironIngot).addCost(Resource.ironOre, 4).addCost(Resource.coal, 1));
            furnaceRecipes.add(new ResourceRecipe(Resource.goldIngot).addCost(Resource.goldOre, 4).addCost(Resource.coal, 1));
            furnaceRecipes.add(new ResourceRecipe(Resource.glass).addCost(Resource.sand, 4).addCost(Resource.coal, 1));

            ovenRecipes.add(new ResourceRecipe(Resource.bread).addCost(Resource.wheat, 4));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
