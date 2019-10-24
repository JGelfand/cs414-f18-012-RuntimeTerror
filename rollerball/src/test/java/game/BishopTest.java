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
    void cornerCase() {
        board.placePiece(bishop, "a1");

        HashSet<String> validMoves = new HashSet<>();
        for(int i=1;i<=7;i++){
            validMoves.add((char)('a'+i)+""+(char)('1'+i));
        }
        HashSet<String> actualMoves = new HashSet<>(bishop.legalMoves());
        assertEquals(validMoves, actualMoves, "Corner case incorrect");
    }

    @Test
    public void noJumpingFriendlies(){
        board.placePiece(bishop, "d4");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "c3");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "e5");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "c5");
        board.placePiece(new Pawn(board, ChessPiece.Color.WHITE), "f2");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("e3"));
        assertEquals(validMoves, new HashSet<>(bishop.legalMoves()), "Movement should be blocked by friendly pieces");
    }

    @Test
    public void moveIntoEnemies(){
        board.placePiece(bishop, "h1");
        board.placePiece(new Pawn(board, ChessPiece.Color.BLACK), "e4");
        HashSet<String> validMoves = new HashSet<>(Arrays.asList("g2", "f3", "e4"));
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