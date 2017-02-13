package net.wouto.tntrun.game;

import net.wouto.tntrun.game.state.StateLobby;
import net.wouto.tntrun.game.state.StateMainGame;

public enum GameStateType {

    LOBBY(StateLobby.class),
    GAME(StateMainGame.class);

    private Class<? extends GameState> stateType;

    GameStateType(Class<? extends GameState> stateType) {
        this.stateType = stateType;
    }

    public Class<? extends GameState> getStateType() {
        return stateType;
    }
}
