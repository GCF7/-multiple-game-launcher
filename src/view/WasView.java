package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class WasView extends RectBoardGameView<WolfAndSheepState, WolfAndSheepAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean primerClick;
	private int colOrigen;
	private int filaOrigen;
	private int colDestino;
	private int filaDestino;
	
	public WasView(WolfAndSheepState state, GameController<WolfAndSheepState, WolfAndSheepAction> gameCtrl, int player, int contadorJugadores) {
		super(state, gameCtrl, player, contadorJugadores);
		this.primerClick = false;
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
		if(this.enable) {
			if(!primerClick) {
				this.filaOrigen = row;
				this.colOrigen = col;
				this.primerClick = true;
				this.showInfoMessage("First cell selected");
			}
			else{
				this.primerClick = false;
				this.filaDestino = row;
				this.colDestino = col;
				//Llamo a controlador
				this.gameCtrl.makeManualMove(new WolfAndSheepAction(player, this.filaOrigen, this.colOrigen, this.filaDestino, this.colDestino));
			}
		}
	}

	@Override
	protected void keyTyped(int keyCode) {
		// TODO Auto-generated method stub
		if(this.enable){
			if(keyCode == KeyEvent.VK_ESCAPE && this.primerClick){
				this.primerClick = false;
				this.msgArea.addContent("First cell deselected");
			}
		}
	}

	@Override
	protected Color getBackground(int row, int col) { 
		if ((row % 2 == 0 && col % 2 == 1) || (row % 2 == 1 && col % 2 == 0)) return new Color(25, 0, 51);
		else return Color.WHITE;
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
