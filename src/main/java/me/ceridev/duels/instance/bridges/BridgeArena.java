package me.ceridev.duels.instance.bridges;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.instance.GameState;
import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.instance.bridges.game.BridgeCountdown;
import me.ceridev.duels.instance.bridges.game.CancelTpTask;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.game.BlockManager;
import me.ceridev.duels.manager.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BridgeArena {

    private final DuelPlugin plugin;
    private BridgeCountdown countdown;

    private GameState gameState;
    private String arenaName;
    private KitType kitType;
    private Kit kit;
    private SpecialGame game;
    private Location spawnLocationOne;
    private Location spawnLocationTwo;
    private Location goal1;
    private Location goal2;
    private Location globalSpawnLocation;
    private PlayerManager playerManager;
    private BlockManager blockManager = null;
    private List<DuelsPlayer> players = new ArrayList<>();
    private Map<DuelsPlayer, Integer> scores = new HashMap<>();
    private int cleanupSeconds = 5;

    public BridgeArena(DuelPlugin plugin, PlayerManager playerManager, String arenaName, KitType kitType, Location spawnLocationOne, Location spawnLocationTwo, Location goal1, Location goal2) {
        this.plugin = plugin;
        this.kitType = kitType;
        this.arenaName = arenaName;
        this.kit = plugin.getKitManager().retrieveKit(this.kitType);
        this.playerManager = playerManager;
        this.countdown = new BridgeCountdown(plugin, this);
        this.spawnLocationOne = spawnLocationOne;
        this.spawnLocationTwo = spawnLocationTwo;
        this.goal1 = goal1;
        this.goal2 = goal2;
        this.globalSpawnLocation = plugin.getDatabaseFile().getGlobalSpawnLocation();
        this.game = new BridgeGame(plugin, this, kit, spawnLocationOne, spawnLocationTwo);
        setGameState(GameState.OPEN);
    }

    public void start() { game.start(); }

    public void sendMessage(String message) {
        for (DuelsPlayer duelsPlayer : players) {
            duelsPlayer.getPlayer().sendMessage(message);
        }
    }

    public void setWinner(DuelsPlayer winner, DuelsPlayer loser) {
        gameState = GameState.CLOSING;
        sendTitle(ChatColor.YELLOW + "VICTORY", ChatColor.GREEN + winner.getPlayer().getDisplayName() + "won the match!");
        loser.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1));
        loser.getPlayer().getInventory().clear();
        loser.addDeath(1);
        loser.addLoss(1);
        plugin.getInventoryItemManager().addArenaCleanupItemsToPlayer(loser.getPlayer());
        winner.getPlayer().setGameMode(GameMode.ADVENTURE);
        winner.getPlayer().getInventory().clear();
        winner.addWin(1);
        winner.addKill(1);
        plugin.getInventoryItemManager().addArenaCleanupItemsToPlayer(winner.getPlayer());
        game.startArenaCleanup();
    }

    public void addScore(DuelsPlayer duelsPlayer) {
        int currentScore = scores.get(duelsPlayer);
        int aboveZ = 0;
        scores.put(duelsPlayer, currentScore+1);
        // add check to see if score is == to 3... if so then that player is now winner
        for (DuelsPlayer dp : scores.keySet()) {
            if (scores.get(dp) == 3) {
                aboveZ++;
                if (players.indexOf(duelsPlayer) == 1) {
                    setWinner(duelsPlayer, players.get(0));
                } else {
                    setWinner(duelsPlayer, players.get(1));
                }
            }
        }
        sendMessage(ChatColor.GREEN + "" + duelsPlayer.getPlayer().getDisplayName() + ChatColor.GREEN + " scored!");
        players.get(0).getPlayer().teleport(spawnLocationOne);
        players.get(0).getPlayer().teleport(spawnLocationTwo);
        if (aboveZ > 0) return;
        cleanupRound();
    }

    public void cleanupRound() {
        BukkitRunnable tpTask = new CancelTpTask(plugin,this);
        new BukkitRunnable() {
            @Override
            public void run() {
                tpTask.run();
                if (cleanupSeconds == 0) {
                    tpTask.cancel();
                    cancel();
                } else {
                    sendMessage("get ready in " + cleanupSeconds);
                }
                cleanupSeconds--;
            }
        }.runTaskTimer(plugin, 0, 20);
        cleanupSeconds = 5;
    }

    public void removePlayer(Player player) {
        if (gameState == GameState.CLOSING) {
            players.remove(playerManager.getDuelPlayer(player));
            scores.remove(playerManager.getDuelPlayer(player));
            sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + player.getDisplayName() + " rage quit lol.");
            return;
        }
        if (gameState == GameState.RUNNING) {
            DuelsPlayer loser = playerManager.getDuelPlayer(player);
            players.remove(playerManager.getDuelPlayer(player));
            scores.remove(playerManager.getDuelPlayer(player));

            sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + player.getDisplayName() + " left during the match.");
            setWinner(players.get(0), loser);
        } else if (gameState == GameState.COUNTDOWN) {
            players.remove(playerManager.getDuelPlayer(player));
            scores.remove(playerManager.getDuelPlayer(player));
            countdown.cancel();
            this.countdown = new BridgeCountdown(plugin, this);
            sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC + "Countdown stopped! A player left before the match started.");
            setGameState(GameState.OPEN);
        } else if (gameState == GameState.OPEN) {
            playerManager.getDuelPlayer(player).setIsInMatch(false);

            player.teleport(globalSpawnLocation);
            plugin.getInventoryItemManager().addLobbyItemsToPlayer(player);
            players.remove(playerManager.getDuelPlayer(player));
            scores.remove(playerManager.getDuelPlayer(player));
        }
    }

    public void addPlayer(Player player) {
        if (gameState == GameState.COUNTDOWN && players.size() == 2) {
            player.sendMessage("Game is in countdown phase but max players is 2...");
            return;
        }
        if (gameState == GameState.RUNNING) {
            player.sendMessage("Game is running you can't join at this time.");
            return;
        }
        if (gameState == GameState.CLOSING) {
            player.sendMessage("Game is closing hang tight!");
            return;
        }
        player.getInventory().clear();
        if (gameState == GameState.OPEN) {
            player.setGameMode(GameMode.ADVENTURE);
            if (players.isEmpty()) {
                this.players.add(playerManager.getDuelPlayer(player));
                this.scores.put(playerManager.getDuelPlayer(player), 0);
                playerManager.getDuelPlayer(player).setIsInMatch(true);
                player.teleport(spawnLocationOne);
            } else {
                this.players.add(playerManager.getDuelPlayer(player));
                playerManager.getDuelPlayer(player).setIsInMatch(true);
                this.scores.put(playerManager.getDuelPlayer(player), 0);
                player.teleport(spawnLocationTwo);
                countdown.start();
            }
        }
        sendMessage(ChatColor.GREEN + player.getDisplayName() + " joined the match! " + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + "2");
    }

    public void reset(boolean kickPlayers) {
        if (!players.isEmpty()) {
            for (DuelsPlayer duelsPlayer : players) {
                duelsPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                duelsPlayer.getPlayer().setHealth(20);
                if (kickPlayers) {
                    if (duelsPlayer.getPlayer().isDead()) duelsPlayer.getPlayer().spigot().respawn();
                    duelsPlayer.getPlayer().getActivePotionEffects().forEach(effect -> {
                        duelsPlayer.getPlayer().removePotionEffect(effect.getType());
                    });
                    duelsPlayer.getPlayer().teleport(globalSpawnLocation);
                }
                duelsPlayer.setIsInMatch(false);
                plugin.getInventoryItemManager().addLobbyItemsToPlayer(duelsPlayer.getPlayer());
            }
        }

        game.unregister();
        players.clear();
        this.countdown = new BridgeCountdown(plugin, this);
        this.game = new BridgeGame(plugin, this, kit, spawnLocationOne, spawnLocationTwo);
        setGameState(GameState.OPEN);
    }

    public void sendTitle(String message) {
        for (DuelsPlayer duelsPlayer : players) {
            duelsPlayer.getPlayer().sendTitle(message, "");
            duelsPlayer.getPlayer().playSound(duelsPlayer.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 100);
        }
    }
    public void sendTitle(String message, String subtitle) {
        for (DuelsPlayer duelsPlayer : players) {
            duelsPlayer.getPlayer().sendTitle(message, subtitle);
        }
    }

    @Getter
    public GameState getGameState() { return gameState; }
    @Getter
    public List<DuelsPlayer> getPlayers() { return players; }
    @Getter
    public PlayerManager getArenaPlayerManager() { return this.playerManager; }
    @Getter
    public String getArenaName() { return arenaName; }
    @Getter
    public KitType getKitType() { return this.kitType; }
    @Getter
    public Location getGoal1() { return goal1; }
    @Getter
    public Location getGoal2() { return goal2; }
    @Getter
    public Location getSpawn1() { return spawnLocationOne; }
    @Getter
    public Location getSpawn2() { return spawnLocationTwo; }
    @Setter
    public void setGameState(GameState state) { this.gameState = state; }

}
