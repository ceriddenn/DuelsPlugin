package me.ceridev.duels.instance.game;

import me.ceridev.duels.instance.*;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game implements Listener {

    protected DuelPlugin plugin;
    protected BasicArena basicArena;

    protected ArenaCleanup arenaCleanup;

    public Game(DuelPlugin plugin, BasicArena basicArena) {
        this.plugin = plugin;
        this.basicArena = basicArena;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void start() {
        basicArena.setGameState(GameState.RUNNING);
        onStart();
    }

    public void startArenaCleanup() {
        this.arenaCleanup = new ArenaCleanup(plugin, basicArena);
        arenaCleanup.start();
    }

    public void newCleanup(BasicArena basicArena) {
        arenaCleanup.cancel();
        this.arenaCleanup = new ArenaCleanup(plugin, basicArena);
        arenaCleanup.start();
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
        if (basicArena.getGameState() == GameState.CLOSING) {
            arenaCleanup.cancel();
        }
        this.arenaCleanup = new ArenaCleanup(plugin, basicArena);
    }

    public abstract void onStart();

}
