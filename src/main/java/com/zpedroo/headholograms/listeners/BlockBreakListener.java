package com.zpedroo.headholograms.listeners;

import com.zpedroo.headholograms.Main;
import com.zpedroo.headholograms.data.HologramData;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!Main.get().getItems().isHologramMaterial(event.getBlock().getType())) return;
        HologramData hologramData = HologramData.getDataByLocation(event.getBlock().getLocation());
        if (hologramData == null) return;

        event.setCancelled(true);
        if (!hologramData.getOwner().equals(event.getPlayer())) {
            event.getPlayer().sendMessage("§6§lVoltz§f§lMC §8§l➜ §cVocê não é o dono desse holograma!");
            return;
        }

        hologramData.delete();
        event.getPlayer().getInventory().addItem(Main.get().getItems().getHologram());
        event.getBlock().setType(Material.AIR);
    }
}