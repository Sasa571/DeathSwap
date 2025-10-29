package me.shaasha.deathswap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class FreezeManager implements Listener {
    private static final Set<Player> frozenPlayers = new HashSet<>();

    public FreezeManager() {

    }

    public static void freezePlayer(Player player, int seconds) {
        frozenPlayers.add(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                frozenPlayers.remove(player);
            }
        }.runTaskLater(Deathswap.getInstance(), seconds * 20L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player)) {
            // Prevent all movement including rotation
            if (event.getFrom().getX() != event.getTo().getX() ||
                    event.getFrom().getY() != event.getTo().getY() ||
                    event.getFrom().getZ() != event.getTo().getZ()) {
                event.setTo(event.getFrom());
            }
        }
    }
}
