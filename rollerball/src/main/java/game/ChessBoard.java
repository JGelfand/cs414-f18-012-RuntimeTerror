package game;

public class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard(){
        board = new ChessPiece[7][];
        for(int i=0;i<7;i++){
            board[i] = new ChessPiece[7];
        }
    }

    public void initialize(){
        placePiece(new Rook(this, ChessPiece.Color.WHITE), "e1");
        placePiece(new Rook(this, ChessPiece.Color.WHITE), "e2");
        placePiece(new Rook(this, ChessPiece.Color.BLACK), "c6");
        placePiece(new Rook(this, ChessPiece.Color.BLACK), "c7");
        placePiece(new Bishop(this, ChessPiece.Color.WHITE), "d1");
        placePiece(new Bishop(this, ChessPiece.Color.BLACK), "d7");
        placePiece(new King(this, ChessPiece.Color.WHITE), "d2");
        placePiece(new King(this, ChessPiece.Color.BLACK), "d6");
        placePiece(new Pawn(this, ChessPiece.Color.WHITE), "c1");
        placePiece(new Pawn(this, ChessPiece.Color.WHITE), "c2");
        placePiece(new Pawn(this, ChessPiece.Color.BLACK), "e6");
        placePiece(new Pawn(this, ChessPiece.Color.BLACK), "e7");

    }

    public ChessPiece getPiece(String position) throws IllegalPositionException{
        int[] indexes = positionToIndexes(position);
        return board[indexes[0]][indexes[1]];
    }

    private int[] positionToIndexes(String position) throws IllegalPositionException{
        if(position == null)
            throw new IllegalPositionException("Position cannot be null");
        if(!position.matches("\\A[a-g][1-7]\\z"))
            throw new IllegalPositionException("Invalid position: "+position+".");
        int[] indexes = new int[]{0,0};
        indexes[1] = position.charAt(0)-'a';
        indexes[0] = position.charAt(1)-'1';
        return indexes;
    }

    public boolean placePiece(ChessPiece piece, String position){
        if(piece == null)
            return false;
        try{
            ChessPiece destinationPiece = getPiece(position);
            if(destinationPiece == null || destinationPiece.getColor() != piece.getColor()) { //if destination is empty or an opponents piece
                piece.setPosition(position);
                int[] indexes = positionToIndexes(position);
                board[indexes[0]][indexes[1]] = piece;
                return true;
            }
            return false;
        } catch (IllegalPositionException e) {
            return false;
        }
    }

    //returns the position of the king of the same color of the moving piece
    //this is assuming that piece is moving to position
    private String getKingLocation(ChessPiece piece, String position)
    {
	String kpos = "";
	if (piece instanceof King)
	{
	    kpos = position;
	}
	else
	{
	    for (int k = 0; k < board.length; k++)
	    {
	        for (int j = 0; j < board[k].lenght; j++)
	        {
		    if (board[k][j] != null && board[k][j].getColor() == piece.getColor() && board[k][j] instanceof King)
		    {
			kpos = board[k][j].getPosition();
		    }
	       }
	   }
	}
    }

    //returns the validity of the move if the moving king is in check, or the move puts them in check
    //this is assuming that piece is moving to position
    private boolean king_in_check(ChessPiece piece, String position)
    {
	String kpos = getKingLocation(piece, position); //location of the king
	String oldPos = piece.getPosition();
        if(!place_piece(piece, position)) //need to update the board first
	{
	    return false; //move wasnt legal for other reasons...
	}
        for (int k = 0; k < board.length; k++)
	{
	    for (int j = 0; j < board[k].lenght; j++) //going over the whole board to find all of the enemies pieces
	    {
		ChessPiece checker = board[k][j]; //possible enemy piece
		if (checker != null && checker.getColor() != piece.getColor()) //checker is an enemy piece
		{
		    ArrayList<String> checkerMoves = checker.legalMoves(); 
		    for (String move : checkerMoves) //go through their legal moves to see if they can capture the king
		    {
			if (move == kpos) //if they can, the king is in check and the move is illegal. 
			{
			    place_piece(piece, oldPos); //need to undo the checking of that move.
			    //If there is some way to create a new board with the move, and check that board, that would be best
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    //returns true if the game is over (win or draw), false otherwise
    //piece is the moving piece (so the winning piece)
    private boolean game_is_over(ChessPiece piece)
    {
	if (game_is_won(piece))
	{
	    return true;
	}
	else if (game_is_draw(piece))
	{
	    return true;
	}
	return false; //the game can never end Chell...
    }

    //returns true if the player opposite piece is in checkmate, false otherwise
    private boolean game_is_won(ChessPiece piece)
    {
	for (int k = 0; k < board.length; k++)
	{
	    for (int j = 0; j < board[k].lenght; j++)
	    {
		ChessPiece mover = board[k][j];
		if (mover != null && mover.getColor() != piece.getColor()) //mover is on the possibly losing team
		{
		    ArrayList<String> moves = mover.legalMoves();
		    for (String move : moves)
		    {
			if (!king_in_check(mover, move))
			{
			    return false; //the losing team has at least this move
			}
		    }
		}
	    }
	}
	return true; //no moves were found that gets the losing team out of check, therefore it is checkmate
    }

    private boolean game_is_draw(ChessPiece piece)
    {
	return false; //for now, lets not support checking for draws
	//if we want we have to check for at least stalemate and three-fold repitition.
    }

    public void move(String from, String to) throws IllegalMoveException{
        try {
            ChessPiece fromPiece = getPiece(from);
            if(fromPiece == null){
                throw new IllegalMoveException("The first argument must be the position of a piece.");
            }
            if(!fromPiece.legalMoves().contains(to)){
                throw new IllegalMoveException("Second argument must be a valid move.");
            }
	    if (king_in_check(piece, position))
	    {
		throw new IllegalMoveException("Your king is in check!");
	    }
            if(placePiece(fromPiece, to)){
                int[] fromIndexes = positionToIndexes(from);
                board[fromIndexes[0]][fromIndexes[1]] = null; //move has been made
		if(game_is_over(piece))
		{
		    //how do we want to handle this?
		    //personally I want to change the return type and handle it above
		    //perhaps to a boolean true if the game is over, false otherwise
		}
            }
            else {
                throw new IllegalMoveException("Invalid move.");
            }
        }catch (IllegalPositionException e){
            throw new IllegalMoveException(e);
        }
    }

    public String toString(){
        String chess="";
        String upperLeft = "\u250C";
        String upperRight = "\u2510";
        String horizontalLine = "\u2500";
        String horizontal3 = horizontalLine + "\u3000" + horizontalLine;
        String verticalLine = "\u2502";
        String upperT = "\u252C";
        String bottomLeft = "\u2514";
        String bottomRight = "\u2518";
        String bottomT = "\u2534";
        String plus = "\u253C";
        String leftT = "\u251C";
        String rightT = "\u2524";
        String topLine = upperLeft;
        for (int i = 0; i<6; i++){
            topLine += horizontal3 + upperT;
        }
        topLine += horizontal3 + upperRight;
        String bottomLine = bottomLeft;
        for (int i = 0; i<6; i++){
            bottomLine += horizontal3 + bottomT;
        }
        bottomLine += horizontal3 + bottomRight;
        chess+=topLine + "\n";
        for (int row = 6; row >=0; row--){
            String midLine = "";
            for (int col = 0; col < 7; col++){
                if(board[row][col]==null) {
                    midLine += verticalLine + " \u3000 ";
                } else {midLine += verticalLine + " "+board[row][col]+" ";}
            }
            midLine += verticalLine;
            String midLine2 = leftT;
            for (int i = 0; i<6; i++){
                midLine2 += horizontal3 + plus;
            }
            midLine2 += horizontal3 + rightT;
            chess+=midLine+ "\n";
            if(row>=1)
                chess+=midLine2+ "\n";
        }
        chess+=bottomLine;
        return chess;
    }

    public static void main(String[] args) throws IllegalMoveException {
        ChessBoard board = new ChessBoard();
        board.initialize();
        System.out.println(board);
    }
}
