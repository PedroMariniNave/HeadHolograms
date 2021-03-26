package com.zpedroo.headholograms;

import com.zpedroo.headholograms.commands.HeadHologramCmd;
import com.zpedroo.headholograms.data.DataManager;
import com.zpedroo.headholograms.data.SQLiteConnector;
import com.zpedroo.headholograms.integration.WorldGuardIntegration;
import com.zpedroo.headholograms.listeners.*;
import com.zpedroo.headholograms.managers.FileManager;
import com.zpedroo.headholograms.managers.HologramManager;
import com.zpedroo.headholograms.utils.Items;
import com.zpedroo.headholograms.utils.menu.builder.ItemBuilder;
import com.zpedroo.headholograms.utils.menu.inv.InvUtils;
import com.zpedroo.headholograms.utils.menu.item.ItemUtils;
import com.zpedroo.headholograms.utils.menu.listeners.InvActionListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {

    private static Main main;
    public static Main get() { return main; }

    private HashMap<String, FileManager> files = new HashMap<>();

    private HologramManager hologramManager;
    private SQLiteConnector sqLiteConnector;
    private DataManager dataManager;
    private Items items;
    private InvUtils invUtils;
    private ItemUtils itemUtils;

    public void onEnable() {
        main = this;
        getFiles().put("CONFIG", new FileManager("", "config", "configuration-files/config"));
        hologramManager = new HologramManager(this);
        sqLiteConnector = new SQLiteConnector("headholograms");
        dataManager = new DataManager();
        items = new Items(getFiles().get("CONFIG"));
        invUtils = new InvUtils();
        itemUtils = new ItemUtils();
        new WorldGuardIntegration();
        getItems().loadItemsFromConfig();
        getSQLiteConnector().loadHolograms();
        registerCommands();
        registerListeners();
        getServer().getPluginManager().registerEvents(new InvActionListener(this, itemUtils, getFiles().get("CONFIG")), this);
        loadHologramCraft();
    }

    public void onDisable() {
        getDataManager().saveAll();
        try {
            getSQLiteConnector().closeConnection();
        } catch (Exception e) {}
    }

    private void registerCommands() {
        getCommand("headhologram").setExecutor(new HeadHologramCmd());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new InvActionListener(this, getItemUtils(), getFiles().get("CONFIG")), this);
        getServer().getPluginManager().registerEvents(new PrepareItemCraftListener(this, getFiles().get("CONFIG")), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(getFiles().get("CONFIG")), this);
    }

    private void loadHologramCraft(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(getItems().getHologram());
        shapedRecipe.shape(getCraftLine(1), getCraftLine(2), getCraftLine(3));
        for (String str : getFiles().get("CONFIG").get().getConfigurationSection("HeadHologram.fragments").getKeys(false)) {
            String type = getFiles().get("CONFIG").get().getString("HeadHologram.fragments." + str + ".type");
            ItemStack item = new ItemStack(Material.getMaterial(type), 1, (short) (type.equals("SKULL_ITEM") ? 3 : 0));
            shapedRecipe.setIngredient(str.charAt(0), item.getData());
        }
        Bukkit.addRecipe(shapedRecipe);
    }

    private String getCraftLine(int line) {
        return getFiles().get("CONFIG").get().getString("ItemCraft." + line);
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public SQLiteConnector getSQLiteConnector() {
        return sqLiteConnector;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public HashMap<String, FileManager> getFiles() {
        return files;
    }

    public Items getItems() {
        return items;
    }

    public InvUtils getInvUtils() {
        return invUtils;
    }

    public ItemUtils getItemUtils() {
        return itemUtils;
    }
}
