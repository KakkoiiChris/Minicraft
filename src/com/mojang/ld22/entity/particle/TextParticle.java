package com.mojang.ld22.entity.particle;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class TextParticle extends Particle {
    private final String msg;
    private final int col;
    public double xa, ya, za;
    public double xx, yy, zz;

    public TextParticle(String msg, int x, int y, int col) {
        super(x, y, 60);

        this.msg = msg;
        this.col = col;

        xx = x;
        yy = y;
        zz = 2;

        xa = random.nextGaussian() * 0.3;
        ya = random.nextGaussian() * 0.2;
        za = random.nextFloat() * 0.7 + 2;
    }

    @Override
    public void tick() {
        super.tick();

        xx += xa;
        yy += ya;
        zz += za;

        if (zz < 0) {
            zz = 0;

            za *= -0.5;
            xa *= 0.6;
            ya *= 0.6;
        }

        za -= 0.15;

        x = (int) xx;
        y = (int) yy;
    }

    @Override
    public void render(Screen screen) {
        Font.draw(msg, screen, x - msg.length() * 4 + 1, y - (int) (zz) + 1, Color.get(-1, 0, 0, 0));
        Font.draw(msg, screen, x - msg.length() * 4, y - (int) (zz), col);
    }
}
