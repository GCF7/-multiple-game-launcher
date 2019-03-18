package es.ucm.fdi.tp.launcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessState;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.view.ChessView;
import es.ucm.fdi.tp.view.ConsoleController;
import es.ucm.fdi.tp.view.ConsoleView;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.GameView;
import es.ucm.fdi.tp.view.GameWindow;
import es.ucm.fdi.tp.view.TttView;
import es.ucm.fdi.tp.view.WasView;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class MainP6 {
	
	private static Scanner in = new Scanner(System.in).useDelimiter(System.getProperty("line.separator"));
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static GameTable<?, ?> createGame(String juego) {
		// create a game with a GameState depending on the value of gType
		GameState<?, ?> initialState;
		GameTable<?, ?> gameTable;
		if(juego.equalsIgnoreCase("ttt")) {
			initialState = new TttState(3);
			gameTable = new GameTable(initialState);
		}
		else if (juego.equalsIgnoreCase("was")){
			initialState = new WolfAndSheepState();
			gameTable = new GameTable(initialState);
		}
		else if (juego.equalsIgnoreCase("chess")){
			initialState = new ChessState();
			gameTable = new GameTable(initialState);
		}
		else return null;
		return gameTable;
	}
	/**
	 * Crea el estado inicial del juego
	 * @param juego
	 * @return initialState juego creado o null si no es valido
	 */
	public static GameState<?,?> createInitialState(String juego) {
		GameState<?, ?> initialState;
		if(juego.equalsIgnoreCase("ttt")) {
			System.out.println("Introduce dimension del tablero:");
			int dim = in.nextInt();
			initialState = new TttState(dim);
		}
		else if (juego.equalsIgnoreCase("was")) initialState = new WolfAndSheepState();
		else return null;
		return initialState;
	}
	
	/**
	 * Crea el jugador
	 * @param gameName nombre del juego
	 * @param playerType tipo de jugador
	 * @param playerName nombre del jugador
	 * @return jugador creado o null si no es valido
	 */
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName) {
		GamePlayer jugador;
		if(playerType.equalsIgnoreCase("manual")) jugador = new ConsolePlayer(playerName, in);
		else if (playerType.equalsIgnoreCase("rand"))jugador = new SmartPlayer(playerName, 5);
		else if (playerType.equalsIgnoreCase("smart")) jugador = new RandomPlayer(playerName);
		else return null;
		return jugador;
		
	}

	/**
	 * Main
	 * @param args
	 */
	private static <S extends GameState<S, A>, A extends GameAction<S,A>>
	void startConsoleMode(String gType, GameTable<S, A> game, String playerModes[]) {
	// create the lis of players as in assignemnt 4
	// ...
		List<GamePlayer> listaJugadores = new ArrayList<GamePlayer>();
		GamePlayer player;
		boolean noValido = false;
		for(int i = 0; i < playerModes.length && !noValido; i++){
			player = createPlayer(gType, playerModes[i], "Guille" + i);
			if(player == null) {
				System.out.println("Tipo de jugador incorrecto");
				noValido = true;
			}
			else listaJugadores.add(player);
		}
		if (!noValido){
			new ConsoleView<S,A>(game);
			new ConsoleController<S,A>(listaJugadores, game).run();
		}
	}
	
	 private static < S extends GameState<S,A>,
     A extends GameAction<S,A> > void startGuiMode(final String gType, final GameTable<S,A> game) {

			final GameController<S,A> gameCtrl = new GameController<S,A>(game);
			
			for (int i = 0; i < game.getState().getPlayerCount(); i++) {
			
				final GamePlayer p1;
				final ConcurrentAiPlayer p2;
				
				p1 = new RandomPlayer("Player - " + i);
				p2 = new ConcurrentAiPlayer("Player - " + i);
				p1.join(i);
				p2.join(i);
				
				final int j = i;
				
				try {
				
					SwingUtilities.invokeAndWait( new Runnable() {
					
					@Override
					public void run() {
						new GameWindow<S, A>(j, p1, p2, (GameView<S,A>)createGameView(gType), gameCtrl, game);
					}

					@SuppressWarnings("unchecked")
					private GameView<S, A> createGameView(String gType) {
						// TODO Auto-generated method stub
						GameView<?, ?> gameView;
						if(gType.equalsIgnoreCase("ttt")) {
							gameView = new TttView((TttState) game.getState(), (GameController<TttState, TttAction>) gameCtrl, j, game.getState().getPlayerCount());
						}
						else if (gType.equalsIgnoreCase("was")){
							gameView = new WasView((WolfAndSheepState) game.getState(), (GameController<WolfAndSheepState, WolfAndSheepAction>) gameCtrl, j, game.getState().getPlayerCount());
						}
						else if (gType.equalsIgnoreCase("chess")){
							gameView = new ChessView((ChessState) game.getState(), (GameController<ChessState, ChessAction>) gameCtrl, j, game.getState().getPlayerCount());
						}
						else return null;
						return (GameView<S, A>) gameView;
					}
					});
				
				} catch (InvocationTargetException | InterruptedException e) {
				
					System.err.println("Some error occurred while creating the view: " + e.getMessage());
					System.exit(1);
				
				}
			
			}
			
			SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				gameCtrl.run();
			}
			
			});
		
		}
	
	private static void usage() {
		// print usage of main
		System.out.println("Syntax: GameName [ttt/ was] GameMode [console/gui] player1 player2... [manual/rand/smart]");
	}
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			usage();
			System.exit(1);
		}
		GameTable<?, ?> game = createGame(args[0]);
		if (game == null) {
			System.err.println("Invalid game");
			usage();
			System.exit(1);
		}
		String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
		switch (args[1]) {
		 case "console":
			startConsoleMode(args[0], game, otherArgs);
			break;
		 case "gui":
			startGuiMode(args[0],game);
			break;
		 default:
			System.err.println("Invalid view mode: "+args[1]);
			usage();
			System.exit(1);
		}
	}
}
