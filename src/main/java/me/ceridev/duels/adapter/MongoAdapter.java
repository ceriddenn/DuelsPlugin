package me.ceridev.duels.adapter;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ceridev.duels.config.Database;
import me.ceridev.duels.instance.DuelPlugin;
import org.bson.Document;
import org.bukkit.Bukkit;

public class MongoAdapter {

    private MongoClient client;
    private final String cString;
    private final DuelPlugin plugin;

    public MongoAdapter(DuelPlugin plugin, Database databaseFile) {
        this.plugin = plugin;
        this.cString = databaseFile.getURI();
    }
    public void startConnectionHandler() {
        plugin.getLogger().info("Plugin loading mongo adapter....");
        // do mongo stuff

        client = MongoClients.create(cString);
        // assume the collection has already been created...
        // add check for new whitelisted players
        plugin.getLogger().info("Plugin loaded mongo adapter....");
    }


    @Getter
    public MongoClient getClient() { return client; }

    @Setter
    public void closeAdapter() { client.close(); }

}
