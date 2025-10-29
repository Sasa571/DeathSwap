package me.shaasha.deathswap;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerSwitcher extends BukkitRunnable implements Listener {

    private long intervalSeconds = 300;
    private long secondsLeft = intervalSeconds;
    static List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
    static List<Location> respawnLocations = new ArrayList<>();

    public static void spreadPlayers() {

        if (players.size() < 2) {
            return; // Need at least 2 players to switch
        }
        World world = Bukkit.getWorld("world");
        if (world == null) {
            for (Player p : players)
                p.sendMessage(ChatColor.RED + "World not found");
            return;
        }

        int angleStep = 360 / players.size();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            double angle = i * angleStep;
            int x = (int) (Math.cos(angle) * 10000);
            int z = (int) (Math.sin(angle) * 10000);
            Location location = findSafeLocation(world, x, z);
            player.teleport(location);
            respawnLocations.add(location);
            player.setBedSpawnLocation(location, true);
            player.sendActionBar(ChatColor.GREEN + "You have been teleported to a random location!");
        }
    }


    private void countdown() {
        for (Player player : players) {
            FreezeManager.freezePlayer(player, 10);
        }

        new BukkitRunnable() {
            int timeLeft = 10;


            @Override
            public void run() {
                if (timeLeft > 0) {
                    for (Player p : players) {
                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                        p.sendActionBar(ChatColor.GREEN + "Deathswap starts in " + timeLeft + " seconds");
                    }
                    timeLeft--;
                } else {
                    for (Player player : players) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.1f, 1.0f);
                        player.sendActionBar(ChatColor.GREEN + "START");
                    }
                    cancel(); // stop the countdown
                }
            }
        }.runTaskTimer(Deathswap.getInstance(), 0L, 20L); // 20 ticks = 1 second

    }

    @Override
    public void run() {
        if (secondsLeft == 0) {
            // Save the original locations
            List<Location> locations = new ArrayList<>();
            for (Player p : players)
                locations.add(p.getLocation());

            List<Location> shuffledLocations = new ArrayList<>(locations);
            //Shuffles player locations
            do {
                Collections.shuffle(shuffledLocations);
            } while (!isValidShuffle(locations, shuffledLocations));

            // Rotate locations
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                Location targetLocation = shuffledLocations.get((i) % players.size()); // next player's location
                player.teleport(targetLocation);
                player.setBedSpawnLocation(getClosestRespawn(targetLocation), true);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 1.0f);
                player.sendActionBar(ChatColor.RED + "Teleporting now!");
            }
            secondsLeft = intervalSeconds;

        }
        //Countdown
        else if (secondsLeft == 30 || (secondsLeft >= 1 && secondsLeft <= 10)) {
            for (Player player : players) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                player.sendActionBar(ChatColor.YELLOW + "Teleporting players in " + secondsLeft + " seconds!");
            }
        }
        secondsLeft--;
    }

    public void start(long length) {
        intervalSeconds = length;
        secondsLeft = length + 10;

        //Spread players around the world
        spreadPlayers();
        //Wait before starting the game
        countdown();
        //Sets up the death counter
        DeathCounter.deathCounter();
        //Schedule every 1 second
        runTaskTimer(Deathswap.getInstance(), 0L, 20L);
    }

    private static boolean isValidShuffle(List<Location> original, List<Location> shuffled) {
        for (int i = 0; i < original.size(); i++) {
            if (original.get(i) == shuffled.get(i)) {
                return false; // player would teleport to themselves
            }
        }
        return true;
    }

    public static Location findSafeLocation(World world, int x, int z) {
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x + 0.5, y + 1, z + 0.5);

    }

    public static Location getClosestRespawn(Location playerLocation) {
        Location closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Location loc : respawnLocations) {
            double distance = playerLocation.distance(loc);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = loc;
            }
        }
        return closest;
    }
}
