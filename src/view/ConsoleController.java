package es.ucm.fdi.tp.view;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;

public class ConsoleController<S extends GameState<S,A>, A extends GameAction<S,A>> implements Runnable {
// add fields here
	protected GameTable<S,A> gameTable;
	protected List<GamePlayer> players;
	protected boolean stopped;
	
	public ConsoleController(List<GamePlayer> players, GameTable<S,A> game) {
		this.players = players;
		this.gameTable = game;
	}
	
	public void run() {
	// add code here
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
									// playerNumber
		}
		if(playerCount > this.gameTable.getState().getPlayerCount() || playerCount > this.gameTable.getState().getPlayerCount()){
			System.out.println("Numero de jugadores incorrecto");
		}
		else {
		this.gameTable.start();		
		while (!this.gameTable.getState().isFinished()) {
			// request move
			A action = players.get(this.gameTable.getState().getTurn()).requestAction(this.gameTable.getState());
			try{
			// apply move
			this.gameTable.execute(action);
			}
			catch(GameError e){
				System.out.println(e.getMessage());
			}
			if (this.gameTable.getState().isFinished()) {
				this.gameTable.stop();
			}
		}
		}
	}
}

