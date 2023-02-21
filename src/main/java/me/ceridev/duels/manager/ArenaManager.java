package me.ceridev.duels.manager;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.*;
import me.ceridev.duels.instance.bridges.BridgeArena;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaManager {

    private DuelPlugin plugin;

    private List<BasicArena> basicArenas = new ArrayList<>();
    private List<BridgeArena> bridgeArenas = new ArrayList<>();
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
            int spawnOneYaw = doc.getEmbedded(Arrays.asList("spawn1", "yaw"), Integer.class);
            int spawnOnePitch = doc.getEmbedded(Arrays.asList("spawn1", "pitch"), Integer.class);

            String spawnOneWorld = doc.getEmbedded(Arrays.asList("spawn1", "world"), String.class);
            Location spawnOneLocation = new Location(Bukkit.getWorld(spawnOneWorld), spawnOneX, spawnOneY, spawnOneZ, (float) spawnOneYaw, (float) spawnOnePitch);
            // spawn2
            int spawnTwoX = doc.getEmbedded(Arrays.asList("spawn2", "x"), Integer.class);
            int spawnTwoY = doc.getEmbedded(Arrays.asList("spawn2", "y"), Integer.class);
            int spawnTwoZ = doc.getEmbedded(Arrays.asList("spawn2", "z"), Integer.class);
            int spawnTwoYaw = doc.getEmbedded(Arrays.asList("spawn1", "yaw"), Integer.class);
            int spawnTwoPitch = doc.getEmbedded(Arrays.asList("spawn1", "pitch"), Integer.class);

            String spawnTwoWorld = doc.getEmbedded(Arrays.asList("spawn2", "world"), String.class);
            Location spawnTwoLocation = new Location(Bukkit.getWorld(spawnTwoWorld), spawnTwoX, spawnTwoY, spawnTwoZ, (float) spawnTwoYaw, (float) spawnTwoPitch);
            if (kitType == KitType.CLASSIC || kitType == KitType.POTPVP) {
                basicArenas.add(new BasicArena(plugin, playerManager, arenaName, kitType, spawnOneLocation, spawnTwoLocation));
                System.out.println("Loaded basic arena... " + arenaName + " with type " + kitType);
            } else if (kitType == KitType.BRIDGES) {
                int goal1X = doc.getEmbedded(Arrays.asList("goal1", "x"), Integer.class);
                int goal1Y = doc.getEmbedded(Arrays.asList("goal1", "y"), Integer.class);
                int goal1Z = doc.getEmbedded(Arrays.asList("goal1", "z"), Integer.class);

                String goal1World = doc.getEmbedded(Arrays.asList("goal1", "world"), String.class);
                Location goal1Location = new Location(Bukkit.getWorld(goal1World), goal1X, goal1Y, goal1Z);

                int goal2X = doc.getEmbedded(Arrays.asList("goal2", "x"), Integer.class);
                int goal2Y = doc.getEmbedded(Arrays.asList("goal2", "y"), Integer.class);
                int goal2Z = doc.getEmbedded(Arrays.asList("goal2", "z"), Integer.class);
                String goal2World = doc.getEmbedded(Arrays.asList("goal2", "world"), String.class);
                Location goal2Location = new Location(Bukkit.getWorld(goal2World), goal2X, goal2Y, goal2Z);
                bridgeArenas.add(new BridgeArena(plugin, playerManager, arenaName, kitType, spawnOneLocation, spawnTwoLocation, goal1Location, goal2Location));
                System.out.println("Loaded bridge arena... " + arenaName + " with type " + kitType);

            }
        }
    }

    @Getter
    public BasicArena getArena(String arenaName) {
        for (BasicArena basicArena : basicArenas) {
            if (basicArena.getArenaName().equalsIgnoreCase(arenaName)) {
                return basicArena;
            }
        }
        return null;
    }

    public BridgeArena getBridgeArena(String arenaName) {
        for (BridgeArena bridgeArena : bridgeArenas) {
            if (bridgeArena.getArenaName().equalsIgnoreCase(arenaName)) {
                return bridgeArena;
            }
        }
        return null;
    }

    public BridgeArena getBridgeArena(Player player) {
        for (BridgeArena bridgeArena : bridgeArenas) {
            if (bridgeArena.getPlayers().contains(playerManager.getDuelPlayer(player))) {
                return bridgeArena;
            }
        }
        return null;
    }

    @Getter
    public boolean isPlayerInBridgeArena(Player player) {
        for (BridgeArena bridgeArena : bridgeArenas) {
            if (bridgeArena.getPlayers().contains(playerManager.getDuelPlayer(player))) {
                return true;
            }
        }
        return false;
    }

    @Getter
    public BasicArena getArena(Player player) {
        DuelsPlayer duelsPlayer = playerManager.getDuelPlayer(player);
        for (BasicArena basicArena : basicArenas) {
            if (basicArena.getPlayers().contains(duelsPlayer)) {
                return basicArena;
            }
        }
        return null;
    }

    @Getter
    public List<BasicArena> getArenas() { return basicArenas; }
    @Getter
    public List<BridgeArena> getBridgeArenas() { return bridgeArenas; }

}
