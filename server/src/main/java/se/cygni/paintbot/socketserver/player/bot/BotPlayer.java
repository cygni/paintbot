package se.cygni.paintbot.socketserver.player.bot;

import se.cygni.paintbot.api.event.*;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.request.RegisterMove;
import se.cygni.paintbot.socketserver.player.BasePlayer;

import java.util.Objects;

public abstract class BotPlayer extends BasePlayer {

    protected final String playerId;
//    protected final EventBus incomingEventbus;

//    public BotPlayer(String playerId, EventBus incomingEventbus) {
    public BotPlayer(String playerId) {
        this.playerId = playerId;
//        this.incomingEventbus = incomingEventbus;
    }

    public void registerMove(MapUpdateEvent mapUpdateEvent, CharacterAction action) {
        RegisterMove registerMove = new RegisterMove(mapUpdateEvent.getGameId(), mapUpdateEvent.getGameTick(), action);
        registerMove.setReceivingPlayerId(playerId);
//        incomingEventbus.post(registerMove);
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {

    }

    @Override
    public void onCharacterStunned(CharacterStunnedEvent characterStunnedEvent) {

    }

    @Override
    public void onGameEnded(GameEndedEvent gameEndedEvent) {

    }

    @Override
    public void onGameResult(GameResultEvent gameResultEvent) {

    }

    @Override
    public void onTournamentEnded(TournamentEndedEvent tournamentEndedEvent) {

    }

    @Override
    public void onGameStart(GameStartingEvent gameStartingEvent) {

    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotPlayer botPlayer = (BotPlayer) o;

        return Objects.equals(playerId, botPlayer.playerId);
    }

    @Override
    public int hashCode() {
        return playerId != null ? playerId.hashCode() : 0;
    }
}
