package me.ceridev.duels.instance;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ceridev.duels.instance.game.BuildUHCGame;
import me.ceridev.duels.instance.game.ClassicGame;
import me.ceridev.duels.instance.game.Countdown;
import me.ceridev.duels.instance.game.Game;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.game.BlockManager;
import me.ceridev.duels.manager.kits.Kit;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BasicArena {

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
    private BlockManager blockManager = null;
    private List<DuelsPlayer> players = new ArrayList<>();

    public BasicArena(DuelPlugin plugin, PlayerManager playerManager, String arenaName, KitType kitType, Location spawnLocationOne, Location spawnLocationTwo) {
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
        } else if (kitType == KitType.BUILDUHC) {
            this.game = new BuildUHCGame(plugin, this, spawnLocationOne, spawnLocationTwo, kit);
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
                playerManager.getDuelPlayer(player).setIsInMatch(true);
                player.teleport(spawnLocationOne);
            } else {
                this.players.add(playerManager.getDuelPlayer(player));
                playerManager.getDuelPlayer(player).setIsInMatch(true);
                player.teleport(spawnLocationTwo);
                countdown.start();
            }
        }
        sendMessage(ChatColor.GREEN + player.getDisplayName() + " joined the match! " + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + "2");
    }

    public void removePlayer(Player player) {
        if (gameState == GameState.CLOSING) {
            players.remove(playerManager.getDuelPlayer(player));
            sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + player.getDisplayName() + " rage quit lol.");
            return;
        }
        if (gameState == GameState.RUNNING) {
            DuelsPlayer loser = playerManager.getDuelPlayer(player);
            players.remove(playerManager.getDuelPlayer(player));

            sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + player.getDisplayName() + " left during the match.");
            setWinner(players.get(0), loser);
        } else if (gameState == GameState.COUNTDOWN) {
            players.remove(playerManager.getDuelPlayer(player));
            countdown.cancel();
            this.countdown = new Countdown(plugin, this);
            sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC + "Countdown stopped! A player left before the match started.");
            setGameState(GameState.OPEN);
        } else if (gameState == GameState.OPEN) {
            playerManager.getDuelPlayer(player).setIsInMatch(false);
            player.teleport(globalSpawnLocation);
            plugin.getInventoryItemManager().addLobbyItemsToPlayer(player);
            players.remove(playerManager.getDuelPlayer(player));
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
        this.countdown = new Countdown(plugin, this);

        if (kitType == KitType.CLASSIC) {
            this.game = new ClassicGame(plugin, this, spawnLocationOne, spawnLocationTwo, kit);
        } else if (kitType == KitType.BUILDUHC) {
            this.game = new BuildUHCGame(plugin, this, spawnLocationOne, spawnLocationTwo, kit);
        }
        setGameState(GameState.OPEN);
    }

    public void sendMessage(String message) {
        for (DuelsPlayer duelsPlayer : players) {
            duelsPlayer.getPlayer().sendMessage(message);
        }
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
    @Setter
    public void setGameState(GameState state) { this.gameState = state; }

}
