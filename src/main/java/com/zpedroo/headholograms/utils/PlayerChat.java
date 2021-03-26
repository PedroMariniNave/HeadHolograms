package com.zpedroo.headholograms.utils;

import com.zpedroo.headholograms.data.HologramData;

public class PlayerChat {

    private HologramData hologramData;
    private int line;

    public PlayerChat(HologramData hologramData, int line) {
        this.hologramData = hologramData;
        this.line = line;
    }

    public HologramData getHologramData() {
        return hologramData;
    }

    public int getLine() {
        return line;
    }
}
