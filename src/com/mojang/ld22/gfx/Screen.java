package com.mojang.ld22.gfx;

import java.util.Arrays;

public class Screen {
    public int xOffset;
    public int yOffset;

    public static final int BIT_MIRROR_X = 0x01;
    public static final int BIT_MIRROR_Y = 0x02;

    public final int w, h;
    public int[] pixels;

    private final SpriteSheet sheet;

    public Screen(int w, int h, SpriteSheet sheet) {
        this.sheet = sheet;
        this.w = w;
        this.h = h;

        pixels = new int[w * h];
    }

    public void clear(int color) {
        Arrays.fill(pixels, color);
    }

    public void render(int xp, int yp, int tile, int colors, int bits) {
        xp -= xOffset;
        yp -= yOffset;

        var mirrorX = (bits & BIT_MIRROR_X) > 0;
        var mirrorY = (bits & BIT_MIRROR_Y) > 0;

        var xTile = tile % 32;
        var yTile = tile / 32;

        var toffs = xTile * 8 + yTile * 8 * sheet.width;

        for (var y = 0; y < 8; y++) {
            var ys = y;

            if (mirrorY) ys = 7 - y;

            if (y + yp < 0 || y + yp >= h) continue;

            for (var x = 0; x < 8; x++) {
                if (x + xp < 0 || x + xp >= w) continue;

                var xs = x;

                if (mirrorX) xs = 7 - x;

                var col = (colors >> (sheet.pixels[xs + ys * sheet.width + toffs] * 8)) & 255;

                if (col < 255) pixels[(x + xp) + (y + yp) * w] = col;
            }
        }
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    private final int[] dither = new int[]{0, 8, 2, 10, 12, 4, 14, 6, 3, 11, 1, 9, 15, 7, 13, 5,};

    public void overlay(Screen screen2, int xa, int ya) {
        var oPixels = screen2.pixels;

        var i = 0;

        for (var y = 0; y < h; y++) {
            for (var x = 0; x < w; x++) {
                if (oPixels[i] / 10 <= dither[((x + xa) & 3) + ((y + ya) & 3) * 4]) pixels[i] = 0;

                i++;
            }
        }
    }

    public void renderLight(int x, int y, int r) {
        x -= xOffset;
        y -= yOffset;

        var x0 = x - r;
        var x1 = x + r;
        var y0 = y - r;
        var y1 = y + r;

        if (x0 < 0) x0 = 0;
        if (y0 < 0) y0 = 0;
        if (x1 > w) x1 = w;
        if (y1 > h) y1 = h;

        for (var yy = y0; yy < y1; yy++) {
            var yd = yy - y;

            yd = yd * yd;

            for (var xx = x0; xx < x1; xx++) {
                var xd = xx - x;

                var dist = xd * xd + yd;

                if (dist <= r * r) {
                    var br = 255 - dist * 255 / (r * r);

                    if (pixels[xx + yy * w] < br) pixels[xx + yy * w] = br;
                }
            }
        }
    }
}
