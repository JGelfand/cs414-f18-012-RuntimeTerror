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
            if(destinationPiece == null || destinationPiece.getColor() != piece.getColor()) {
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

    public void move(String from, String to) throws IllegalMoveException{
        try {
            ChessPiece fromPiece = getPiece(from);
            if(fromPiece == null){
                throw new IllegalMoveException("The first argument must be the position of a piece.");
            }
            if(!fromPiece.legalMoves().contains(to)){
                throw new IllegalMoveException("Second argument must be a valid move.");
            }
            if(placePiece(fromPiece, to)){
                int[] fromIndexes = positionToIndexes(from);
                board[fromIndexes[0]][fromIndexes[1]] = null;
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
