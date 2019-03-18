package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class ConsoleView<S extends GameState<S,A>, A extends GameAction<S,A>>
implements GameObserver<S,A>{

	public ConsoleView(GameObservable<S,A> gameTable) {
		// add code here
		gameTable.addObserver(this);
	}
	
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		// TODO Auto-generated method stub
		System.out.println("Current state");
		System.out.println(e.getState());
		if (e.getState().isFinished() && e.getType() == EventType.Stop) {
			//game over
			String endText = "The game ended: ";
			int winner = e.getState().getWinner();
			if (winner == -1) endText += "draw!";
			else {
				endText = "player " + winner + " has won";
			}
			System.out.println(endText);
		}
		else System.out.println("Turn for player " + e.getState().getTurn());
	}
}

