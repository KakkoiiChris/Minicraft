package com.mojang.ld22.item;

public enum ToolType {
    SHOVEL("Shvl"),
    HOE("Hoe"),
    SWORD("Swrd"),
    PICKAXE("Pick"),
    AXE("Axe");

    public final String name;
    public final int sprite;

    ToolType(String name) {
        this.name = name;

        sprite = ordinal();
    }
}
