import java.util.ArrayList;


public class State {
    
    public int value;
    private int height;
    private int length;
    boolean playerTurn;
    private int[][] table;
    private int[] count;
    private int lastMove;
    private ArrayList<Integer> possMoves;

    
    /**
     * constructor that creates a state which is a copy of the parameter state
     * @param state the state to copy
     */
    public State(State state) {
    	this.value  = state.value;
    	this.height = state.height;
    	this.length = state.length;
    	this.playerTurn = state.playerTurn;
    	this.table = state.getCopyTable();
    	this.setCount(state.count); 
    }
    /**
     * initializes the table
     * @param height
     * @param length
     * @param firstPlayer
     */
    public State(int height, int length, boolean firstPlayer) {
        this.playerTurn = firstPlayer;
        this.height = height;
        this.length = length;
        table = new int[height][length];
        count = new int[length];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                this.table[i][j] = 0;
            }
        }

        for (int i = 0; i < length; i++) {
            count[i] = 0;
        }

        this.initPossMoves();
    }

    /**
     * creates a new state, after a move has been played
     * @param previous
     * @param c
     */
    public State(State previous, int c) {


        this.setHeight(previous.height);

        this.setLength(previous.length);


        this.setTable(previous.getCopyTable());
        this.setCount(previous.count);

        this.playerTurn = !previous.playerTurn;
        this.table[height-1-count[c-1]][c-1] = previous.playerTurn ? 1:2;
        this.count[c-1]++;
        this.lastMove = c;

        this.initPossMoves();
    }

    /**
     * @param col
     * @return the new state after a move is played
     */
    public State move(int col) {
        if (this.count[col-1]<height) {
            return new State(this, col);
        }
        else
            return null;

    }


    /**
     * prints the current table
     */
    public void print() {
    	String s;
        for (int i = 0; i < this.height; i++) {
            System.out.println();
            for (int j = 0; j < this.length; j++) {
            	if(this.table[i][j] == 1) {
            		s = "X";
            	} else if(this.table[i][j] == 2) {
            		s = "O";
            	} else 
            	{
            		s = " ";
            	}
                System.out.print("|" + s + "|");
            }
        }

    }
    /**
     * initializes the list with the count of possible moves
     */
    private void initPossMoves() {
        this.possMoves = new ArrayList<Integer>();
        for(int i = 0; i < length; i++) {
            if(this.count[i] < height)
                this.possMoves.add(i + 1);
        }
    }

    /**
     * @return a list with the children of the current state
     */
    public ArrayList<State> getChildren(){


        ArrayList<State> childrenStates = new ArrayList<>();

        if(this.isTerminal()==1 || this.isTerminal()==2){
            return childrenStates;
        }


        for(int i=0;i<this.length;i++){
            for(int j = 0;j < this.height;j++){
                if(this.table[j][i]==0){
                    childrenStates.add(move(i + 1));
                    break;
                }
            }
        }

        return childrenStates;
    }

    // getters and setters
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    /**
    * @return true if pc is playing and false if human is playing
    */
    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int[][] getTable() {
        return this.table;
    }
    /**
     * @return the column of the last move
     */
    public int getLastMove() {
        return this.lastMove;
    }
    /**
     * @return a copy of the table of the current state
     */
    public int[][] getCopyTable() {
        int[][] array = new int[this.height][this.length];
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.length; j++) {
                array[i][j] = this.table[i][j];
            }
        }
        return array;
    }

    public void setTable(int[][] table) {
        this.table = table;
    }

    public int[] getCount() {
        return count;
    }

    public void setCount(int[] count) {
        this.count = new int[count.length];
        for(int i = 0; i < count.length; i++) {
            this.count[i] = count[i];
        }

    }
    // ---------------------------------------------------------- heuristic function  
    
    public int evaluate() {
    	// if the state is terminal
    	if(this.isTerminal() == 1) { // if max(computer) wins
    		return 10000;
    	} else if(this.isTerminal() == 2) { // if min(human) wins
    		return -10000;
    	}
    	
    	// if not terminal return the sum of the Threats
    	return sumThreats();
    	
    	
    }
    /**
     * sum the results from isThreat for each column
     * @return
     */
    private int sumThreats() {
    	int sum = 0;
    
    		for(int i = 0; i < this.length; i++) {
    			
    			sum += isThreat(i + 1);
    		}
    		
    		
    	return sum;
    
    	
    }
    
    /**
     * 
     * @param column the column to check for threat
     * @return 1, -1, 0, 100, -100
     */
    private int isThreat(int column) {
    	State tmp = new State(this); // copy of the current state
   
    	State next = tmp.move(column);
    	// if the column is full 
    	if(next == null) {
    		return 0;
    	} 
    	
    	int term = next.isTerminal();
    	if(term == 1) { // if the player in turn is one move away from completing four in a row
    		           // return +-100, for computer or human accordingly
    		return 100;
    	} else if(term == 2) {
    		return -100;
    	}
    	
    	tmp.setPlayerTurn(!tmp.playerTurn); // change player turn
    	next = tmp.move(column);
    	term = next.isTerminal();
    	// if a player is one move away from completing four in a row but it's not his turn 
    	// return +- 1
    	if(term == 1) {
    		return 1;
    	} else if(term == 2) {
    		return -1;
    	}
    	
    	
    	return 0;
    	
    	
    	
    }
    
//----------------------------------------------------------

    /** 
     * return true if the game is a draw
     */
    public boolean isDraw() {

        return 	(this.isTerminal()==0) && this.possMoves.isEmpty();
    }

    /** 
     * checks if the state is terminal
     * 
     * @return 1 if the computer has won 
     * 2 the the human has won
     * 0 if neither one has won 
     */
    public int isTerminal() {
        if(checkHorizontal() != 0) {
            return checkHorizontal();
        }

        if(checkVertical() != 0) {
            return checkVertical();
        }

        if(checkDiagonal() != 0) {

            return checkDiagonal();
        }



        return 0;
    }

    /**
     * checks if the horizontal winning condition is fulfilled
     * 
     * @return the winner's number if the horizontal condition is fulfilled
     * 0 otherwise
     */
    private int checkHorizontal() {

        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.length - 3; j++) { // should be greater than 3

                if(this.table[i][j] != 0 && this.table[i][j] == this.table[i][j + 1]) { // two same chars, that aren't null
                    if(this.table[i][j + 1] == this.table[i][j + 2]) { // three same chars
                        if(this.table[i][j + 2] == this.table[i][j + 3]) { // four same chars

                            return this.table[i][j];

                        }
                    }
                }
            }
        }
        return 0;
    }
    /**
     * checks if the vertical winning condition is fulfilled
     * 
     * @return the winner's number if the vertical condition is fulfilled
     * 0 otherwise
     */
    private int checkVertical() {

        for(int i = 0; i < this.length; i++) {
            for(int j = 0; j < this.height - 3; j++) { // should be greater than 3
                if(this.table[j][i] != 0 && this.table[j][i] ==  this.table[j + 1][i]) { // two same chars
                    if(this.table[j + 1][i] == this.table[j + 2][i]) { // three same chars
                        if(this.table[j + 2][i] == this.table[j + 3][i]) { // four same chars

                            return this.table[j][i];

                        }
                    }
                }
            }
        }
        return 0;
    }
    /**
     * checks if the diagonal winning condition is fulfilled
     * either from up left to down right or from up right to down left
     * @return the winner's number if the diagonal winning condition is fulfilled
     * 0 otherwise
     */
    private int checkDiagonal() {

        // this first double loop check for four same chars in a diagonal from up left to down right
        for(int i = 0; i < this.height - 3; i++) {
            for(int j = 0; j < this.length - 3; j++) { // should be greater than 3
                if(this.table[i][j] != 0 && this.table[i][j] == this.table[i + 1][j + 1]) { // two same chars
                    if(this.table[i + 1][j + 1] == this.table[i + 2][j + 2]) { // three same chars
                        if(this.table[i + 2][j + 2] == this.table[i + 3][j + 3]) { // four same chars

                            return this.table[i][j];

                        }
                    }
                }
            }
        }
        // -----------------------
        // this second double loop checks for four same chars in a diagonal from up right to down left
        for(int i = length - 1; i > 2; i--) { // begins from the up right element
            for(int j = 0; j < this.height - 3; j++) { // should be greater than 3

                if(this.table[j][i] != 0 && this.table[j][i] == this.table[j + 1][i - 1]) { // two same chars

                    if(this.table[j + 1][i - 1] == this.table[j + 2][i - 2]) { // three same chars

                        if(this.table[j + 2][i - 2] == this.table[j + 3][i - 3]) { // four same chars

                            return this.table[j][i];

                        }
                    }
                }
            }
        }

        return 0;
    }


}

