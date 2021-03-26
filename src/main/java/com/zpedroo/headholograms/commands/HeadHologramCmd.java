package com.zpedroo.headholograms.commands;

import com.zpedroo.headholograms.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

public class HeadHologramCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;

        if (args.length < 3) {
            sender.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cUso correto: /headhologram give/fragment <jogador> <quantia>.");
            return true;
        }

        if (args[0].toUpperCase().equals("GIVE")) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cEsse jogador está offline!");
                return true;
            }

            if (!isNumeric(args[2]) || Integer.parseInt(args[2]) < 0) {
                sender.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cQuantia inválida!");
                return true;
            }

            ItemStack item = Main.get().getItems().getHologram().clone();
            item.setAmount(Integer.parseInt(args[2]));
            player.getInventory().addItem(item);
        }

        if (args[0].toUpperCase().equals("FRAGMENT")) {

            if (args.length < 4) {
                sender.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cUso correto: /headhologram fragment <fragmento> <jogador> <quantia>.");
                return true;
            }

            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cEsse jogador está offline!");
                return true;
            }

            if (!isNumeric(args[3]) || Integer.parseInt(args[3]) < 0) {
                sender.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cQuantia inválida!");
                return true;
            }

            if (!Main.get().getItems().getFragments().containsKey(args[1])) {
                sender.sendMessage("§6§lVoltz§f§lMC §8§l➜ §cFragmento inválido! Fragmentos válidos: " + Main.get().getItems().getFragments().values() + ".");
                return true;
            }

            ItemStack item = Main.get().getItems().getFragments().get(args[1]).clone();
            item.setAmount(Integer.parseInt(args[3]));
            player.getInventory().addItem(item);
        }
        return false;
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) return true;

        return Pattern.compile("-?\\d+(\\.\\d+)?").matcher(strNum).matches();
    }
}
