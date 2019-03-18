package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.extra.jcolor.ColorChooser;

public class PlayerInfoTable extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<Color> players;
	protected int contadorJugadores;
	protected JScrollPane playersInfo;
	private ColorChooser colorChooser;
	@SuppressWarnings("rawtypes")
	private RectBoardGameView board;
	
	private MyPlayersTableModel tModel;
	
	public PlayerInfoTable(int contadorJugadores, @SuppressWarnings("rawtypes") RectBoardGameView board){
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Player Information"));
		this.board = board;
		//this.setPreferredSize(new Dimension(250, 30));
		this.players = new ArrayList<Color> (contadorJugadores);
		this.contadorJugadores = contadorJugadores;
		Iterator<Color> iterador = Utils.colorsGenerator();
		for(int i = 0; i < this.contadorJugadores; i++) {			
			this.players.add(iterador.next());
		}
		initGUI();
	 }
	private void initGUI() {
		colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK); //Menu colores
		List<Color> colores = this.players; //Lista colores de cada jugador
		//CREAR TABLA
		tModel = new MyPlayersTableModel();
		for(int i = 0; i < this.contadorJugadores; i++){
			tModel.addPlayer("");
		}
		//PONER TABLA EN JTABLE 
		JTable table = new JTable(tModel){
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1)
					comp.setBackground(colores.get(row));
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};
		//AÑADIR FUNCIONALIDAD
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
				}
			}

		});	
		table.setSize(20, 30);
		//PONER JTABLE EN SCROLLBAR
		this.playersInfo = new JScrollPane(table);
		this.playersInfo.getViewport().setBackground(Color.WHITE);
		this.setSize(20, 30);
		//AÑADIR AL PANEL
		this.add(this.playersInfo);
	 }
	
	private void changeColor(int row) {
		colorChooser.setSelectedColorDialog(this.players.get(row));
		colorChooser.openDialog();
		if (colorChooser.getColor() != null) {
			this.players.set(row, colorChooser.getColor());
			this.board.repaint();
		}

	}
	
	public Color getPlayerColor(int p) {
		return this.players.get(p);
	}
}
