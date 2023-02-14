package me.ceridev.duels.manager;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.instance.DuelsPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private final MongoManager mongoManager;
    private DuelPlugin plugin;
    private final ArrayList<DuelsPlayer> players = new ArrayList<>();

    public PlayerManager(DuelPlugin plugin, MongoManager mongoManager) {
        this.mongoManager = mongoManager;
        this.plugin = plugin;
    }

    public void handlePlayerJoin(Player player) {
        boolean shouldLoadDefaultValues = mongoManager.createPlayerIfPossible(player);
        if (shouldLoadDefaultValues) {
            players.add(new DuelsPlayer(plugin, player, 0, 0, 0, 0, false, KitType.CLASSIC));
        } else {
            Document userDetails = mongoManager.getDuelUsersDetails(player);
            players.add(new DuelsPlayer(
                    plugin,
                    Bukkit.getPlayer(UUID.fromString(userDetails.getString("uuid"))),
                    userDetails.getEmbedded(Arrays.asList("stats", "totalWins"), Integer.class),
                    userDetails.getEmbedded(Arrays.asList("stats", "totalLosses"), Integer.class),
                    userDetails.getEmbedded(Arrays.asList("stats", "totalKills"), Integer.class),
                    userDetails.getEmbedded(Arrays.asList("stats", "totalDeaths"), Integer.class),
                    userDetails.getBoolean("duelRequestsOpen"),
                    KitType.valueOf(userDetails.getString("preferredArenaType").toUpperCase())
                    ));
        }
    }

    public void savePlayerData(Player player) {
        DuelsPlayer duelsPlayer = getDuelPlayer(player);
        int totalWins = duelsPlayer.getTotalWins();
        int totalKills = duelsPlayer.getTotalKills();
        int totalLosses = duelsPlayer.getTotalLosses();
        int totalDeaths = duelsPlayer.getTotalDeaths();
        boolean duelRequestsOpen = duelsPlayer.getDuelRequestsOpen();
        mongoManager.saveDuelUsersDetails(duelsPlayer, totalWins, totalLosses, totalKills, totalDeaths, duelRequestsOpen);
    }

    public void handlePlayerLeave(Player player) {
        savePlayerData(player);
        players.removeIf(duelsPlayer -> duelsPlayer.getPlayer().getUniqueId().equals(player.getUniqueId()));
    }
    public DuelsPlayer getDuelPlayer(Player player) {
        for (DuelsPlayer duelsPlayer : players) {
            if (duelsPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())){
                return duelsPlayer;
            }
        }
        return null;
    }

    public List<DuelsPlayer> getAllPlayers() { return this.players; }

}
