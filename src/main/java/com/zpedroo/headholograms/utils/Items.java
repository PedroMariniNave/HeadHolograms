package com.zpedroo.headholograms.utils;

import com.zpedroo.headholograms.Main;
import com.zpedroo.headholograms.managers.FileManager;
import com.zpedroo.headholograms.utils.menu.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Items {

    private FileManager file;
    private ItemStack hologram;
    private HashMap<String, ItemStack> fragments;

    public Items(FileManager file) {
        this.file = file;
        this.fragments = new HashMap<>();
    }

    public void loadItemsFromConfig() {
        this.hologram = ItemBuilder.build(file, "HeadHologram.item").build();
        for (String str : file.get().getConfigurationSection("HeadHologram.fragments").getKeys(false)) {
            fragments.put(str, ItemBuilder.build(file, "HeadHologram.fragments." + str).build());
        }
    }

    public ItemStack getHologram() {
        return hologram;
    }

    public HashMap<String, ItemStack> getFragments() {
        return fragments;
    }

    public boolean isHologramMaterial(Material material) {
        if (material == null || material.equals(Material.AIR)) return false;

        return material.toString().equals(Main.get().getFiles().get("CONFIG").get().getString("HeadHologram.item.type").replace("SKULL_ITEM", "SKULL"));
    }
}