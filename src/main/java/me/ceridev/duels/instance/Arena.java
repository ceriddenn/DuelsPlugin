package me.ceridev.duels.instance;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ceridev.duels.instance.game.ClassicGame;
import me.ceridev.duels.instance.game.Game;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private final DuelPlugin plugin;
    private Countdown countdown;

    private GameState gameState;
    private String arenaName;
    private KitType kitType;
    private Kit kit;
    private Game game;
    private Location spawnLocationOne;
    private Location spawnLocationTwo;
    private Location globalSpawnLocation;
    private PlayerManager playerManager;
    private List<DuelsPlayer> players = new ArrayList<>();

    public Arena(DuelPlugin plugin, PlayerManager playerManager, String arenaName, KitType kitType, Location spawnLocationOne, Location spawnLocationTwo) {
        this.plugin = plugin;
        this.countdown = new Countdown(plugin, this);
        setGameState(GameState.OPEN);
        this.playerManager = playerManager;
        this.arenaName = arenaName;
        this.kitType = kitType;
        this.kit = plugin.getKitManager().retrieveKit(this.kitType);
        this.spawnLocationOne = spawnLocationOne;
        this.spawnLocationTwo = spawnLocationTwo;
        this.globalSpawnLocation = plugin.getDatabaseFile().getGlobalSpawnLocation();
        // register new game
        if (kitType == KitType.CLASSIC) {
            this.game = new ClassicGame(plugin, this, spawnLocationOne, spawnLocationTwo, kit);
        }
    }

    public void setWinner(DuelsPlayer winner, DuelsPlayer loser) {
        sendMessage(ChatColor.BLUE + winner.getPlayer().getDisplayName() + " won the match!");
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

    public void addPlayer(Player player) {
        playerManager.getDuelPlayer(player).setIsInMatch(true);
        player.getInventory().clear();
        if (gameState == GameState.OPEN) {
            player.setGameMode(GameMode.ADVENTURE);
            if (players.isEmpty()) {
                this.players.add(playerManager.getDuelPlayer(player));
                player.teleport(spawnLocationOne);
            } else {
                this.players.add(playerManager.getDuelPlayer(player));
                player.teleport(spawnLocationTwo);
                countdown.start();
            }
        } else if (gameState == GameState.COUNTDOWN && players.size() == 2) {
            player.sendMessage("Game is in countdown phase but max players is 2...");
        } else if (gameState == GameState.RUNNING) {
            player.sendMessage("Game is running you can't join at this time.");
        }
    }

    public void removePlayer(Player player) {
        if (gameState == GameState.RUNNING) {
            DuelsPlayer loser = playerManager.getDuelPlayer(player);
            players.remove(playerManager.getDuelPlayer(player));
            if (!players.isEmpty()) {
                sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "A player has left during the match.");
                setWinner(players.get(0), loser);
            } else {
                reset(false);
            }
        } else if (gameState == GameState.COUNTDOWN) {
            players.remove(playerManager.getDuelPlayer(player));
            countdown.cancel();
            this.countdown = new Countdown(plugin, this);
            sendMessage(ChatColor.RED + "Countdown stopped... a player left before the match started...");
            setGameState(GameState.OPEN);
        } else if (gameState == GameState.OPEN) {
            playerManager.getDuelPlayer(player).setIsInMatch(false);
            player.teleport(globalSpawnLocation);
            plugin.getInventoryItemManager().addLobbyItemsToPlayer(player);
            players.clear();
        }
    }

    public void start() { game.start(); }

    public void reset(boolean kickPlayers) {
        if (!players.isEmpty()) {
            for (DuelsPlayer duelsPlayer : players) {
                duelsPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                duelsPlayer.getPlayer().setHealth(20);
                if (kickPlayers) {
                    if (duelsPlayer.getPlayer().isDead()) duelsPlayer.getPlayer().spigot().respawn();
                    duelsPlayer.getPlayer().teleport(globalSpawnLocation);
                }
                duelsPlayer.setIsInMatch(false);
                plugin.getInventoryItemManager().addLobbyItemsToPlayer(duelsPlayer.getPlayer());
            }
        }

        setGameState(GameState.OPEN);
        game.unregister();
        players.clear();
        this.countdown = new Countdown(plugin, this);

        if (kitType == KitType.CLASSIC) {
            this.game = new ClassicGame(plugin, this, spawnLocationOne, spawnLocationTwo, kit);
        }
    }

    public void sendMessage(String message) {
        for (DuelsPlayer duelsPlayer : players) {
            duelsPlayer.getPlayer().sendMessage(message);
        }
    }

    public void sendTitle(String message) {
        for (DuelsPlayer duelsPlayer : players) {
            duelsPlayer.getPlayer().sendTitle(message, "");
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
    @Setter
    public void setGameState(GameState state) { this.gameState = state; }

}
