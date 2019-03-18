package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.mvc.GameTable;
import javax.swing.BoxLayout;


public class GameWindow<S extends GameState<S, A>,
A extends GameAction<S, A>>
extends JFrame implements GameObserver<S, A>, GameViewController<S,A>{ // (*)

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int player;
	protected GamePlayer rand;
	protected ConcurrentAiPlayer smart;
	protected GameView<S, A> gameView;
	protected GameController<S, A> gameCtrl;
	protected GameTable<S,A> gameTable;
	protected RectBoardGameView<?, ?> tablero;
	protected Settings settings;
	protected ThinkingTool thinkingTool;//PRACTICA 6
	protected PlayerMode playerMode;
	private int curTurn;
	private boolean activeGame;
	private Thread smartThread;//PRACTICA 6
	protected S state;
	
	public GameWindow(){
	}
	public GameWindow(int playerId, GamePlayer randPlayer, ConcurrentAiPlayer smartPlayer,
	GameView<S, A> gameView, GameController<S, A> gameCtrl, GameTable<S,A> gameTable) {
		this.player = playerId;
		this.rand = randPlayer;
		this.smart = smartPlayer;
		this.gameView = gameView;
		this.gameCtrl = gameCtrl;
		this.gameTable = gameTable;
		this.playerMode = PlayerMode.MANUAL;
		this.curTurn = 0;
		this.activeGame = true;
		this.state = gameTable.getState();
		initGui();
		this.gameTable.addObserver(this);
	}

	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater( new Runnable() {	
			@Override
			public void run() {
				GameWindow.this.handleEvent(e);
			}
		});
	}
	
	public void initGui(){
		this.setTitle(this.gameTable.getState().getGameDescreption() + " - " + "Jugador" + " " + this.player);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.settings = new Settings(this);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add (this.settings);
		this.thinkingTool = new ThinkingTool(this);
		panel.add(this.thinkingTool);
		this.add(panel, BorderLayout.NORTH);
		this.add(this.gameView,BorderLayout.CENTER);
		this.setSize(1000, 800);
		this.setVisible(true);
		if(this.player == this.curTurn) {
			this.settings.enableMoveAndPause(true);
			this.gameView.enable();
		}
	}
	
	public void makeRandomMove() {
		// TODO Auto-generated method stub
		A a = this.rand.requestAction(this.gameTable.getState());
		this.gameCtrl.makeManualMove(a);
	}

	public void makeSmartMove() {
        if (this.player == this.curTurn && this.activeGame) {
            if (smartThread == null) {
                int nThreads = this.thinkingTool.getNThreads();
                int timeout = this.thinkingTool.getTime();
                S st = state;
                smartThread = new Thread(() -> {
                    SwingUtilities.invokeLater(() -> GameWindow.this.thinkingTool.setThinking(true));
                    long time0 = System.currentTimeMillis();
                    smart.setMaxThreads(nThreads);
                    smart.setTimeout(timeout);
                    A action = smart.requestAction(st);
                    long time1 = System.currentTimeMillis();
                    if (action != null) {
                        SwingUtilities.invokeLater(() -> {
                            GameWindow.this.thinkingTool.setThinking(false);
                            GameWindow.this.gameView.showInfoMessage(smart.getEvaluationCount() + " nodes in "
                                    + (time1-time0) + " ms. Value = " + String.format( "%.5f", smart.getValue()));
                            GameWindow.this.gameCtrl.makeManualMove(action);
                        });
                    }
                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            GameWindow.this.smartThread = null;
                        });
                    } catch (Exception e) {
                        // If it is interrupted, then smartThread will be set to null.
                    }
                });
                smartThread.start();
                //smartThread.interrupt();
            } else {
                this.gameView.showInfoMessage("I am still thinking! Stop me first!");
            }
        }
    }
	
	@Override
	public void cancelSmartMove() {
		if (this.smartThread != null) {
			this.smartThread.interrupt();
			this.gameView.showInfoMessage("SmartMove thinking is stopped!!");
			this.thinkingTool.setThinking(false);
		}
	}
	
	@Override
	public void restartGame() {
		// TODO Auto-generated method stub
		this.gameCtrl.run();
		this.activeGame = true;
	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub
		JFrame aviso = new JFrame("Exit");
		new JLabel();
		JLabel mensaje = new JLabel("The game will stop. Are you sure?",JLabel.CENTER);
		JButton yes = new JButton("YES");
		JButton no = new JButton("NO");
		no.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				aviso.dispose();
			}
			
		});
		yes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GameWindow.this.activeGame = false;
				try{
				GameWindow.this.gameCtrl.stop();
				}
				catch(GameError e1) {
					GameWindow.this.gameView.showInfoMessage(e1.getMessage());
				}
				finally{
					aviso.dispose();
				}
				//System.exit(0);
			}
			
		});
		JPanel panel = new JPanel();
		panel.add(yes);
		panel.add(no);
		aviso.add(mensaje, BorderLayout.NORTH);
		aviso.add(panel, BorderLayout.CENTER);
		aviso.setSize(300, 100);
		aviso.setVisible(true);		
	}
	
	@Override
	public void changePlayerMode(PlayerMode p) {
		// TODO Auto-generated method stub
		this.playerMode = p;
		if(this.curTurn == this.player) this.decideMakeAutomaicMove();
	}
	
	
	public void handleEvent(GameEvent<S, A> e) {
		// TODO Auto-generated method stub
		this.state = e.getState();
		this.curTurn = state.getTurn();
		this.activeGame = !state.isFinished() || e.getType() == EventType.Stop;
		switch(e.getType()){
		case Start:
			this.gameView.showInfoMessage(e.toString());
			this.gameView.update(state);
			this.startEvent();
			break;
		case Info:
			break;
		case Change:
			this.gameView.update(state);
			this.changeEvent(e);
			break;
		case Stop:
			this.stopEvent(e);
			break;
		case Error:
			this.gameView.showInfoMessage(e.toString());
			break;
		case Pause://OPCIONAL
			this.pauseEvent();
			this.gameView.showInfoMessage(e.toString());
			break;
		case Chat: //OPCIONAL
			this.gameView.sendMessage(e.toString());
			break;
		}
		if (this.gameTable.getState().isFinished() && !this.gameTable.getFinished()) {
			this.gameCtrl.stop();
		}
	}
	
	@Override
	public PlayerMode getPlayerMode() {
		// TODO Auto-generated method stub
		return this.playerMode;
	}
	@Override
	public int getPlayerId() {
		// TODO Auto-generated method stub
		return this.player;
	}
	@Override
	public GameTable<S, A> getModel() {
		// TODO Auto-generated method stub
		return this.gameTable;
	}
	
	private void decideMakeAutomaicMove() {
		if (this.player == this.curTurn && this.activeGame){
			if(this.playerMode == PlayerMode.RANDOM) {
				this.makeRandomMove();
			}
			else if (this.playerMode == PlayerMode.SMART){
				this.makeSmartMove();
			}
		}
	}
	
	@Override
	public void previousState() {
		// TODO Auto-generated method stub
		try{
			this.gameCtrl.returnPreviosState();
		}
		catch(GameError e){
			this.gameView.showInfoMessage("The game is paused");
		}
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		this.gameCtrl.pause();
	}
	
	//TRATAMIENTO DE LOS EVENTOS
	private void startEvent(){
		if(this.curTurn == this.player) {
			this.gameView.showInfoMessage("Your Turn");
			this.settings.enableMoveAndPause(true);
			this.gameView.enable();
		}
		else {
			this.gameView.showInfoMessage("Turn for player " + this.curTurn);
			this.settings.enableMoveAndPause(false);
			this.gameView.disable();
		}
		this.settings.enablePrevious(false);
	}
	
	private void pauseEvent() {
		if(this.gameTable.getPause()){
			this.settings.enableAll(false);
			this.gameView.disable();
		}
		else{
			this.settings.enableAll(true);
			if(this.curTurn != this.player) this.settings.enableMoveAndPause(false);
			else this.gameView.enable();
		}
	}
	
	private void stopEvent(GameEvent<S, A> e) {
		String endText = "The game ended: ";
		int winner = e.getState().getWinner();
		if (winner == -1) endText += "draw!";
		else {
			endText += "player " + winner + " has won";
		}
		this.gameView.showInfoMessage(endText);
		this.settings.enableMoveAndPause(false);
		this.settings.enablePrevious(false);
		this.gameView.disable();
	}
	
	private void changeEvent(GameEvent<S, A> e) {
		if (e.getAction() == null) this.gameView.showInfoMessage("The last move has been undone");
		if(curTurn == this.player && this.activeGame){
			this.gameView.showInfoMessage("Your Turn");
			this.settings.enableMoveAndPause(true);
			this.settings.enablePrevious(false);
			this.gameView.enable();
			if(this.playerMode == PlayerMode.MANUAL){
				this.gameView.showInfoMessage("Click on source cell");
			}
		}
		else if(curTurn != this.player && this.activeGame) {
			this.gameView.showInfoMessage("Turn for player " + this.curTurn);
			this.settings.enableMoveAndPause(false);
			if (e.getAction() != null) this.settings.enablePrevious(true);
			this.gameView.disable();
		}
		this.decideMakeAutomaicMove();
	}
}
