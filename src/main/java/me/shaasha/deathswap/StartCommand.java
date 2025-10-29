package me.shaasha.deathswap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StartCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage("Please enter the length of the swap in minutes!");
            return true;
        }
        long duration;
        try {
            duration = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid format, please enter a number");
            return true;
        }
        new PlayerSwitcher().start(duration * 60); //Gives the duration in seconds
        sender.sendMessage("Deathswap started");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return List.of("5");
        return List.of();
    }
}
