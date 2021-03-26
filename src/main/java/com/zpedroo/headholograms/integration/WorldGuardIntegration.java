package com.zpedroo.headholograms.integration;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardIntegration extends Integration {

    private static WorldGuardIntegration instance;
    private WorldGuardPlugin worldGuardPlugin;

    public WorldGuardIntegration() {
        super("WorldGuard");

        WorldGuardIntegration.instance = this;
    }

    @Override
    public WorldGuardPlugin getMain() {
        if (isEnabled()) return null;
        if (worldGuardPlugin == null) this.worldGuardPlugin = WorldGuardPlugin.inst();
        return worldGuardPlugin;
    }

    public Boolean canBuild(Player player, Location location) {
        if (getMain() == null) return true;
        return getMain().canBuild(player, location);
    }

    public static WorldGuardIntegration getInstance() {
        return instance;
    }
}