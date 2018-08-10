import java.util.ArrayList;

public class Player {
	private static int initDepth;
    private int move;
 
	/**
	*  initializes the move
    * calls maximizer function 
    * sets the initial values for alpha-beta pruning
    * returns the move that leads to the maximum evaluated state from the current 
    * in the given depth
    */
    public int play(State currentState,int depth) {
    	ArrayList<State> children = currentState.getChildren();
    	if(!children.isEmpty()) {
    		this.move = children.get(0).getLastMove();
    	}
        int beta = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        initDepth = depth;
        max(currentState,depth,alpha,beta);
        return this.move;
                    
    }
    /**
     * Minimax implementation for the maximizer
     */
    private int max(State currentState ,int depth ,int alpha ,int beta){
    	
        if((currentState.isTerminal() == 1) || (currentState.isTerminal() == 2) || (depth == 0))
        {
        	
           return currentState.evaluate();
        }
        
    	int v = Integer.MIN_VALUE;
    	ArrayList<State> children = new ArrayList<State>(currentState.getChildren());
    	for(State child : children) {
    		
    		
    		int m = min(child, depth - 1, alpha, beta);

    		if(m > v) {
    			v = m;
    			if(depth == initDepth) // CHANGE THE MOVE ONLY THE FIRST TIME THE MAX FUNCTION IS CALLED 
    				this.move = child.getLastMove(); // this is the best move
    			

    			
    		} 
    		
    		
    		if (v >= beta)  return v; // it doesn't matter what value we return
    		alpha = Math.max(alpha, v);
    		
    	}
    	return v;
    
    	
    	
    }
    /**
     * Minimax implementation for the minimizer
     */
    private int min(State currentState ,int depth ,int alpha ,int beta) {
        if((currentState.isTerminal() == 1) || (currentState.isTerminal() == 2) || (depth == 0))
        {
        	
           return currentState.evaluate();
        }
        
    	int v = Integer.MAX_VALUE;
    	ArrayList<State> children = new ArrayList<State>(currentState.getChildren());
    	for(State child : children) {
    		
    		// we don't care for the move, for which we get the minimum value 
    		v = Math.min(v, max(child, depth - 1,alpha, beta));
    		
    		if (v <= alpha)  return v; // it doesn't matter what value we return
    		beta = Math.min(beta, v);
    		
    	}
    	return v;
    

    }


}

