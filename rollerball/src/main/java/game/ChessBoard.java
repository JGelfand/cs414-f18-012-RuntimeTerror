package game;

import java.util.ArrayList;
import java.util.Scanner;

public class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard(){
        board = new ChessPiece[7][];
        for(int i=0;i<7;i++){
            board[i] = new ChessPiece[7];
        }
    }

    public ChessPiece[][] getBoard()
    {
	return this.board;
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
	    kpos = getKingLocation(piece.getColor()); //position of the king doesnt change based on the move, so can use a simpler searcher
	}
	return kpos; 
    }

    public String getKingLocation(ChessPiece.Color color) //public for king circle
    {
	for (int k = 0; k < board.length; k++)
	{
	    for (int j = 0; j < board.length; j++)
	    {
		if (board[k][j] != null && board[k][j] instanceof King && board[k][j].getColor() == color)
		{
		    return board[k][j].getPosition();
		}
	    }
	}
	return "";
    }

    //returns the validity of the move if the moving king is in check, or the move puts them in check
    //this is assuming that piece is moving to position
    public boolean king_in_check(ChessPiece piece, String position)
    {
	try
	{
	    String kpos = getKingLocation(piece, position); //location of the king
	    String oldPos = piece.getPosition();
	    ChessPiece capPiece = getPiece(position);
            if(!placePiece(piece, position)) //need to update the board first
	    {

	        return false; //move wasnt legal for other reasons...
 	    }
	    int[] fromIndexes = positionToIndexes(oldPos);
            board[fromIndexes[0]][fromIndexes[1]] = null;
	    
	    boolean ret = false;
            if (king_in_check(piece.getColor(), kpos))
	    {
	        ret = true;
	    }
	    placePiece(piece, oldPos); //undo the update to not change the state of the board
	    fromIndexes = positionToIndexes(position);
            board[fromIndexes[0]][fromIndexes[1]] = capPiece; //put the possibly captured piece back 

	    return ret;
	}
	catch (IllegalPositionException e)
	{
	     return false;
	}
    }

    public boolean king_in_check(ChessPiece.Color color, String kpos)
    {
	for (int k = 0; k < board.length; k++)
	{
	    for (int j = 0; j < board[k].length; j++) //going over the whole board to find all of the enemies pieces
	    {
		ChessPiece checker = board[k][j]; //possible enemy piece
		if (checker != null && checker.getColor() != color) //checker is an enemy piece
		{
		    ArrayList<String> checkerMoves = checker.legalMoves(); 
		    for (String move : checkerMoves) //go through their legal moves to see if they can capture the king
		    {
			if (move.equals(kpos)) //if they can, the king is in check and the move is illegal. 
			{
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
	if(game_is_stalemate(piece)) //opposing team cant move
	{
	    ChessPiece.Color losing_color = piece.getColor() == ChessPiece.Color.WHITE ? ChessPiece.Color.BLACK : ChessPiece.Color.WHITE;
	    if (king_in_check(losing_color, getKingLocation(losing_color)))
	    {
		return true; //no legal moves, and king is in check, therefore checkmate
	    }
	}
	return false;
    }

    private boolean game_is_draw(ChessPiece piece)
    {
	return game_is_stalemate(piece); //for now, lets only support stalemate
	//if we want we could check for three fold repitition, but that sounds difficult
    }

    private boolean game_is_stalemate(ChessPiece piece)
    {
	for (int k = 0; k < board.length; k++)
	{
	    for (int j = 0; j < board[k].length; j++)
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
	return true; //no moves were found for the next turn, therefore it is stalemate. 
    }

    public void handlePromotion(ChessPiece mover, String to, String promoteTo) throws IllegalPositionException
    {
	if (mover instanceof Pawn)
	{
       	    int[] indexes = positionToIndexes(to);
	    if (to.equals("e6") || to.equals("e7"))
	    {
		 if (mover.getColor() == ChessPiece.Color.WHITE)
		 {
		    //promotion occurs
		    if (promoteTo.equals("R") || promoteTo.equals("r")) //promote to rook
		    {
			ChessPiece piece = new Rook(this, ChessPiece.Color.WHITE);
			piece.setPosition(to);
 	              	board[indexes[0]][indexes[1]] = piece;
		    }
		    else
		    {
			ChessPiece piece = new Bishop(this, ChessPiece.Color.WHITE);
			piece.setPosition(to);
			board[indexes[0]][indexes[1]] = piece;
		    }
                }
	    }
	    else if (to.equals("c1") || to.equals("c2"))
	    {
	 	if (mover.getColor() == ChessPiece.Color.BLACK)
		{
		    //promotion occurs
		    if (promoteTo.equals("R") || promoteTo.equals("r"))
		    {
			ChessPiece piece = new Rook(this, ChessPiece.Color.BLACK);
			piece.setPosition(to);
			board[indexes[0]][indexes[1]] = piece;
		    }
		    else
		    {
			ChessPiece piece = new Bishop(this, ChessPiece.Color.BLACK);
			piece.setPosition(to);
			board[indexes[0]][indexes[1]] = piece;
		    }
		}
	    }
	}
    }

    public ChessPiece move(String from, String to) throws IllegalMoveException {
	return this.move(from, to, "");
    }

    public ChessPiece move(String from, String to, String promotion) throws IllegalMoveException{
        try {
            ChessPiece fromPiece = getPiece(from);
            if(fromPiece == null){
                throw new IllegalMoveException("The first argument must be the position of a piece.");
            }
            if(!fromPiece.legalMoves().contains(to)){
                throw new IllegalMoveException("Second argument must be a valid move.");
            }
	    if (king_in_check(fromPiece, to))
	    {
		throw new IllegalMoveException("Your king is in check!");
	    }
            if(placePiece(fromPiece, to)){
                int[] fromIndexes = positionToIndexes(from);
                board[fromIndexes[0]][fromIndexes[1]] = null; //move has been made
		handlePromotion(fromPiece, to, promotion);
		return fromPiece;	
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
	boolean turn  = true;
    	Scanner input = new Scanner(System.in);
	while(true){
		System.out.println(board.toString());
		String[] moves = input.nextLine().split("\\s+");
		if(moves.length != 2)
			continue;
		try{
			board.move(moves[0], moves[1]);
		}catch(IllegalMoveException e){
			continue;
		}
	}
    }

    public byte[] serializeToBytes(){
        byte[] piecePositions = new byte[16];
        //a1 is 1, b1 is 2, a2 is 8, etc (0 is unset/dead)
        //for pawns: 0 is no promotion, 1 is bishop, 2 is rook
        //create int array indexed by piece and color.
        //0: king, 1: bishop, 2,3: rook 4: pawn1 promote, 5:pawn1, 6:pawn2 promote, 7:pawn2
        //black is white+8
        try {
            for (int i = 0; i < board.length; i++)
                for (int j = 0; j < board[i].length; j++) {
                    ChessPiece piece = getPiece(((char) ('a' + j)) + "" + ((char) ('1' + i)));
                    if(piece == null)
                        continue;
                    int color = piece.getColor() == ChessPiece.Color.WHITE?0:8;
                    byte position = (byte)(i*7+j+1);
                    if(piece instanceof King){
                        piecePositions[color] = position;
                    }else if(piece instanceof Bishop){
                        //if no bishop seen yet, use dedicated bishop space. if seen, assume promotion and overflow into pawns
                        if(piecePositions[1+color] == 0)
                            piecePositions[1+color] = position;
                        else if(piecePositions[5+color] == 0){
                            piecePositions[4+color] = 1;
                            piecePositions[5+color] = position;
                        }
                        else{
                            piecePositions[6+color] = 1;
                            piecePositions[7+color] = position;
                        }
                    }
                    else if(piece instanceof Rook){
                        //if no rook seen yet, use dedicated rook spaces. if seen, assume promotion and overflow into pawns
                        if(piecePositions[2+color] == 0)
                            piecePositions[2+color] = position;
                        else if(piecePositions[3+color] == 0)
                            piecePositions[3+color] = position;
                        else if(piecePositions[5+color] == 0){
                            piecePositions[4+color] = 2;
                            piecePositions[5+color] = position;
                        }
                        else{
                            piecePositions[6+color] = 2;
                            piecePositions[7+color] = position;
                        }
                    }
                    else if(piece instanceof Pawn){
                        if(piecePositions[5+color] == 0)
                            piecePositions[5+color] = position;
                        else
                            piecePositions[7+color] = position;
                    }
                }
        }catch (IllegalPositionException e){
            //should never happen if I do this right
            e.printStackTrace();
        }
        return piecePositions;
    }

    public ChessBoard(byte[] bytes){
        this();
        for(int team=0;team <=8;team+=8) {
            ChessPiece.Color color = team==0? ChessPiece.Color.WHITE: ChessPiece.Color.BLACK;
            for (int column = 0; column < 8; column++) {
                ChessPiece piece = null;
                byte value=bytes[team+column];
                switch (column){
                    case 0: if(value != 0) piece = new King(this, color); break;
                    case 1: if(value != 0) piece = new Bishop(this, color); break;
                    case 2: case 3: if(value != 0) piece = new Rook(this, color); break;
                    case 4: case 6: continue;
                    case 5: case 7: if(value !=0) switch(bytes[team+column -1]){
                        case 0:piece = new Pawn(this, color); break;
                        case 1:piece = new Bishop(this, color); break;
                        case 2:piece = new Rook(this, color); break;
                    } break;
                }
                if(piece != null){
                    this.placePiece(piece, ((char)('a'+(value-1)%7)+""+((char)('1'+(value-1)/7))));
                }
            }
        }
    }
}
