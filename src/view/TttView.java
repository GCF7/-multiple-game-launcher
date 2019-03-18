package es.ucm.fdi.tp.view;

import java.awt.Color;

import javax.swing.ImageIcon;

import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;


public class TttView extends RectBoardGameView<TttState, TttAction>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TttView(TttState state, GameController<TttState, TttAction> gameCtrl, int player, int contadorJugadores) {
		super(state, gameCtrl, player, contadorJugadores);
	}
	@Override
	protected int getNumCols() {
		// TODO Auto-generated method stub
		return this.state.getDimension();
	}

	@Override
	protected int getNumRows() {
		// TODO Auto-generated method stub
		return this.state.getDimension();
	}

	@Override
	protected Integer getPosition(int row, int col) {
		// TODO Auto-generated method stub
		return this.state.at(row, col);
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		// TODO Auto-generated method stub
		if (this.enable){
			this.gameCtrl.makeManualMove(new TttAction(player, row, col));
		}
	}

	@Override
	protected void keyTyped(int keyCode) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected Color getBackground(int row, int col) { 
		return new Color(25, 0, 51);
	}
	@Override
	protected int getSepPixels() {
		return 4;
	}
	@Override
	protected String getImage(int row, int col) {
		// TODO Auto-generated method stub
		return null;
	}

}
