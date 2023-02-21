package me.ceridev.duels.instance;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Arrays;

public class DuelsPlayer {

    private Player player;
    private DuelPlugin plugin;
    //settings
    private boolean duelRequestsOpen;
    private KitType preferredKitQueueType;
    //stats
    private int totalWins;
    private int totalLosses;
    private int totalKills;
    private int totalDeaths;
    private boolean isInMatch;

    public Scoreboard board;
    public Objective obj;

    private Team playersInQueue;
    private Team playerStats;
    private Team gameStatus;

    public DuelsPlayer(DuelPlugin plugin, Player player, int tW, int tL, int tK, int tD, boolean duelRequestsOpen, KitType preferredKitQueueType) {
        this.plugin = plugin;
        this.player = player;
        this.totalWins = tW;
        this.totalLosses = tL;
        this.totalKills = tK;
        this.totalDeaths = tD;
        this.duelRequestsOpen = duelRequestsOpen;
        this.preferredKitQueueType = preferredKitQueueType;
        this.isInMatch = false;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.obj = board.registerNewObjective(player.getDisplayName().toLowerCase()+"board", player.getDisplayName().toLowerCase());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "  MysterMC " + ChatColor.GREEN + "[Practice]");

        Score website = obj.getScore(ChatColor.YELLOW + "  store.mystermc.com");
        website.setScore(1);

        Score space = obj.getScore("");
        space.setScore(2);
    }

    public void setScoreboard() {
        if (this.isInMatch()) {
            if (!(board.getTeam("queueteam") == null)) board.getTeam("queueteam").unregister();
            if (!(board.getTeam("playerstats") == null)) board.getTeam("playerstats").unregister();
            if (board.getTeam("game_status") == null) {
                gameStatus = board.registerNewTeam("game_status");
                gameStatus.addEntry(ChatColor.BLUE.toString());
                gameStatus.setPrefix(ChatColor.GRAY + "Game: ");
                gameStatus.setSuffix(ChatColor.WHITE + String.valueOf(plugin.getArenaManager().getArena(player).getGameState()));
                obj.getScore(ChatColor.BLUE.toString()).setScore(3);
            } else {
                gameStatus.setSuffix(ChatColor.WHITE + String.valueOf(plugin.getArenaManager().getArena(player).getGameState()));
            }
        } else {
            if (!(board.getTeam("game_status") == null)) board.getTeam("game_status").unregister();
            // overrides multiple registering and unregister -- stops flickering
            int totalKills = plugin.getMongoManager().getDuelUsersDetails(player).getEmbedded(Arrays.asList("stats", "totalKills"), Integer.class);
            if (board.getTeam("queueteam") == null && board.getTeam("playerstats") == null) {
                playersInQueue = board.registerNewTeam("queueteam");
                playersInQueue.addEntry(ChatColor.BLUE.toString());
                playersInQueue.setPrefix(ChatColor.GRAY.toString() + ChatColor.BOLD + "  Queue: ");
                playersInQueue.setSuffix(ChatColor.WHITE + String.valueOf(plugin.getQueueManager().getQueue().size()));
                obj.getScore(ChatColor.BLUE.toString()).setScore(3);

                playerStats = board.registerNewTeam("playerstats");
                playerStats.addEntry(ChatColor.WHITE.toString());
                playerStats.setPrefix(ChatColor.GRAY + ChatColor.BOLD.toString() + "  Kills:");
                playerStats.setSuffix(ChatColor.WHITE + String.valueOf(plugin.getMongoManager().getDuelUsersDetails(player).getEmbedded(Arrays.asList("stats", "totalKills"), Integer.class)));
                obj.getScore(ChatColor.GOLD.toString()).setScore(4);
            } else {
                playersInQueue.setSuffix(ChatColor.WHITE + String.valueOf(plugin.getQueueManager().getQueue().size()));
                playerStats.setSuffix(ChatColor.WHITE + " " + totalKills);
            }
        }


        player.setScoreboard(board);
    }

    @Getter
    public Player getPlayer() { return player; }
    @Getter
    // convert settings to seperate class later down the road
    public boolean getDuelRequestsOpen() { return duelRequestsOpen; }
    @Getter
    public KitType getPreferredKitQueueType() { return preferredKitQueueType; }
    @Getter
    public int getTotalWins() { return totalWins; }
    @Getter
    public int getTotalLosses() { return totalLosses; }
    @Getter
    public int getTotalKills() { return totalKills; }
    @Getter
    public int getTotalDeaths() { return totalDeaths; }
    @Getter
    public boolean isInMatch() { return this.isInMatch;}

    // ADDERS
    @Setter
    public void addWin(int winValue) { this.totalWins = this.totalWins + winValue; }
    @Setter
    public void addLoss(int lossValue) { this.totalLosses = this.totalLosses + lossValue; }
    @Setter
    public void addKill(int killValue) { this.totalKills = this.totalKills + killValue; }
    @Setter
    public void addDeath(int deathValue) { this.totalDeaths = this.totalDeaths + deathValue; }
    @Setter
    public void setDuelRequestsOpen(boolean value) { this.duelRequestsOpen = value; }
    @Setter
    public void setPreferredKitQueueType(KitType kitType) { this.preferredKitQueueType = kitType; }

    // SUBRACTORS

    @Setter
    public void removeWin(int winValue) { this.totalWins = this.totalWins - winValue; }
    @Setter
    public void removeLoss(int lossValue) { this.totalLosses = this.totalLosses - lossValue; }
    @Setter
    public void removeKill(int killValue) { this.totalKills = this.totalKills - killValue; }
    @Setter
    public void removeDeath(int deathValue) { this.totalDeaths = this.totalDeaths - deathValue; }
    @Setter
    public void setIsInMatch(boolean isInMatch) { this.isInMatch = isInMatch; }

    // sb stuff

    @Getter
    public Objective getSbObjective() { return this.obj; }
    @Getter
    public Scoreboard getBoard() { return this.board; }

}
