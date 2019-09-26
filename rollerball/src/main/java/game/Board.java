package game;

public interface Board {
    public static char[][] blankBoard= new char[][]{
            new char[]{'-', '-', '-', '-', '-', '-', '-', '-', '-'},
            new char[]{'-', '_', '_', '_', '_', '_', '_', '_', '-'},
            new char[]{'-', '_', '_', '_', '_', '_', '_', '_', '-'},
            new char[]{'-', '_', '_', '-', '-', '-', '_', '_', '-'},
            new char[]{'-', '_', '_', '-', '-', '-', '_', '_', '-'},
            new char[]{'-', '_', '_', '-', '-', '-', '_', '_', '-'},
            new char[]{'-', '_', '_', '_', '_', '_', '_', '_', '-'},
            new char[]{'-', '_', '_', '_', '_', '_', '_', '_', '-'},
            new char[]{'-', '-', '-', '-', '-', '-', '-', '-', '-'}
    };
    public static char[][] startBoard = new char[][]{
            new char[]{'-', '-', '-', '-', '-', '-', '-', '-', '-'},
            new char[]{'-', '_', '_', 'R', 'B', '!', '_', '_', '-'},
            new char[]{'-', '_', '_', 'R', 'K', '@', '_', '_', '-'},
            new char[]{'-', '_', '_', '-', '-', '-', '_', '_', '-'},
            new char[]{'-', '_', '_', '-', '-', '-', '_', '_', '-'},
            new char[]{'-', '_', '_', '-', '-', '-', '_', '_', '-'},
            new char[]{'-', '_', '_', '1', 'k', 'r', '_', '_', '-'},
            new char[]{'-', '_', '_', '2', 'b', 'r', '_', '_', '-'},
            new char[]{'-', '-', '-', '-', '-', '-', '-', '-', '-'}
    };
    char[][] getBoard();
    void setBoard(char[][] board);
    void saveBoard();
}
