package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameAction;


public class WolfAndSheepAction implements GameAction<WolfAndSheepState, WolfAndSheepAction> {

	private static final long serialVersionUID = -8051073297973839926L;
	
	private int player;
    private int originRow;
    private int originCol;
    private int finalRow;
    private int finalCol;
    
    /**
     * Constructora de la clase
     * @param player2 jugador que tiene el turno
     * @param originRow2 Fila de origen
     * @param originCol2 Columna de origen
     * @param finalRow2 Fila de destino
     * @param finalCol2 Columna de destino
     */
	public WolfAndSheepAction(int player2, int originRow2, int originCol2, int finalRow2, int finalCol2) {
		// TODO Auto-generated constructor stub
		this.player = player2;
		this.originRow = originRow2;
		this.originCol = originCol2;
		this.finalRow = finalRow2;
		this.finalCol = finalCol2;
	}
	
	/**
	 *Devuelve el numero del jugador
	 *@return player
	 */
	@Override
	public int getPlayerNumber() {
		// TODO Auto-generated method stub
		return player;
	}
	
	/**
	 *Aplica la jugada
	 *@param state Estado de origen
	 *@return next Estado resultado
	 */
	@Override
	public WolfAndSheepState applyTo(WolfAndSheepState state) {
		// TODO Auto-generated method stub
		if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }
		if (!state.isValid(this)) return state;
        // make move
        int[][] board = state.getBoard();
        board[finalRow][finalCol] = player;
        board[originRow][originCol] = -1;

        // update state
        WolfAndSheepState next;
        if (WolfAndSheepState.isWinner(board, state.getTurn())) {
            next = new WolfAndSheepState(state, board, true, state.getTurn());
        } else {
            next = new WolfAndSheepState(state, board, false, -1);
        }
        return next;
	}
	
	/**
	 * Metodo que devuelve un string para mostrar por pantalla un objeto de la clase
	 * @return string de un objeto la clase
	 */
    public String toString() {
        return "place " + player + " from (" + originRow + ", " + originCol + ")" + " at (" + finalRow + ", " + finalCol + ")";
    }
    
    public int getPlayer(){
    	return this.player;
    }

   public boolean equals(WolfAndSheepAction a){
    	if(a.player==this.player && a.originRow == this.originRow
    			&& a.originCol == this.originCol && a.finalRow == this.finalRow 
    			&& a.finalCol == this.finalCol) return true;
    	else return false;
    }
}
