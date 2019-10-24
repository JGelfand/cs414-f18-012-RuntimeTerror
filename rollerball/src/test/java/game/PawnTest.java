package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    private ChessBoard board;

    @BeforeEach
    public void setup(){
        board = new ChessBoard();
    }

    @Test
    void legalMoves() {
        Pawn pawnOne = new Pawn(board, ChessPiece.Color.WHITE);
        board.placePiece(pawnOne, "a2");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "b3");

        HashSet<String> validMoves = new HashSet<>(Arrays.asList("a3", "a4"));
        HashSet<String> actualMoves = new HashSet<String>(pawnOne.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to move forward one or two, but not capture own queen or go off board.");

        assertDoesNotThrow(()->board.move("a2", "a4"));
        board.placePiece(new Pawn(board, ChessPiece.Color.BLACK), "b5");

        validMoves.clear();
        validMoves.addAll(Arrays.asList("a5", "b5"));
        actualMoves.clear();
        actualMoves.addAll(pawnOne.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to move forward one or capture opposing pawn.");

        for(int i=5; i<=8; i++){
            int finalI = i;
            assertDoesNotThrow(()->board.move("a"+(finalI -1), "a"+ finalI));
        }
        validMoves.clear();
        actualMoves.clear();
        actualMoves.addAll(pawnOne.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn shouldn't be able to move while on last row");
    }

    @Test
    public void testBlockJump(){
        Pawn pawnTwo = new Pawn(board, ChessPiece.Color.BLACK);
        board.placePiece(pawnTwo, "f7");

        HashSet<String> validMoves= new HashSet<>(Arrays.asList("f6", "f5"));
        HashSet<String> actualMoves= new HashSet<String>(pawnTwo.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to move forward one or two but no diagonals since no available captures");

        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "f6");

        validMoves.clear();
        actualMoves.clear();
        actualMoves.addAll(pawnTwo.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn shouldn't be able to jump piece or capture straight ahead");
    }

    @Test
    public void testInitialPosition(){
        Pawn pawn = new Pawn(board, ChessPiece.Color.WHITE);
        board.placePiece(pawn, "c3");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("c4"));
        HashSet<String> actualMoves = new HashSet<>(pawn.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn shouldn't be able to move two even though current position was initial placement");

    }

    @Test
    public void testToString(){
        assertEquals(new Pawn(board, ChessPiece.Color.WHITE).toString(), "\u2659", "Pawn::toString() is bad");
        assertEquals(new Pawn(board, ChessPiece.Color.BLACK).toString(), "\u265F", "Pawn::toString() is bad");
    }
}