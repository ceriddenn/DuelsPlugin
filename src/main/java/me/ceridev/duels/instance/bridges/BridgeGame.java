package me.ceridev.duels.instance.bridges;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.instance.GameState;
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

public class BridgeGame extends SpecialGame {
    private Kit kit;
    private BridgeArena bridgeArena;
    private Location spawnLocationOne;
    private Location spawnLocationTwo;
    public BridgeGame(DuelPlugin plugin, BridgeArena bridgeArena, Kit kit, Location spawnLocationOne, Location spawnLocationTwo) {
        super(plugin, bridgeArena);
        this.bridgeArena = bridgeArena;
        this.kit = kit;
        this.spawnLocationOne = spawnLocationOne;
        this.spawnLocationTwo = spawnLocationTwo;
    }

    @Override
    public void onStart() {
        DuelsPlayer playerOne = bridgeArena.getPlayers().get(0);
        DuelsPlayer playerTwo = bridgeArena.getPlayers().get(1);

        playerOne.getPlayer().teleport(spawnLocationOne);
        playerOne.getPlayer().setGameMode(GameMode.SURVIVAL);

        playerTwo.getPlayer().teleport(spawnLocationTwo);
        playerTwo.getPlayer().setGameMode(GameMode.SURVIVAL);
        kit.addKitToInventory(playerOne);
        kit.addKitToInventory(playerTwo);
        bridgeArena.sendMessage("Game has started! First to kill the other wins!");
    }
    // add void checking event
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (bridgeArena.getGameState() == GameState.OPEN || bridgeArena.getGameState() == GameState.COUNTDOWN || bridgeArena.getGameState() == GameState.RUNNING || bridgeArena.getGameState() == GameState.CLOSING) {
            for (DuelsPlayer duelsPlayer : bridgeArena.getPlayers()) {
                if (duelsPlayer.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                    event.setCancelled(true);
                }
            }
        }
        Player playerWhoDiedKiller = (Player) event.getDamager();
        Player playerWhoDied = (Player) event.getEntity();
        PlayerManager aPM = bridgeArena.getArenaPlayerManager();
        if (bridgeArena.getPlayers().contains(aPM.getDuelPlayer(playerWhoDied)) && bridgeArena.getPlayers().contains(aPM.getDuelPlayer(playerWhoDiedKiller)) && bridgeArena.getGameState() == GameState.RUNNING) {
            if (playerWhoDied.getHealth() <= event.getFinalDamage()) {
                playerWhoDied.setHealth(20);
                int pIndex;
                pIndex = bridgeArena.getPlayers().indexOf(aPM.getDuelPlayer(playerWhoDied));
                if (pIndex == 0) {
                    playerWhoDied.teleport(bridgeArena.getSpawn1());
                } else {
                    playerWhoDied.teleport(bridgeArena.getSpawn2());
                }
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
        if (this.bridgeArena.getPlayers().contains(plugin.getPlayerManager().getDuelPlayer(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }
}
