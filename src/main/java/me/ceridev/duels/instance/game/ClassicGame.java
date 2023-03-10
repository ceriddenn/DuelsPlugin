package me.ceridev.duels.instance.game;

import me.ceridev.duels.instance.*;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.kits.Kit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ClassicGame extends Game {

    private DuelPlugin plugin;
    private final Location spawnLocationOne;
    private final Location spawnLocationTwo;

    private final Kit kit;

    public ClassicGame(DuelPlugin plugin, BasicArena basicArena, Location spawnLocationOne, Location spawnLocationTwo, Kit kit) {
        super(plugin, basicArena);
        this.plugin = plugin;
        this.spawnLocationOne = spawnLocationOne;
        this.spawnLocationTwo = spawnLocationTwo;
        this.kit = kit;
    }

    @Override
    public void onStart() {
        // tp players
        DuelsPlayer playerOne = basicArena.getPlayers().get(0);
        DuelsPlayer playerTwo = basicArena.getPlayers().get(1);

        playerOne.getPlayer().teleport(spawnLocationOne);
        playerOne.getPlayer().setGameMode(GameMode.SURVIVAL);

        playerTwo.getPlayer().teleport(spawnLocationTwo);
        playerTwo.getPlayer().setGameMode(GameMode.SURVIVAL);
        kit.addKitToInventory(playerOne);
        kit.addKitToInventory(playerTwo);
        basicArena.sendMessage("Game has started! First to kill the other wins!");
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (basicArena.getGameState() == GameState.OPEN || basicArena.getGameState() == GameState.COUNTDOWN || basicArena.getGameState() == GameState.RUNNING || basicArena.getGameState() == GameState.CLOSING) {
            for (DuelsPlayer duelsPlayer : basicArena.getPlayers()) {
                if (duelsPlayer.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                    event.setCancelled(true);
                }
            }
        }
        Player playerWhoDiedKiller = (Player) event.getDamager();
        Player playerWhoDied = (Player) event.getEntity();
        PlayerManager aPM = basicArena.getArenaPlayerManager();
        if (basicArena.getPlayers().contains(aPM.getDuelPlayer(playerWhoDied)) && basicArena.getPlayers().contains(aPM.getDuelPlayer(playerWhoDiedKiller)) && basicArena.getGameState() == GameState.RUNNING) {
            if (playerWhoDied.getHealth() <= event.getFinalDamage()) {
                playerWhoDied.setHealth(20);
                basicArena.setWinner(aPM.getDuelPlayer(playerWhoDiedKiller), aPM.getDuelPlayer(playerWhoDied));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("duels.plugin.build")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void hungerDepletionEvent(FoodLevelChangeEvent event) {
        Player p = (Player) event.getEntity();
        p.setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler
    public void itemDropEvent(PlayerDropItemEvent event) {
        if (!plugin.getPlayerManager().getDuelPlayer(event.getPlayer()).isInMatch()) return;
        if (this.basicArena.getPlayers().contains(plugin.getPlayerManager().getDuelPlayer(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

}

