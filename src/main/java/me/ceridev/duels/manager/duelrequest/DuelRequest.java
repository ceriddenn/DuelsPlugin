package me.ceridev.duels.manager.duelrequest;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.DuelsPlayer;

public class DuelRequest {

    private DuelsPlayer requester;
    private DuelsPlayer receiver;

    public DuelRequest(DuelsPlayer requester, DuelsPlayer receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }

    @Getter
    public DuelsPlayer getRequester() { return requester; }
    @Getter
    public DuelsPlayer getReceiver() { return receiver; }

}
