package me.shaasha.deathswap;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class DeathSwap extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathCounter(), this);
        getServer().getPluginManager().registerEvents(new FreezeManager(), this);
        Objects.requireNonNull(getCommand("startdeathswap")).setExecutor(new StartCommand());
        Objects.requireNonNull(getCommand("stopdeathswap")).setExecutor(new StopCommand());
    }

    public static DeathSwap getInstance() {
        return getPlugin(DeathSwap.class);
    }
}
