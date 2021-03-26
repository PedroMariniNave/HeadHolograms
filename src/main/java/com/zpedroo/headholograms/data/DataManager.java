package com.zpedroo.headholograms.data;

import com.zpedroo.headholograms.Main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataManager{

    public List<HologramData> dataCache;
    public List<HologramData> deletedCache;

    public DataManager() {
        this.dataCache = new ArrayList<>(64);
        this.deletedCache = new ArrayList<>(64);
    }

    public void saveAll() {
        new HashSet<>(Main.get().getDataManager().getDeletedCache()).forEach(hologramData -> {
            Main.get().getSQLiteConnector().deleteHologram(hologramData);
        });

        new HashSet<>(Main.get().getDataManager().getCache()).forEach(hologramData -> {
            Main.get().getSQLiteConnector().saveHologram(hologramData);
        });
    }

    public List<HologramData> getCache() {
        return dataCache;
    }

    public List<HologramData> getDeletedCache() {
        return deletedCache;
    }
}
