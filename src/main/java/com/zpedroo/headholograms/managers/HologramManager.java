package com.zpedroo.headholograms.managers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.zpedroo.headholograms.Main;
import com.zpedroo.headholograms.data.HologramData;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.UUID;

public class HologramManager {

    private Main main;

    public HologramManager(Main main) {
        this.main = main;
    }

    public Hologram createHologram(Location location) {
        return HologramsAPI.createHologram(main, location);
    }

    public void addLine(Hologram hologram, String text) {
        hologram.appendTextLine(text);
    }

    public void removeHologram(Hologram hologram) {
        hologram.delete();
    }

    public void setLine(Hologram hologram, int line, String str) {
        hologram.insertTextLine(line, str);
    }

    public Hologram addHologram(UUID uuid, Location location, String lines) {
        Location newLocation = location.clone().add(0.5, 1.25, 0.5);
        Hologram hologram = createHologram(newLocation);
        String[] split = lines.split("#");
        for (String str : split) {
            addLine(hologram, ChatColor.translateAlternateColorCodes('&', str));
            hologram.teleport(newLocation.clone().add(0, 0.2, 0));
        }
        HologramData hologramData = new HologramData(location, uuid, lines, hologram);
        Main.get().getDataManager().getCache().add(hologramData);
        return hologram;
    }

    public Hologram newHologram(UUID uuid, Location location) {
        Location newLocation = location.clone().add(0.5, 1.25, 0.5);
        Hologram hologram = createHologram(newLocation);
        String lines = "&f(Clique para editar)";
        addLine(hologram, ChatColor.translateAlternateColorCodes('&', lines));
        HologramData hologramData = new HologramData(location, uuid, lines, hologram);
        Main.get().getDataManager().getCache().add(hologramData);
        return hologram;
    }
}