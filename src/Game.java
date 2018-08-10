import java.util.Scanner;

/** class representation of a game  */
public class Game {
	/**   current State  */ 
    private State curState; // current state

    private Player computer;

    private Scanner input;
    /** height of the board */
    private final static int height = 6;
    /** length of the board */
    private final static int length = 7;
    /** depth for minimax */
    private int depth;

    /**
     * creates a new game 
     * asks the depth that will be used in minimax and who is playing first
     * (computer or human)
     * ends when the game is in terminal position or draw 
     */
    public Game() {
    	// ask depth
        input = new Scanner(System.in);
        System.out.println("Please choose depth: ");
        this.depth = input.nextInt();
        
        // choose who is playing
        boolean turn = askTurn();
        this.curState = new State(height, length, turn);
        this.computer = new Player();
        
        int nextMove;

        this.curState.print();
        System.out.println("\n");
        
        State tmp;
        while(this.curState.isTerminal() == 0) {
            // check if draw
            if(this.curState.isDraw()) {
                System.out.println("Draw");
                break;
            }
            
            // asks for move
            if(this.curState.playerTurn) {
                nextMove = this.computer.play(this.curState,depth); // computer
            } else {
                nextMove = askMove(); // human
            }

            System.out.println("Move played: " + nextMove);
            tmp = this.curState.move(nextMove);

            while(tmp == null) { // if invalid move
                System.out.println("Invalid move");

                if(this.curState.playerTurn) { // not reachable because the computer plays only valid moves
                    nextMove = this.computer.play(this.curState,depth); // computer
                } else {
                    nextMove = askMove(); // human
                }
                tmp = this.curState.move(nextMove);
                System.out.println("Move played: " + nextMove);
            }

            this.curState = curState.move(nextMove);

            // print state
            this.curState.print();
            //System.out.println("State evaluation: " + this.curState.evaluate());
       
            
            System.out.println("\n");


        }
        // if terminal position 
        if(this.curState.isTerminal() == 1)
            System.out.println("Computer wins!");
        else if(this.curState.isTerminal() == 2)
            System.out.println("Human wins!");

        input.close();

    }

    /**
     * asks the user who is playing
     * @return true if computer is playing
     */
    private boolean askTurn() {

        System.out.println("Computer's turn: 1 \nHuman's turn: 2");
      
        int turn = input.nextInt();

        return turn == 1;

    }
    /**
     * asks human for move 
     * @return the move played
     */
    private int askMove() {
        System.out.println("Move(1-7): ");
    
        int move = input.nextInt();
        
        while(move>7 || move<1){
            System.out.println("Out of bounds\nMove(1-7): ");
            move = input.nextInt();
        }
        return move;
    }



}
