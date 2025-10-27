package me.shaasha.deathswap;

import org.bukkit.plugin.java.JavaPlugin;

public final class DeathSwap extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathCounter(), this);
        getServer().getPluginManager().registerEvents(new FreezeManager(), this);
        getCommand("startdeathswap").setExecutor(new StartCommand());
        getCommand("stopdeathswap").setExecutor(new StopCommand());
    }

    @Override
    public void onDisable() {

    }

    public static DeathSwap getInstance() {
        return getPlugin(DeathSwap.class);
    }
}
