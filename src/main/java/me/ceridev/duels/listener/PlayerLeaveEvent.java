package me.ceridev.duels.listener;

import me.ceridev.duels.instance.BasicArena;
import me.ceridev.duels.instance.bridges.BridgeArena;
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
        if (arenaManager.isPlayerInBridgeArena(event.getPlayer())) {
            for (BridgeArena bridgeArena : arenaManager.getBridgeArenas()) {
                if (bridgeArena.getPlayers().contains(playerManager.getDuelPlayer(event.getPlayer()))) {
                    bridgeArena.removePlayer(event.getPlayer());
                }
            }
        } else {
            for (BasicArena basicArena : arenaManager.getArenas()) {
                if (basicArena.getPlayers().contains(playerManager.getDuelPlayer(event.getPlayer()))) {
                    basicArena.removePlayer(event.getPlayer());
                }
            }
        }
        if (playerManager.getDuelPlayer(event.getPlayer()).isInMatch()) {
            playerManager.getDuelPlayer(event.getPlayer()).setIsInMatch(false);
        }
        event.getPlayer().getActivePotionEffects().forEach(effect -> {
            event.getPlayer().removePotionEffect(effect.getType());
        });
        playerManager.handlePlayerLeave(event.getPlayer());
        queueManager.removePlayerFromQueue(event.getPlayer());
    }

}
