package com.zpedroo.headholograms.data;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.zpedroo.headholograms.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HologramData {

    private Location location;
    private UUID uuid;
    private String lines;
    private Hologram hologram;

    public HologramData(Location location, UUID uuid, String lines, Hologram hologram) {
        this.location = location;
        this.uuid = uuid;
        this.lines = lines;
        this.hologram = hologram;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(uuid);
    }

    public String getLines() {
        return lines;
    }

    public String getLine(int line) {
        return lines.split("#").length >= (line) ? lines.split("#")[(line - 1)] : null;
    }

    public void setLine(int line, String text) {
        if (getLine(line) != null) {
            getHologram().getLine(line - 1).removeLine();
            getHologram().insertTextLine(line - 1, ChatColor.translateAlternateColorCodes('&', text));
        } else{
            getHologram().appendTextLine(ChatColor.translateAlternateColorCodes('&', text));
            getHologram().teleport(getHologram().getLocation().clone().add(0, 0.2, 0));
        }

        String[] split = lines.split("#");
        if (split.length < line) {
            this.lines = new StringBuilder(lines).append("#").append(text).toString();
        } else {
            split[line - 1] = text;
            StringBuilder builder = new StringBuilder(split[0]);
            for (int i = 1; i < split.length; ++i) {
                builder.append("#").append(split[i]);
            }
            this.lines = builder.toString();
        }
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void load() {
        Main.get().getHologramManager().addHologram(getUUID(), getLocation(), getLines());
    }

    public void delete() {
        if (getHologram() != null) {
            getHologram().delete();
        }
        Main.get().getDataManager().getDeletedCache().add(this);
        Main.get().getDataManager().getCache().remove(this);
    }

    public static HologramData getDataByLocation(Location location) {
        for (HologramData hologramData : Main.get().getDataManager().getCache()) {
            if (hologramData == null) continue;

            if (hologramData.getLocation().equals(location)) {
                return hologramData;
            }
        }
        return null;
    }
}