package com.zpedroo.headholograms.listeners;

import com.zpedroo.headholograms.Main;
import com.zpedroo.headholograms.data.HologramData;
import com.zpedroo.headholograms.integration.WorldGuardIntegration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item == null || item.getType().equals(Material.AIR)) return;
        if (!WorldGuardIntegration.getInstance().canBuild(event.getPlayer(), event.getBlockPlaced().getLocation())) return;

        if (item.isSimilar(Main.get().getItems().getHologram())) {
            Block block = event.getBlockPlaced();

            int radius = 5;
            int initialX = block.getX(), initialY = block.getY(), initialZ = block.getZ();
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (initialX == x && initialY == y && initialZ == z) continue;
                        if (!WorldGuardIntegration.getInstance().canBuild(event.getPlayer(), new Location(block.getWorld(), x, y, z))) continue;

                        Block blocks = block.getRelative(x, y, z);
                        if (blocks == null || blocks.getType().equals(Material.AIR) || !Main.get().getItems().isHologramMaterial(blocks.getType())) continue;

                        HologramData hologramData = HologramData.getDataByLocation(blocks.getLocation());
                        if (hologramData == null) continue;

                        event.setCancelled(true);
                        event.getPlayer().sendMessage("§6§lVoltz§f§lMC §8§l➜ §cHá um holograma por perto! Afaste-se e tente novamente.");
                        return;
                    }
                }
            }

            Main.get().getHologramManager().newHologram(event.getPlayer().getUniqueId(), block.getLocation());
        }
    }
}