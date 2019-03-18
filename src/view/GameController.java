package es.ucm.fdi.tp.view;


import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;

public class GameController<S extends GameState<S,A>, A extends GameAction<S,A>> implements Runnable {
	// add fields here
		private GameTable<S,A> gameTable;
		private boolean stop;
		
		public GameController(GameTable<S,A> game) {
				this.gameTable = game;
				this.stop = false;
		}
		
		public void run() {
		// add code here
			gameTable.start();
			this.stop = false;
		}

		public void makeManualMove(A a) {
			// TODO Auto-generated method stub
			if (!this.stop) this.gameTable.execute(a);
		}
		
		public void stop(){
			this.stop = true;
			this.gameTable.stop();
		}
		
		public void returnPreviosState() {
			this.gameTable.returnPreviosState();
		}
		
		public void pause() {
			this.gameTable.pause();
		}
		
		public void sendMessage(String msg) {
			this.gameTable.chat(msg);
		}
}

