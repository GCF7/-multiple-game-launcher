package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessBoard;
import es.ucm.fdi.tp.chess.ChessBoard.Piece;
import es.ucm.fdi.tp.view.JBoard.Shape;
import es.ucm.fdi.tp.chess.ChessState;
import es.ucm.fdi.tp.was.WolfAndSheepAction;

public class ChessView extends RectBoardGameView<ChessState, ChessAction>{
	
	private boolean primerClick;
	private int colOrigen;
	private int filaOrigen;
	private int colDestino;
	private int filaDestino;
	private static int piecePlayer[] = loadPiecePlayers();

	public ChessView(ChessState state, GameController<ChessState, ChessAction> gameCtrl, int player,
			int contadorJugadores) {
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
		if (state != null && !ChessBoard.empty((byte)state.at(row, col))) return this.state.at(row, col);
		else return -1;
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
				ChessAction A = new ChessAction(player, this.filaOrigen, this.colOrigen, this.filaDestino, this.colDestino);				
				if (state.isValid(A)){
					this.gameCtrl.makeManualMove(A);
				} else {
					 A = getSpecialAction(A);
					 if (A != null) {
						 this.gameCtrl.makeManualMove(A);
					 } else {
						 this.showInfoMessage("Invalid move!");
					 }
				}
			}
		}
	}

	private ChessAction getSpecialAction(ChessAction action) {
		List<ChessAction> actions = state.validActions(state.getTurn());
		for (ChessAction a : actions) {
			if (a.getSrcRow() == action.getSrcRow() &&
					a.getSrcCol() == action.getSrcCol() &&
					a.getDstRow() == action.getDstRow() &&
					a.getDstCol() == action.getDstCol()) {
				return a;
			}
		}
		return null;
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
	    
        String iconName = "es/ucm/fdi/tp/images/" +ChessBoard.Piece.iconName(state.getBoard().get(row, col));

        return iconName;

    }
	
	private static int[] loadPiecePlayers() {
		int[] pp = new int[Piece.Empty.white()+1];  // Kludge to dimension the array!
		for (Piece p : Piece.values()) {
			if (p != Piece.Empty && p != Piece.Outside) {
				pp[p.white()] = ChessState.WHITE;
				pp[p.black()] = ChessState.BLACK;
			}
		}
		pp[Piece.Empty.white()] = -1;
		return pp;
	}
	
	@Override
	protected Color getPlayerColor(int piece) {
		int player = piecePlayer[piece];
		return player == ChessState.BLACK || player == ChessState.WHITE ? this.playerTable.getPlayerColor(player) : null;
	}

	@Override
	protected Shape getShape(int player) {
		return Shape.RECTANGLE;
	}
}
