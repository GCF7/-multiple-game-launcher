package es.ucm.fdi.tp.mvc;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import es.ucm.fdi.tp.mvc.GameObserver;
/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

    // define fields here
	protected S currentState;
	protected S initialState;
	protected S previousState;
	protected List<GameObserver<S,A>> obs;
	protected boolean finished;
	protected boolean started;
	protected boolean pause; //OPCIONAL

    public GameTable(S initState) {
        // add code here
    	this.initialState = initState;
    	this.currentState = this.initialState;
    	this.previousState = this.initialState;
    	this.finished = false;
    	this.started = false;
    	this.pause = false;
    	this.obs = new ArrayList<GameObserver<S,A>>();
    }
    public void start() {
        // add code here
    	this.currentState = this.initialState;
    	this.previousState = this.initialState;
    	this.started = true;
    	this.finished = false;
    	notifyObservers(new GameEvent<S,A> (EventType.Start, null, this.currentState, null, "The game has started"));
    }
    public void stop() {
        // add code here
    	if (finished){
    		GameError error =new GameError("The game was finished");
    		notifyObservers(new GameEvent<S,A>(EventType.Error, null, this.currentState, error, "The game was finished"));
    		throw error;
    	}
    	else {
    		this.finished = true;
    		notifyObservers(new GameEvent<S,A>(EventType.Stop, null, this.currentState, null, "The game is end"));
    	}   	
    }
    public void execute(A action) {
        // add code here
    	if(this.started && !this.currentState.isFinished() && action.getPlayerNumber() == this.currentState.getTurn() && !this.pause) {
    		this.previousState = this.currentState;
    		this.currentState = action.applyTo(this.currentState);
    		if (this.currentState == this.previousState) {
    			GameError error =new GameError("The game can not execute action");
	    		notifyObservers(new GameEvent<S,A> (EventType.Error, action, this.currentState, error, "The game can not execute action"));
    		}
    		else notifyObservers(new GameEvent<S,A> (EventType.Change, action, this.currentState, null, "The game state has change"));
    	}
    	else {
    		if (this.pause){
    			GameError error =new GameError("The game is pause");
	    		notifyObservers(new GameEvent<S,A> (EventType.Error, action, this.currentState, error, "The game is pause"));
    		}
    		else if (action.getPlayerNumber() != this.currentState.getTurn()) {
    			GameError error =new GameError("It's not your turn.");
        		notifyObservers(new GameEvent<S,A> (EventType.Error, action, this.currentState, error, "It's not your turn."));
    		}
    		else {
	    		GameError error =new GameError("The game can not execute action");
	    		notifyObservers(new GameEvent<S,A> (EventType.Error, action, this.currentState, error, "The game can not execute action"));
    		}
    	}
    	
    }
    
// PRINCIPIO PARTE OPCIONAL
    public void returnPreviosState () {
    	if(!this.pause){
	    	this.currentState = this.previousState;
	    	notifyObservers(new GameEvent<S,A> (EventType.Change, null, this.currentState, null, "The game state has change"));
    	}
    	else {
    		GameError error =new GameError("The game is pause");
    		notifyObservers(new GameEvent<S,A> (EventType.Error, null, this.currentState, error, "The game is pause"));
    	}
    }
    
    public void pause() {
    	if (this.pause) {
    		this.pause = false;
    		notifyObservers(new GameEvent<S,A> (EventType.Pause, null, this.currentState, null, "The game has resumed"));
    	}
    	else {
    		this.pause = true;
    		notifyObservers(new GameEvent<S,A> (EventType.Pause, null, this.currentState, null, "The game has paused"));
    	}
    	
    }
    
    public void chat(String msg) {
    	notifyObservers(new GameEvent<S,A> (EventType.Chat, null, this.currentState, null, msg));
    }
    
    //FIN PARTE OPCIONAL
    public S getState() {
        // add code here
    	return this.currentState;
    }
    
    public S getPreviousState() {
        // add code here
    	return this.previousState;
    }
    
    public boolean getPause() {
    	return this.pause;
    }

    public void addObserver(GameObserver<S, A> o) {
        // add code here
    	obs.add(o);
    }
    public void removeObserver(GameObserver<S, A> o) {
        // add code here
    	obs.remove(o);
    }
    
    protected void notifyObservers(GameEvent <S,A> e) {
    	for(GameObserver <S,A> o:obs) {
    		o.notifyEvent(e);
    	}
    }
    
    public boolean getFinished(){
    	return this.finished;
    }
}
