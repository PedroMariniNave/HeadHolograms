package com.zpedroo.headholograms.integration;

import org.bukkit.Bukkit;

public abstract class Integration implements IntegrationInterface {

    private String name;
    private Boolean enabled;

    /**
     * The @integration abstract
     * which helps to implement
     * a new dependency.
     *
     * @param name soft-depend/dependency
     *             plugin name.
     */
    public Integration(final String name) {
        this.name = name;
        this.enabled = setup();
    }

    /**
     * Get the plugin name.
     *
     * @return plugin name.
     */
    public String getName() {
        return name;
    }

    /**
     * Verify if this plugin
     * is running on your server.
     *
     * @return is plugin is currently
     *          running on your server.
     */
    public Boolean isEnabled() {
        return enabled;
    }

    public void refresh() {
        this.enabled = setup();
    }

    /**
     * Setup this dependency.
     *
     * @return if plugin is currently
     *          running on your server.
     */
    private Boolean setup() {
        if (name == null || name.isEmpty()) return false;
        return Bukkit.getServer().getPluginManager().isPluginEnabled(getName());
    }
}