package com.mojang.ld22.entity.particle;

import com.mojang.ld22.entity.Entity;

public class Particle extends Entity {
    private final int lifeTime;

    private int time = 0;

    public Particle(int x, int y, int lifeTime) {
        this.x = x;
        this.y = y;
        this.lifeTime = lifeTime;
    }

    public void tick() {
        time++;

        if (time > lifeTime) {
            remove();
        }
    }
}
