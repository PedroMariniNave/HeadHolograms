package com.zpedroo.headholograms.listeners;

import com.zpedroo.headholograms.Main;
import com.zpedroo.headholograms.data.HologramData;
import com.zpedroo.headholograms.managers.FileManager;
import com.zpedroo.headholograms.utils.PlayerChat;
import com.zpedroo.headholograms.utils.menu.builder.ItemBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PlayerInteractListener implements Listener {

    private FileManager file;
    private int ROWS;
    private String TITLE;

    public PlayerInteractListener(FileManager file) {
        this.file = file;
        this.ROWS = file.get().getInt("Inventory.rows");
        this.TITLE = file.get().getString("Inventory.title");
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        HologramData hologramData = HologramData.getDataByLocation(event.getClickedBlock().getLocation());
        if (hologramData == null) return;

        event.setCancelled(true);
        if (!hologramData.getOwner().equals(event.getPlayer())) {
            event.getPlayer().sendMessage("§6§lVoltz§f§lMC §8§l➜ §cVocê não é o dono desse holograma!");
            return;
        }

        openHologramInventory(event.getPlayer(), hologramData);
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) return;

        HashMap<String, ItemStack> fragments = Main.get().getItems().getFragments();
        fragments.forEach((str, item) -> {
            if (event.getItem().isSimilar(item)) {
                event.setCancelled(true);
                openCraftInventory(event.getPlayer());
            }
        });
    }

    private void openHologramInventory(Player player, HologramData hologramData) {
        if (ROWS == 0 || TITLE == null) return;

        Inventory inventory = Bukkit.createInventory(null, ROWS*9, TITLE);
        for (String str : file.get().getConfigurationSection("Inventory.items").getKeys(false)) {
            int slot = file.get().getInt("Inventory.items." + str + ".slot");
            String action = file.get().getString("Inventory.items." + str + ".action", "NULL");
            ItemStack item = ItemBuilder.build(file, "Inventory.items." + str).build();
            if (item == null) continue;

            ItemMeta itemm = item.getItemMeta();
            if (itemm.hasLore()) {
                ArrayList<String> lore = (ArrayList<String>) itemm.getLore();
                int i = -1;
                for (String line : lore) {
                    lore.set(++i, StringUtils.replaceEach(line, new String[]{
                            "%owner%",
                            "%first_hologram%",
                            "%second_hologram%",
                            "%third_hologram%"
                    }, new String[]{
                            hologramData.getOwner().getName(),
                            hologramData.getLine(1) != null ? ChatColor.translateAlternateColorCodes('&', hologramData.getLine(1)) : "§cNenhum",
                            hologramData.getLine(2) != null ? ChatColor.translateAlternateColorCodes('&', hologramData.getLine(2)) : "§cNenhum",
                            hologramData.getLine(3) != null ? ChatColor.translateAlternateColorCodes('&', hologramData.getLine(3)) : "§cNenhum"
                    }));
                }
                itemm.setLore(lore);
                item.setItemMeta(itemm);
            }

            // Then set the item action, if it has one.
            if (!StringUtils.equalsIgnoreCase(action, "NULL")) {
                Main.get().getItemUtils().setItemAction(slot, () -> {
                    if (player == null || !player.isOnline()) return;
                    if (inventory == null) return;

                    switch (action.toUpperCase()) {
                        case "FIRST_LINE":
                            if (!hologramData.getOwner().equals(player)) {
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cVocê não é o dono desse holograma!");
                            } else {
                                for (int i = 0; i < 25; ++i) {
                                    player.sendMessage("");
                                }
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §aDigite no chat o holograma que você quer! Para cancelar, digite 'cancelar'");
                                PlayerChatListener.getPlayerChat().put(player, new PlayerChat(hologramData, 1));
                                player.closeInventory();
                            }
                            break;
                        case "SECOND_LINE":
                            if (hologramData.getLine(1) == null) {
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cVocê precisa adicionar a primeira linha antes!");
                                return;
                            }
                            if (!hologramData.getOwner().equals(player)) {
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cVocê não é o dono desse holograma!");
                            } else {
                                for (int i = 0; i < 25; ++i) {
                                    player.sendMessage("");
                                }
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §aDigite no chat o holograma que você quer! Para cancelar, digite 'cancelar'");
                                PlayerChatListener.getPlayerChat().put(player, new PlayerChat(hologramData, 2));
                                player.closeInventory();
                            }
                            break;
                        case "THIRD_LINE":
                            if (hologramData.getLine(2) == null) {
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cVocê precisa adicionar a segunda linha antes!");
                                return;
                            }
                            if (!hologramData.getOwner().equals(player)) {
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cVocê não é o dono desse holograma!");
                            } else {
                                for (int i = 0; i < 25; ++i) {
                                    player.sendMessage("");
                                }
                                player.sendMessage("§6§lVoltz§f§lMC §8§l➜ §aDigite no chat o holograma que você quer! Para cancelar, digite 'cancelar'");
                                PlayerChatListener.getPlayerChat().put(player, new PlayerChat(hologramData, 3));
                                player.closeInventory();
                            }
                            break;
                    }
                }, false);
            }

            inventory.setItem(slot, item);
        }

        player.openInventory(inventory);
    }

    public void openCraftInventory(Player player){
        Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH);
        String str = file.get().getString("ItemCraft.1") + file.get().getString("ItemCraft.2") + file.get().getString("ItemCraft.3");

        inventory.setItem(0, Main.get().getItems().getHologram());

        for (int i = 1; i <= 9; ++i) {
            inventory.setItem(i, Main.get().getItems().getFragments().get(Character.toString(str.charAt(i - 1))));
        }

        player.openInventory(inventory);

        player.updateInventory();
    }
}
