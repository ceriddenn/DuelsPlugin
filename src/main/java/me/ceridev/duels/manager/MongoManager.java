package me.ceridev.duels.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ceridev.duels.adapter.MongoAdapter;
import me.ceridev.duels.instance.DuelsPlayer;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.*;

public class MongoManager {

    private final MongoClient client;
    private final MongoDatabase database;
    private MongoCollection<Document> playersCollection;

    public MongoManager(MongoAdapter mongoAdapter) {
        this.client = mongoAdapter.getClient();
        this.database = mongoAdapter.getClient().getDatabase("cluster0");
        this.playersCollection = database.getCollection("duels_players");
    }

    @Setter
    // creates a player if no player with the uuid provided is found.
    public boolean createPlayerIfPossible(Player player) {
            try (MongoCursor<Document> cursor = playersCollection.find(Filters.eq("uuid", player.getUniqueId().toString())).cursor()) {
                while (cursor.hasNext()) {
                    Document document = (Document) cursor.next();
                    String documentPlayerUUID = document.getString("uuid");
                    if (UUID.fromString(documentPlayerUUID).equals(player.getUniqueId())) {
                        return false;
                    }
                }
            }
            HashMap<String, Integer> stats = new HashMap<>();
            stats.put("totalWins", 0);
            stats.put("totalLosses", 0);
            stats.put("totalKills", 0);
            stats.put("totalDeaths", 0);
            // add another map for settings in the future.
            playersCollection.insertOne(new Document()
                    .append("uuid", player.getUniqueId().toString())
                    .append("name", player.getDisplayName())
                    .append("duelRequestsOpen", false)
                    .append("preferredArenaType", "classic")
                    .append("stats", stats)
            );
            return true;
    }

    @Getter
    public Document getDuelUsersDetails(Player player) {
        try (MongoCursor<Document> cursor = playersCollection.find(Filters.eq("uuid", player.getUniqueId().toString())).cursor()) {
            while (cursor.hasNext()) {
                Document document = (Document) cursor.next();
                String documentPlayerUUID = document.getString("uuid");
                if (UUID.fromString(documentPlayerUUID).equals(player.getUniqueId())) {
                    return document;
                }
            }
        }
        return null;
    }
    @Setter
    public void saveDuelUsersDetails(DuelsPlayer player, int totalWins, int totalLosses, int totalKills, int totalDeaths, boolean duelRequestsOpen) {
        playersCollection.updateOne(Filters.eq("uuid", player.getPlayer().getUniqueId().toString()), Updates.combine(
                Updates.set("stats.totalWins", totalWins),
                Updates.set("stats.totalLosses", totalLosses),
                Updates.set("stats.totalKills", totalKills),
                Updates.set("stats.totalDeaths", totalDeaths),
                Updates.set("duelRequestsOpen", duelRequestsOpen)
        ));
    }

    @Getter
    public List<Document> getAllArenas() {
        List<Document> allArenas = new ArrayList<>();
        MongoCollection<Document> arenasCollection = database.getCollection("arenas");
        try (MongoCursor<Document> cursor = arenasCollection.find().cursor()) {
            while (cursor.hasNext()) {
                Document document = (Document) cursor.next();
                allArenas.add(document);
            }
        }
        return allArenas;
    }

    @Getter
    public List<Document> getAllKits() {
        List<Document> allKits = new ArrayList<>();
        MongoCollection<Document> kitsCollection = database.getCollection("duels_kits");
        try (MongoCursor<Document> cursor = kitsCollection.find().cursor()) {
            while(cursor.hasNext()) {
                Document document = (Document) cursor.next();
                allKits.add(document);
            }
        }
        return allKits;
    }
}
