package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameTable;

public interface GameViewController<S extends GameState<S,A>, A extends GameAction<S,A>> {
public void makeRandomMove();
public void makeSmartMove();
public void cancelSmartMove();
public void restartGame();
public void stopGame();
public void changePlayerMode(PlayerMode p);
public void previousState();
public void pause();
public PlayerMode getPlayerMode();
public int getPlayerId();
public GameTable<S, A> getModel();
}

