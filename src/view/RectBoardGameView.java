package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.JBoard.Shape;

import es.ucm.fdi.tp.view.JBoard;

public abstract class RectBoardGameView<S extends GameState<S, A>, A extends GameAction<S, A>> extends GameView<S,A> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected GameController<S, A> gameCtrl;
	protected S state;
	protected JComponent jboard;//
	protected MessageArea msgArea;
	protected PlayerInfoTable playerTable;
	protected ChatArea chat;
	protected JPanel panelCentral;
	protected boolean enable;
	protected int player; //Para las acciones(no es extrictamente necesario) y para el chat (parte opcional)
	private int contadorJugadores;
	
	public RectBoardGameView(S state, GameController<S, A> gameCtrl, int player, int contadorJugadores) {
		this.state = state;
		this.gameCtrl = gameCtrl;
		this.enable = false;
		this.player = player;
		this.contadorJugadores = contadorJugadores;
		initGUI();
	}
	
	private void initGUI() { 
		//TABLERO
		this.jboard = new JBoard() {// …
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				RectBoardGameView.this.mouseClicked(row, col, clickCount, mouseButton);
			}

			@Override
			protected void keyTyped(int keyCode) {
				RectBoardGameView.this.keyTyped(keyCode);
			}

			@Override
			protected Shape getShape(int player) {
				return RectBoardGameView.this.getShape(player);
			}

			@Override
			protected Integer getPosition(int row, int col) {
				return RectBoardGameView.this.getPosition(row, col);
			}

			@Override
			protected int getNumRows() {
				return RectBoardGameView.this.getNumRows();
			}

			@Override
			protected int getNumCols() {
				return RectBoardGameView.this.getNumCols();
			}

			@Override
			protected Color getColor(int player) {
				if (player != -1) return RectBoardGameView.this.getPlayerColor(player);
				else return null;
			}

			@Override
			protected Color getBackground(int row, int col) {
				return RectBoardGameView.this.getBackground(row,col);
			}

			@Override
			protected int getSepPixels() {
				return RectBoardGameView.this.getSepPixels();
			}

			@Override
			protected String getImage(int row, int col) {
				// TODO Auto-generated method stub
				return RectBoardGameView.this.getImage(row, col);
			}
		};
		this.jboard.setPreferredSize(new Dimension(600, 300));
		//PANEL DERECHA
		JPanel panelDerecha = new JPanel();
		panelDerecha.setPreferredSize(new Dimension(10, 30));
		panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
		this.msgArea = new MessageArea();
		panelDerecha.add(this.msgArea);
		this.playerTable = new PlayerInfoTable(contadorJugadores, this);
		panelDerecha.add(this.playerTable);
		this.chat = new ChatArea(this.gameCtrl, player);
		panelDerecha.add(chat);
		//Panel Central
		this.panelCentral = new JPanel();
		this.panelCentral.setLayout(new BoxLayout(this.panelCentral, BoxLayout.X_AXIS));
		this.panelCentral.add(jboard);
		this.panelCentral.add(panelDerecha, BorderLayout.WEST);
		this.panelCentral.setOpaque(true);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(this.panelCentral);
	 }

	protected Shape getShape(int player) {
		return Shape.CIRCLE;
	}
	protected Color getBackground(int row, int col) {
		return Color.BLACK; 
	}
	protected int getSepPixels() {
		return 0; 
	}

	protected Color getPlayerColor(int id) {
		return this.playerTable.getPlayerColor(id); 
	}

	@Override
	public void update(S state) {
		// TODO Auto-generated method stub
		this.state = state;
		this.repaint();
	}

	@Override
	public void setController(GameController<S, A> gameCtrl) {
		// TODO Auto-generated method stub
		this.gameCtrl = gameCtrl;
	}

	@Override
	public void showInfoMessage(String msg) {
		// TODO Auto-generated method stub
		this.msgArea.addContent(msg);
	}
	
	@Override
	public void sendMessage(String msg) {
		// TODO Auto-generated method stub
		this.chat.addContent(msg);
	}
	
	@Override
	public void enable() {
		// TODO Auto-generated method stub
		this.enable = true;
	}
	
	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.enable = false;
	}
	
	protected abstract int getNumCols();
	protected abstract int getNumRows();
	protected abstract Integer getPosition(int row, int col);
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	protected abstract void keyTyped(int keyCode);
	protected abstract String getImage(int row, int col);
}
