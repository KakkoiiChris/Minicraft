package com.mojang.ld22.entity.particle;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.sound.Sound;

public class SmashParticle extends Particle {
	public SmashParticle(int x, int y) {
		super(x, y, 10);

		Sound.monsterHurt.play();
	}

	@Override
    public void render(Screen screen) {
		int col = Color.get(-1, 555, 555, 555);
		screen.render(x - 8, y - 8, 5 + 12 * 32, col, 2);
		screen.render(x, y - 8, 5 + 12 * 32, col, 3);
		screen.render(x - 8, y, 5 + 12 * 32, col, 0);
		screen.render(x, y, 5 + 12 * 32, col, 1);
	}
}
