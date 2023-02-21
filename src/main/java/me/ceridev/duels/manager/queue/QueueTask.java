package me.ceridev.duels.manager.queue;

import me.ceridev.duels.instance.*;
import me.ceridev.duels.instance.bridges.BridgeArena;
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
        Random generator = new Random();
        Object[] keys = queueManager.getQueue().keySet().toArray();
            if (queueManager.getQueue().size() == 1) {
                DuelsPlayer matchedPlayerOne = (DuelsPlayer) keys[generator.nextInt(keys.length)];
                if (queueManager.getQueue().get(matchedPlayerOne) == KitType.BRIDGES) {
                    for (BridgeArena bridgeArena : arenaManager.getBridgeArenas()) {
                        if (bridgeArena.getKitType() != queueManager.getQueue().get(matchedPlayerOne)) return;
                        if (bridgeArena.getPlayers().size() == 1) {
                            // add preventative measure to ensure a player already in an arena cannot join queue.
                            if (matchedPlayerOne.isInMatch()) return;
                            queueManager.executeBridge(matchedPlayerOne);
                            System.out.println("RAN1");
                        }
                    }
                } else {
                    for (BasicArena basicArena : arenaManager.getArenas()) {
                        if (basicArena.getKitType() != queueManager.getQueue().get(matchedPlayerOne)) return;
                        if (basicArena.getPlayers().size() == 1) {
                            // add preventative measure to ensure a player already in an arena cannot join queue.
                            if (matchedPlayerOne.isInMatch()) return;
                            queueManager.execute(matchedPlayerOne);
                        }
                    }
                }
            } else if (queueManager.getQueue().size() > 1){
                DuelsPlayer mPOne = null;
                DuelsPlayer mpTwo = null;
                DuelsPlayer matchedPlayerOne = (DuelsPlayer) keys[generator.nextInt(keys.length)];
                DuelsPlayer matchedPlayerTwo = (DuelsPlayer) keys[generator.nextInt(keys.length)];
                // check if both players kit queue type is the same
                if (!queueManager.getQueue().get(matchedPlayerOne).equals(queueManager.getQueue().get(matchedPlayerTwo))) {
                    return;
                }
                if (matchedPlayerOne.getPlayer().getDisplayName().equalsIgnoreCase(matchedPlayerTwo.getPlayer().getDisplayName())) return;
                if (queueManager.getQueue().get(matchedPlayerOne) == KitType.BRIDGES) {
                    for (BridgeArena bridgeArena : arenaManager.getBridgeArenas()) {
                        if (bridgeArena.getKitType() != queueManager.getQueue().get(matchedPlayerOne)) return;
                        ;
                        if (bridgeArena.getKitType() != queueManager.getQueue().get(matchedPlayerTwo)) return;
                        ;
                        if (bridgeArena.getPlayers().isEmpty()) {
                            if (matchedPlayerOne.isInMatch()) return;
                            if (matchedPlayerTwo.isInMatch()) return;
                            queueManager.executeBridge(matchedPlayerOne, matchedPlayerTwo);
                            System.out.println("RAN2");
                        }
                        if (bridgeArena.getPlayers().size() == 1) {
                            if (bridgeArena.getPlayers().contains(matchedPlayerOne)) {
                                if (matchedPlayerTwo.isInMatch()) return;
                                queueManager.executeBridge(matchedPlayerTwo);
                                System.out.println("RAN3");
                            } else if (bridgeArena.getPlayers().contains(matchedPlayerTwo)) {
                                if (matchedPlayerOne.isInMatch()) return;
                                queueManager.executeBridge(matchedPlayerOne);
                                System.out.println("RAN4");
                            }
                        }
                    }
                } else {
                    for (BasicArena basicArena : arenaManager.getArenas()) {
                        if (basicArena.getKitType() != queueManager.getQueue().get(matchedPlayerOne)) return;
                        ;
                        if (basicArena.getKitType() != queueManager.getQueue().get(matchedPlayerTwo)) return;
                        ;
                        if (basicArena.getPlayers().isEmpty()) {
                            if (matchedPlayerOne.isInMatch()) return;
                            if (matchedPlayerTwo.isInMatch()) return;
                            queueManager.execute(matchedPlayerOne, matchedPlayerTwo);
                        }
                        if (basicArena.getPlayers().size() == 1) {
                            if (basicArena.getPlayers().contains(matchedPlayerOne)) {
                                if (matchedPlayerTwo.isInMatch()) return;
                                queueManager.execute(matchedPlayerTwo);
                            } else if (basicArena.getPlayers().contains(matchedPlayerTwo)) {
                                if (matchedPlayerOne.isInMatch()) return;
                                queueManager.execute(matchedPlayerOne);
                            }
                        }
                    }
                }
            }
    }
}
