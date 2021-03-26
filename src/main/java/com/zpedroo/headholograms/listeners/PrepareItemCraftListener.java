package com.zpedroo.headholograms.listeners;

import com.zpedroo.headholograms.Main;
import com.zpedroo.headholograms.managers.FileManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareItemCraftListener implements Listener {

    private Main main;
    private FileManager file;

    public PrepareItemCraftListener(Main main, FileManager file) {
        this.main = main;
        this.file = file;
    }

    @EventHandler
    public void onPrepareItem(PrepareItemCraftEvent event) {
        if (event.getRecipe().getResult().isSimilar(main.getItems().getHologram())) {
            int amount = 0;
            String str = file.get().getString("ItemCraft.1") + file.get().getString("ItemCraft.2") + file.get().getString("ItemCraft.3");
            for (int i = 1; i <= 9; ++i) {
                if (event.getInventory().getItem(i) == null) continue;

                if (event.getInventory().getItem(i).isSimilar(main.getItems().getFragments().get(Character.toString(str.charAt(i - 1))))) {
                    amount += 1;
                }
            }
            if (amount < 9) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }
}