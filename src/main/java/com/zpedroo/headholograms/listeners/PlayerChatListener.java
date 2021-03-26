package com.zpedroo.headholograms.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.zpedroo.headholograms.Main;
import com.zpedroo.headholograms.utils.PlayerChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class PlayerChatListener implements Listener {

    private static HashMap<Player, PlayerChat> playerChat = new HashMap<>(4);

    @EventHandler
    public void onChat(ChatMessageEvent event) {
        if (!playerChat.containsKey(event.getSender())) return;

        event.setCancelled(true);
        PlayerChat playerChat = getPlayerChat().remove(event.getSender());
        if (event.getMessage().toUpperCase().equals("CANCELAR")) {
            event.getSender().sendMessage("§6§lVoltz§f§lMC §8§l➜ §cAção cancelada!");
            return;
        }

        Main.get().getServer().getScheduler().runTaskLater(Main.get(), () -> playerChat.getHologramData().setLine(playerChat.getLine(), "&f" + event.getMessage()), 1L);
    }

    public static HashMap<Player, PlayerChat> getPlayerChat() {
        return playerChat;
    }
}
