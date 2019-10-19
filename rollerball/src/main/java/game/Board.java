package game;

public class Board {
		private char[][] board;
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


		public Board(){
			board = startBoard;
		}

    public char[][] getBoard(){
				return board;
		}
    public void setBoard(char[][] board){
			this.board = board;
		}
    public void saveBoard(){}

		public String toString(){
			String output = "";
			for(char[] arr : board){
				for(char c : arr){
					output += c + ", ";
				}
				output = output.substring(0, output.lastIndexOf(", ")) + "\n";
			}

			return output;
		}
}
