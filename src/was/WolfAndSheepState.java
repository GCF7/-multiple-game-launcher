package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.ttt.TttAction;

public class WolfAndSheepState extends GameState<WolfAndSheepState, WolfAndSheepAction> {
	
private static final long serialVersionUID = -2641387354190472378L;
	
	private final int turn;
    private final boolean finished;
    private final int[][] board;
    private final int winner;

    final static int EMPTY = -1, DIM = 8, OVEJA = 1, LOBO = 0;

    /**
     * Constructora sin parametros
     */
	public WolfAndSheepState() {
		super(2);
		board = new int[DIM][];
        for (int i=0; i<DIM; i++) {
            board[i] = new int[DIM];
            for (int j=0; j<DIM; j++) board[i][j] = EMPTY;
        }
        for(int i = 1; i < DIM; i =i+2) {
        	board[0][i] = OVEJA;
        }
        board[DIM - 1][0] = LOBO;
        this.turn = 0;
        this.winner = -1;
        this.finished = false;
	}
	
	/**
	 * Constructora con parametros
	 * @param prev Estado anterior
	 * @param board tablero
	 * @param finished terminado
	 * @param winner ganador
	 */
    public WolfAndSheepState(WolfAndSheepState prev, int[][] board, boolean finished, int winner) {
    	super(2);
        this.board = board;
        this.turn = (prev.turn + 1) % 2;
        this.finished = finished;
        this.winner = winner;
    } 

    /**
     * Devuelve el turno
     * @param turn
     */
	@Override
	public int getTurn() {
		// TODO Auto-generated method stub
		return turn;
	}
	
	 public boolean isValid(WolfAndSheepAction action) {
		 boolean encontrado = false;
	        if (isFinished()) {
	            return false;
	        }
	        List<WolfAndSheepAction> list = this.validActions(action.getPlayer());
	        int i = 0;
	        while(!encontrado && i < list.size()) {
	        	if(list.get(i).equals(action)) encontrado = true;
	        	i++;
	        }
	        return encontrado;
	  }
	 
	/**
	 * Busca las acciones validas (jugadas)
	 * @param player jugador
	 * @return valid Lista de acciones validas
	 */
	@Override
	public List<WolfAndSheepAction> validActions(int player) {
		// TODO Auto-generated method stub
		ArrayList<WolfAndSheepAction> valid = new ArrayList<>();
		 if (finished) {
	            return valid;
	        }
		 else {
			 if(player == LOBO) {
				 int i = 0, j = 0;
				 boolean encontrado = false; //Encontrado indica si se ha encontrado el lobo
				 while(i < DIM && !encontrado) {
					 j = 0;
					 while(j <DIM && !encontrado) {
						 if(board[i][j] == LOBO){
							 if(i - 1 >= 0 && j - 1  >= 0 && board[i-1][j-1] == EMPTY) valid.add(new WolfAndSheepAction(player, i, j, i - 1, j - 1));
							 if (i - 1 >= 0 && j + 1 < DIM && board [i-1][j+1] == EMPTY) valid.add(new WolfAndSheepAction(player, i, j, i - 1, j + 1));
							 if (i + 1 < DIM && j - 1 >= 0 && board[i+1][j - 1] == EMPTY) valid.add(new WolfAndSheepAction(player, i, j, i + 1, j - 1));
							 if( i + 1 < DIM && j + 1 < DIM && board[i + 1][j + 1] == EMPTY) valid.add(new WolfAndSheepAction(player, i, j, i + 1, j + 1));
							 encontrado = true;
						 }
						 else j++;
					 }
					 i++;
				 }
			 }
			 else if (player == OVEJA) {
				 int i = 0, j = 0, count = 0; // count cuenta cuantas veces se han encontrado ovejas, para salir del bucle al encontrar 4
				 while(i < DIM && count < DIM/2) {
					 j = 0;
					 while(j <DIM && count < DIM/2) {
						 if(board[i][j] == OVEJA){
							 if (i + 1 < DIM && j - 1 >= 0 && board[i+1][j - 1] == EMPTY) valid.add(new WolfAndSheepAction(player, i, j, i + 1, j - 1));
							 if( i + 1 < DIM && j + 1 < DIM && board[i + 1][j + 1] == EMPTY) valid.add(new WolfAndSheepAction(player, i, j, i + 1, j + 1));
							 count++;
						 }
						 j++;
					 }
					 i++;
				 }
			 }
		 }
		return valid;
	}
	
	/**
	 * Busca si el jugador ha ganado
	 * @param board2 tablero
	 * @param player jugador
	 * @return winner true si es ganador o false si no lo es
	 */
	public static boolean isWinner(int[][] board2, int player) {
		boolean encontrado = false, posible = false; //Encontrado indica si se ha encontrado el lobo y posible indica si el lobo puede realizar algún movimiento
        if(player == OVEJA) {
        	int i = 0, j = 0;
        	while(i < DIM && !encontrado) {
        		j = 0;
				while(j <DIM && !encontrado) {
					 if(board2[i][j] == LOBO){
						 if(i - 1 >= 0 && j - 1  >= 0 && board2[i-1][j-1] == EMPTY) posible = true;
						 else if (i - 1 >= 0 && j + 1 < DIM && board2 [i-1][j+1] == EMPTY) posible = true;
						 else if (i + 1 < DIM && j - 1 >= 0 && board2[i+1][j - 1] == EMPTY) posible = true;
						 else if( i + 1 < DIM && j + 1 < DIM && board2[i + 1][j + 1] == EMPTY) posible = true;
						 encontrado = true;
					 }
					 else j++;
				 }
				 i++;
			 }
        	 if(!posible) return true; // Si el lobo no puede hacer movimientos se acabo, han ganado las ovejas
        	 else return false;
        }
        else {
        	for(int j = 0; j < DIM && !encontrado; j++) {
        		if(board2[0][j] == LOBO) encontrado = true;
        	}
        	if (!encontrado){
        		int i = 0, j = 0, count = 0; // count cuenta cuantas veces se han encontrado ovejas, para salir del bucle al encontrar 4
        		while(i < DIM && count < DIM/2 && !posible) {
   				 j = 0;
   				 while(j <DIM && count < DIM/2 && !posible) {
   					 if(board2[i][j] == OVEJA){
   						 if (i + 1 < DIM && j - 1 >= 0 && board2[i+1][j - 1] == EMPTY) posible = true;
   						 else if( i + 1 < DIM && j + 1 < DIM && board2[i + 1][j + 1] == EMPTY) posible = true;
   						 count++;
   					 }
   					 j++;
   				 }
   				 i++;
        		}
        		return !posible;
        	}
        	else return encontrado;
        }
    } 
	/**
	 *Devuelve si el juego ha terminado o no
	 *@return finished
	 */
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return finished;
	}

	/**
	 *Devuelve el ganador
	 *@return winner 0 = jugador 0, 1 = jugador 1, -1 = no ganador
	 */
	@Override
	public int getWinner() {
		// TODO Auto-generated method stub
		return winner;
	}
	
	/**
	 *Devuelve el tablero
	 *@return copy Tablero copiado
	 */
	public int[][] getBoard() {
		// TODO Auto-generated method stub
		int[][] copy = new int[board.length][];
        for (int i=0; i<board.length; i++) copy[i] = board[i].clone();
        return copy;
	}
	
	/**
	 * Metodo que devuelve un string para mostrar por pantalla un objeto de la clase
	 * @return string de un objeto la clase
	 */
	/**
	 * Metodo que devuelve un string para mostrar por pantalla un objeto de la clase
	 * @return string de un objeto la clase
	 */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("     " + 0);
        for (int i=1; i<board.length; i++) {
        	if(i <= 10) sb.append("  " + i);
        	else sb.append(" " + i);
        }
        sb.append("\n");
        for (int i=0; i<board.length; i++) {
            if (i < 10) sb.append(i + "  |");
            else sb.append(i + " |");
            for (int j=0; j<board.length; j++) {
            	if (board[i][j] == EMPTY) {
            		if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) sb.append(" . ");
            		else sb.append("   ");
            	}
            	else if (board[i][j] == LOBO) sb.append(" W ");
            	else sb.append(" S ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public int getDimension() {
    	return DIM;
    }
    
    public String getGameDescreption() {
    	return "Wolf and Sheep";
    }

    public int at(int row, int col) {
        return board[row][col];
    }

}
