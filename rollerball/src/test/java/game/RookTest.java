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
        for(int i=5; i>=1;i--){
            validMoves.add("g"+(7-i));
            validMoves.add((char)('b'+i)+"7");
        }
        validMoves.add("b6");
        validMoves.add("g1");
        validMoves.add("a7");

        HashSet<String> actualMoves = new HashSet<>(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Rook should be able to move vertically and horizontally");

        board.placePiece(new Pawn(board, ChessPiece.Color.BLACK), "f7");
        validMoves.clear();
        validMoves.addAll(Arrays.asList("c7", "d7", "e7", "b6", "a7"));
        actualMoves.clear();
        actualMoves.addAll(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Same side piece should block move to itself and beyond it.");

        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "c7");
        validMoves.remove("d7");
        validMoves.remove("e7");
        actualMoves.clear();
        actualMoves.addAll(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Opposing piece should allow move to itself and block beyond it.");
    }

    @Test
    public void cornerCase(){
        Rook cornerRook = new Rook(board, ChessPiece.Color.WHITE);
        board.placePiece(cornerRook, "g1");
        HashSet<String> validMoves = new HashSet<>();
        for (int i=1; i<=6; i++){
            validMoves.add("a"+(1+i));
            validMoves.add((char)('g'-i)+"1");
        }
        validMoves.add("a1");
        validMoves.add("g2");
        HashSet<String> actualMoves= new HashSet<>(cornerRook.legalMoves());
        assertEquals(validMoves, actualMoves, "Corner case incorrect");
    }

    @Test
    public void fullSectionCase(){
        Rook bigRook = new Rook(board, ChessPiece.Color.WHITE);
        board.placePiece(bigRook, "b1");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("a1", "c1"));
        for(int i=2;i<=7;i++){
            validMoves.add("a"+i);
            validMoves.add("b"+i);
        }
        HashSet<String> actualMoves = new HashSet<>(bigRook.legalMoves());
        assertEquals(validMoves, actualMoves, "Huge aoe case incorrect");
    }

    @Test
    public void rightIsBackwards(){
        Rook rook =  new Rook(board, ChessPiece.Color.BLACK);
        board.placePiece(rook, "b2");
        HashSet<String> validMoves = new HashSet<>();
        for (int i=3; i<=7; i++){
            validMoves.add("b"+i);
        }
        validMoves.add("c2");
        validMoves.add("a2");
        validMoves.add("b1");

        HashSet<String> actualMoves = new HashSet<>(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Right is backwards case is incorrect");
    }

    @Test
    public void middleOfLane(){
        Rook rook = new Rook(board, ChessPiece.Color.WHITE);
        board.placePiece(rook, "b3");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("b2", "b4", "b5", "b6", "b7", "a3"));
        HashSet<String> actualMoves = new HashSet<>(rook.legalMoves());
        assertEquals(validMoves, actualMoves, "Rook in middle of lane is incorrect");
    }

    @Test
    public void testToString(){
        assertEquals(new Rook(board, ChessPiece.Color.WHITE).toString(), "\u2656", "Rook::toString() is bad");
        assertEquals(new Rook(board, ChessPiece.Color.BLACK).toString(), "\u265C", "Rook::toString() is bad");
    }
}