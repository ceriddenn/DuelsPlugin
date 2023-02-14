package me.ceridev.duels.manager.queue;

import me.ceridev.duels.instance.Arena;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.manager.ArenaManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class QueueTask extends BukkitRunnable {
    private final QueueManager queueManager;
    private final ArenaManager arenaManager;
    public QueueTask(DuelPlugin plugin, QueueManager queueManager, ArenaManager arenaManager) {
        this.queueManager = queueManager;
        this.arenaManager = arenaManager;
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
            if (queueManager.getClassicQueue().size() == 1) {
                DuelsPlayer matchedPlayerOne = queueManager.getClassicQueue().get(0);
                for (Arena arena : arenaManager.getArenas()) {
                    if (arena.getPlayers().size() == 1) {
                        // add preventative measure to ensure a player already in an arena cannot join queue.
                        if (matchedPlayerOne.isInMatch()) return;
                        queueManager.execute(matchedPlayerOne);
                    }
                }
            } else if (queueManager.getClassicQueue().size() > 1){
                DuelsPlayer matchedPlayerOne = queueManager.getClassicQueue().get(new Random().nextInt(queueManager.getClassicQueue().size()));
                DuelsPlayer matchedPlayerTwo = queueManager.getClassicQueue().get(new Random().nextInt(queueManager.getClassicQueue().size()));
                if (matchedPlayerOne.getPlayer().getDisplayName().equalsIgnoreCase(matchedPlayerTwo.getPlayer().getDisplayName())) return;
                for (Arena arena : arenaManager.getArenas()) {
                    if (arena.getPlayers().isEmpty()){
                        if (matchedPlayerOne.isInMatch()) return;
                        if (matchedPlayerTwo.isInMatch()) return;
                        queueManager.execute(matchedPlayerOne, matchedPlayerTwo);
                    }
                    if (arena.getPlayers().size() == 1) {
                        if (arena.getPlayers().contains(matchedPlayerOne)) {
                            if (matchedPlayerTwo.isInMatch()) return;
                            queueManager.execute(matchedPlayerTwo);
                        } else if (arena.getPlayers().contains(matchedPlayerTwo)) {
                            if (matchedPlayerOne.isInMatch()) return;
                            queueManager.execute(matchedPlayerOne);
                        }
                    }
                }
            }
    }
}
