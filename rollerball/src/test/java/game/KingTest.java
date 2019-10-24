package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


class KingTest {
    private ChessBoard board;
    @BeforeEach
    void initializeBoard(){
        board = new ChessBoard();
    }
    @Test
    void cornerCase() {
        King king = new King(board, ChessPiece.Color.WHITE);
        board.placePiece(king, "a1");

        HashSet<String> validMoves = new HashSet<String>(Arrays.asList("b1", "a2", "b2"));
        HashSet<String> actualMoves = new HashSet<String>(king.legalMoves());

        assertEquals(validMoves, actualMoves,"Corner case incorrect");

        assertThrows(IllegalPositionException.class, () -> king.setPosition("a0"), "Moving off board should be illegal");
        assertThrows(IllegalPositionException.class, () -> king.setPosition((char)('a'-1)+"1"), "Moving off board should be illegal");
        assertThrows(IllegalPositionException.class, () -> king.setPosition("i1"), "Moving off board should be illegal");
        assertThrows(IllegalPositionException.class, () -> king.setPosition("h9"), "Moving off board should be illegal");
        assertThrows(IllegalPositionException.class, () -> king.setPosition("i9"), "Moving off board should be illegal");
    }

    @Test
    void otherPieces(){
        King king = new King(board, ChessPiece.Color.WHITE);
        board.placePiece(king, "b2");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("a1", "a2", "a3","b1", "b3","c1","c2","c3"));
        HashSet<String> actualMoves = new HashSet<>(king.legalMoves());
        assertEquals(validMoves, actualMoves, "King should be able to move around self");

        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "b3");
        validMoves.remove("b3");
        actualMoves.clear();
        actualMoves.addAll(king.legalMoves());
        assertEquals(validMoves, actualMoves, "King shouldn't be able to capture own pawn");

        board.placePiece(new King(board, ChessPiece.Color.BLACK), "b1");
        actualMoves.clear();
        actualMoves.addAll(king.legalMoves());
        assertEquals(validMoves, actualMoves, "King should be able to capture opposing king");

    }

    @Test
    void initialPosition(){
        assertEquals(new King(board, ChessPiece.Color.BLACK).getPosition(), "a1", "Position shouldn't change when piece isn't placed. Default row, col is 0,0 -> a,1.");
    }

    @Test
    public void testToString(){
        assertEquals("\u2654", new King(board, ChessPiece.Color.WHITE).toString(), "King::toString() is bad");
        assertEquals("\u265A", new King(board, ChessPiece.Color.BLACK).toString(), "King::toString() is bad");
    }
}