package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    private Bishop bishop;
    private ChessBoard board;

    @BeforeEach
    void setupBishop(){
        board = new ChessBoard();
        bishop = new Bishop(board, ChessPiece.Color.WHITE);
    }
    @Test
    void maximumMovementCases() {
        board.placePiece(bishop, "a4");

        HashSet<String> validMoves = new HashSet<>();
        for(int i=1;i<=3;i++){
            validMoves.add((char)('a'+i)+""+(char)('4'+i));
            validMoves.add((char)('d'+i)+""+(char)('7'-i));
        }
        validMoves.add("b3");
        HashSet<String> actualMoves = new HashSet<>(bishop.legalMoves());
        assertEquals(validMoves, actualMoves, "Maximal case 1 incorrect");

        assertDoesNotThrow(()->board.move("a4", "b5"));
        validMoves.removeAll(Arrays.asList("b5", "b3"));
        validMoves.addAll(Arrays.asList("a4", "a6", "b7"));
        actualMoves.clear();
        actualMoves.addAll(bishop.legalMoves());
        assertEquals(validMoves, actualMoves, "Maximal case 2 incorrect");
    }

    @Test
    public void noJumpingFriendlies(){
        board.placePiece(bishop, "e7");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "d6");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "g5");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("f6"));
        assertEquals(validMoves, new HashSet<>(bishop.legalMoves()), "Movement should be blocked by friendly pieces");
    }

    @Test
    public void moveIntoEnemies(){
        board.placePiece(bishop, "d1");
        board.placePiece(new Pawn(board, ChessPiece.Color.BLACK), "b5");
        board.placePiece(new Pawn(board, ChessPiece.Color.BLACK), "e2");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("c2", "b3", "a4", "b5", "e2"));
        assertEquals(validMoves, new HashSet<>(bishop.legalMoves()));
    }

    @Test
    public void noReturnNull(){
        board.placePiece(bishop, "a7");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "b6");
        assertEquals(new ArrayList<String>(), bishop.legalMoves(), "No moves should return empty list");
    }

    @Test
    public void testToString(){
        assertEquals(bishop.toString(), "\u2657", "Bishop::toString() is bad");
        assertEquals(new Bishop(board, ChessPiece.Color.BLACK).toString(), "\u265D","Bishop::toString() is bad");
    }
}