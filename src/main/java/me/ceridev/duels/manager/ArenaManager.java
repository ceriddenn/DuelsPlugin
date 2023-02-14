package me.ceridev.duels.manager;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.Arena;
import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaManager {

    private DuelPlugin plugin;

    private List<Arena> arenas = new ArrayList<>();
    private final MongoManager mongoManager;
    private final PlayerManager playerManager;

    public ArenaManager(DuelPlugin plugin, MongoManager mongoManager, PlayerManager playerManager) {
        this.plugin = plugin;
        this.mongoManager = mongoManager;
        this.playerManager = playerManager;
        for (Document doc : mongoManager.getAllArenas()) {
            String arenaName = doc.getString("name");
            String arenaTypeAsString = doc.getString("type").toUpperCase();
            KitType kitType = KitType.valueOf(arenaTypeAsString);
            // spawn1
            int spawnOneX = doc.getEmbedded(Arrays.asList("spawn1", "x"), Integer.class);
            int spawnOneY = doc.getEmbedded(Arrays.asList("spawn1", "y"), Integer.class);
            int spawnOneZ = doc.getEmbedded(Arrays.asList("spawn1", "z"), Integer.class);
            String spawnOneWorld = doc.getEmbedded(Arrays.asList("spawn1", "world"), String.class);
            Location spawnOneLocation = new Location(Bukkit.getWorld(spawnOneWorld), spawnOneX, spawnOneY, spawnOneZ, 180, 0);
            // spawn1
            int spawnTwoX = doc.getEmbedded(Arrays.asList("spawn2", "x"), Integer.class);
            int spawnTwoY = doc.getEmbedded(Arrays.asList("spawn2", "y"), Integer.class);
            int spawnTwoZ = doc.getEmbedded(Arrays.asList("spawn2", "z"), Integer.class);
            String spawnTwoWorld = doc.getEmbedded(Arrays.asList("spawn2", "world"), String.class);
            Location spawnTwoLocation = new Location(Bukkit.getWorld(spawnTwoWorld), spawnTwoX, spawnTwoY, spawnTwoZ, 180, 0);
            if (kitType == KitType.CLASSIC) {
                arenas.add(new Arena(plugin, playerManager, arenaName, kitType, spawnOneLocation, spawnTwoLocation));
            }
            System.out.println("Loaded arena... " + arenaName + " with type " + kitType);
        }
    }

    @Getter
    public Arena getArena(String arenaName) {
        for (Arena arena : arenas) {
            if (arena.getArenaName().equalsIgnoreCase(arenaName)) {
                return arena;
            }
        }
        return null;
    }

    @Getter
    public Arena getArena(Player player) {
        DuelsPlayer duelsPlayer = playerManager.getDuelPlayer(player);
        for (Arena arena : arenas) {
            if (arena.getPlayers().contains(duelsPlayer)) {
                return arena;
            }
        }
        return null;
    }

    @Getter
    public List<Arena> getArenas() { return arenas; }

}
