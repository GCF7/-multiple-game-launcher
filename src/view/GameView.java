package es.ucm.fdi.tp.view;

import javax.swing.JComponent;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public abstract class GameView <S extends GameState<S,A>, A extends GameAction<S,A>>
extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public abstract void enable();
	public abstract void disable();
	public abstract void update(S state); //actualiza la vista.
	public abstract void setController(GameController<S, A>
	gameCtrl);
	public abstract void showInfoMessage(String msg);
	public abstract void sendMessage (String msg);

}
