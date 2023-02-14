package me.ceridev.duels.listener;

import me.ceridev.duels.instance.Arena;
import me.ceridev.duels.manager.ArenaManager;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.queue.QueueManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveEvent implements Listener {

    private PlayerManager playerManager;
    private ArenaManager arenaManager;
    private QueueManager queueManager;
    public PlayerLeaveEvent(PlayerManager playerManager, ArenaManager arenaManager, QueueManager queueManager) {
        this.playerManager = playerManager;
        this.arenaManager = arenaManager;
        this.queueManager = queueManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        for (Arena arena : arenaManager.getArenas()) {
            if (arena.getPlayers().contains(playerManager.getDuelPlayer(event.getPlayer()))) {
                arena.removePlayer(event.getPlayer());
            }
        }
        playerManager.handlePlayerLeave(event.getPlayer());
        queueManager.removePlayerFromQueue(event.getPlayer());
    }

}
