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

        HashSet<String> validMoves = new HashSet<>(Arrays.asList("a3"));
        HashSet<String> actualMoves = new HashSet<String>(pawnOne.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to move forward one, but not capture own pawn or go off board.");

        assertDoesNotThrow(()->board.move("a2", "a3"));
        board.placePiece(new Pawn(board, ChessPiece.Color.BLACK), "b4");

        validMoves.clear();
        validMoves.addAll(Arrays.asList("a4", "b4"));
        actualMoves.clear();
        actualMoves.addAll(pawnOne.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to move forward one or capture opposing pawn.");

        assertDoesNotThrow(()->board.move("a3", "b4"));
        validMoves.clear();
        validMoves.add("a5");
        validMoves.add("b5");
        actualMoves.clear();
        actualMoves.addAll(pawnOne.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to move diagonally without capturing.");
    }

    @Test
    public void testMaximalMove(){
        Pawn pawnTwo = new Pawn(board, ChessPiece.Color.BLACK);
        board.placePiece(pawnTwo, "e6");

        HashSet<String> validMoves= new HashSet<>(Arrays.asList("f7", "f6", "f5"));
        HashSet<String> actualMoves= new HashSet<String>(pawnTwo.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to move forward or to either diagonal.");

        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "f6");

        actualMoves.clear();
        actualMoves.addAll(pawnTwo.legalMoves());
        assertEquals(validMoves, actualMoves, "Pawn should be able to capture forward.");
    }

    @Test
    public void testToString(){
        assertEquals(new Pawn(board, ChessPiece.Color.WHITE).toString(), "\u2659", "Pawn::toString() is bad");
        assertEquals(new Pawn(board, ChessPiece.Color.BLACK).toString(), "\u265F", "Pawn::toString() is bad");
    }
}