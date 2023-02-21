package me.ceridev.duels.manager.duelrequest;

import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DuelRequestManager {

    private List<DuelRequest> duelRequestList;
    private PlayerManager playerManager;

    public DuelRequestManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.duelRequestList = new ArrayList<>();
    }

    public boolean currentOutgoingRequest(DuelsPlayer duelsPlayer) {
        for (DuelRequest duelRequest : duelRequestList) {
            if (duelRequest.getRequester().equals(duelsPlayer)) {
                return true;
            }
        }
        return false;
    }

    public boolean currentIncomingRequest(DuelsPlayer duelsPlayer) {
        for (DuelRequest duelRequest : duelRequestList) {
            if (duelRequest.getReceiver().equals(duelsPlayer)) {
                return true;
            }
        }
        return false;
    }

    public void createNewDuelRequest(Player requester, Player receiver) {
        if (currentOutgoingRequest(playerManager.getDuelPlayer(requester))) {
            playerManager.getDuelPlayer(requester).getPlayer().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "You already sent a duel request. To cancel this the request already sent, run /request cancel");
            return;
        }
        duelRequestList.add(new DuelRequest(playerManager.getDuelPlayer(requester), playerManager.getDuelPlayer(receiver)));
    }


}
