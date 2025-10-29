package me.shaasha.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class DeathCounter implements Listener {
    static Map<Player, Integer> playersDeaths = new HashMap<>();

    public static void deathCounter() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playersDeaths.put(player, 0);
        }
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event) {
        playersDeaths.put(event.getPlayer(), playersDeaths.get(event.getPlayer()) + 1);
        printPlayerDeaths();

    }

    public static void printPlayerDeaths() {
        Bukkit.broadcastMessage(ChatColor.GOLD + "Player Deaths:");
        for (Map.Entry<Player, Integer> entry : playersDeaths.entrySet()) {
            Player player = entry.getKey();
            int deathCount = entry.getValue();

            Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + ": " + ChatColor.RED + deathCount);
        }
    }
}
