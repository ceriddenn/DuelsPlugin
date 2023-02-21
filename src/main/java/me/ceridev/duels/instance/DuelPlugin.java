package me.ceridev.duels.instance;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.adapter.MongoAdapter;
import me.ceridev.duels.commands.JoinCommand;
import me.ceridev.duels.commands.QueueCommand;
import me.ceridev.duels.commands.SettingsCommand;
import me.ceridev.duels.config.Database;
import me.ceridev.duels.inventory.lobby.InventoryItemManager;
import me.ceridev.duels.listener.PlayerJoinListener;
import me.ceridev.duels.listener.PlayerLeaveEvent;
import me.ceridev.duels.manager.ArenaManager;
import me.ceridev.duels.manager.MongoManager;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.kits.KitManager;
import me.ceridev.duels.manager.queue.QueueManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DuelPlugin extends JavaPlugin {

    private Database databaseFile;
    private MongoAdapter mongoAdapter;
    private MongoManager mongoManager;
    private ArenaManager arenaManager;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    private KitManager kitManager;
    private InventoryItemManager inventoryItemManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        databaseFile = new Database(this);
        databaseFile.setup();
        // register commands and events
        //---------------
        this.mongoAdapter = new MongoAdapter(this, databaseFile);
        mongoAdapter.startConnectionHandler();
        this.mongoManager = new MongoManager(mongoAdapter);
        this.playerManager = new PlayerManager(this, mongoManager);
        this.kitManager = new KitManager(mongoManager);
        kitManager.populateKits();
        this.arenaManager = new ArenaManager(this, mongoManager, playerManager);
        this.queueManager = new QueueManager(this, playerManager, arenaManager);
        this.inventoryItemManager = new InventoryItemManager(this, playerManager, queueManager);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerManager, inventoryItemManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(playerManager, arenaManager, queueManager), this);

        getCommand("join").setExecutor(new JoinCommand(arenaManager));
        getCommand("jq").setExecutor(new QueueCommand(this, queueManager));
        getCommand("settings").setExecutor(new SettingsCommand(this, playerManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        inventoryItemManager.unregisterItemEvents();
        // save mongodb duelplugin instances
        mongoAdapter.closeAdapter();
    }
    @Getter
    public MongoAdapter getMongoAdapter() { return this.mongoAdapter; }


    @Getter
    public KitManager getKitManager() { return this.kitManager; }

    @Getter
    public ArenaManager getArenaManager() { return this.arenaManager; }

    @Getter
    public InventoryItemManager getInventoryItemManager() { return this.inventoryItemManager; }

    @Getter
    public Database getDatabaseFile() { return databaseFile; }

    @Getter
    public MongoManager getMongoManager() { return this.mongoManager; }

    @Getter
    public QueueManager getQueueManager() { return this.queueManager; }

    @Getter
    public PlayerManager getPlayerManager() { return this.playerManager; }
}
