package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

import java.util.List;

public class Spark extends Entity {
    private final int lifeTime;
    public double xa, ya;
    public double xx, yy;
    private int time;
    private final AirWizard owner;

    public Spark(AirWizard owner, double xa, double ya) {
        this.owner = owner;

        xx = this.x = owner.x;
        yy = this.y = owner.y;

        xr = 0;
        yr = 0;

        this.xa = xa;
        this.ya = ya;

        lifeTime = 60 * 10 + random.nextInt(30);
    }

    public void tick() {
        time++;

        if (time >= lifeTime) {
            remove();

            return;
        }

        xx += xa;
        yy += ya;

        x = (int) xx;
        y = (int) yy;

        var toHit = level.getEntities(x, y, x, y);

        for (var e : toHit) {
            if (e instanceof Mob && !(e instanceof AirWizard)) {
                e.hurt(owner, 1, ((Mob) e).dir ^ 1);
            }
        }
    }

    @Override
    public boolean isBlockableBy(Mob mob) {
        return false;
    }

    @Override
    public void render(Screen screen) {
        if (time >= lifeTime - 6 * 20) {
            if (time / 6 % 2 == 0) return;
        }

        var xt = 8;
        var yt = 13;

        screen.render(x - 4, y - 4 - 2, xt + yt * 32, Color.get(-1, 555, 555, 555), random.nextInt(4));
        screen.render(x - 4, y - 4 + 2, xt + yt * 32, Color.get(-1, 000, 000, 000), random.nextInt(4));
    }
}
