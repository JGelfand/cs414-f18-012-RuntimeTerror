package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {
    private ChessBoard board;
    @BeforeEach
    public void setupBoard(){
        board = new ChessBoard();
    }
    @Test
    void legalMoves() {
        Rook rook = new Rook(board, ChessPiece.Color.BLACK);
        board.placePiece(rook, "b7");

        HashSet<String> validMoves = new HashSet<String>();
        for(int i=6; i>=1;i--){
            validMoves.add("b"+i);
            validMoves.add((char)('b'+i)+"7");
        }
        validMoves.add("b8");
        validMoves.add("a7");

        HashSet<String> actualMoves = new HashSet<>(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Rook should be able to move vertically and horizontally");

        board.placePiece(new Pawn(board, ChessPiece.Color.BLACK), "f7");
        validMoves.remove("f7");
        validMoves.remove("g7");
        validMoves.remove("h7");
        actualMoves.clear();
        actualMoves.addAll(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Same side piece should block move to itself and beyond it.");

        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "b3");
        validMoves.remove("b2");
        validMoves.remove("b1");
        actualMoves.clear();
        actualMoves.addAll(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Opposing piece should allow move to itself and block beyond it.");
    }

    @Test
    public void cornerCase(){
        Rook cornerRook = new Rook(board, ChessPiece.Color.WHITE);
        board.placePiece(cornerRook, "h1");
        HashSet<String> validMoves = new HashSet<>();
        for (int i=1; i<=7; i++){
            validMoves.add("h"+(1+i));
            validMoves.add((char)('h'-i)+"1");
        }
        HashSet<String> actualMoves= new HashSet<>(cornerRook.legalMoves());
        assertEquals(validMoves, actualMoves, "Corner case incorrect");
    }

    @Test
    public void testToString(){
        assertEquals(new Rook(board, ChessPiece.Color.WHITE).toString(), "\u2656", "Rook::toString() is bad");
        assertEquals(new Rook(board, ChessPiece.Color.BLACK).toString(), "\u265C", "Rook::toString() is bad");
    }
}