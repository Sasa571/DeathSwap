package me.shaasha.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Bukkit.getScheduler().cancelTasks(DeathSwap.getInstance());
        sender.sendMessage("Deathswap stopped!");
        return true;
    }
}
