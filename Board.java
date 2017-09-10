
import java.util.ArrayList;

public class Board {
    
    private final int[][] boards;
    private final int n;

    public Board(int[][] blocks) // construct a board from an n-by-n array of
                                 // blocks
                                 // (where blocks[i][j] = block in row i, column
                                 // j)
    {
        // int k = 1;
        int l = blocks.length;
        boards = new int[l][l];       
        
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                boards[i][j] = blocks[i][j];
                /*
                 * if (k < l * l) { board[k] = blocks[i][j]; k++; }
                 */

            }
        }
        n = l;
    }

    public int dimension() // board dimension n
    {
        return n;
    }

    public int hamming() // number of blocks out of place
    {
        int k = 1;
        int out = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (boards[i][j] != k && boards[i][j] != 0)
                    out++;
                k++;
            }
        }
        return out;
    }

    public int manhattan() // sum of Manhattan distances between blocks and goal
    {
        int l = 0, m = 0, sum = 0, k = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (boards[i][j] != k && boards[i][j] != 0) {

                    l = i - ((boards[i][j] - 1) / n);
                    m = j - ((boards[i][j] - 1) % n);
                    if (l < 0)
                        l = l * -1;
                    if (m < 0)
                        m = m * -1;

                }

                sum = sum + l + m;
                l = 0;
                m = 0;
                k++;
            }
        }

        return sum;
    }

    public boolean isGoal() // is this board the goal board?
    {

        return this.hamming() == 0;
    }

    public Board twin() // a board that is obtained by exchanging any pair of
                        // blocks
    {

        if (boards[0][0] != 0 && boards[0][1] != 0)
            return new Board(swap(0, 0, 0, 1));
        else
            return new Board(swap(1, 0, 1, 1));

    }

    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == row1 && j == col1)
                    copy[i][j] = boards[row2][col2];
                else if (i == row2 && j == col2)
                    copy[i][j] = boards[row1][col1];
                else
                    copy[i][j] = boards[i][j];

            }
        }
        /*
         * int tmp = copy[row1][col1]; copy[row1][col1] = copy[row2][col2];
         * copy[row2][col2] = tmp;
         */

        return copy;
    }

    public boolean equals(Object y) // does this board equal y?
    {
        if (y == null || !(y instanceof Board) || ((Board) y).boards.length != boards.length)
            return false;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (boards[row][col] != ((Board) y).boards[row][col])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() // all neighboring boards
    {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        int r = 0, c = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                if (boards[i][j] == 0) {
                    r = i;
                    c = j;
                    break;
                }
        }
        Board br;
        if (c > 0) {
            br = new Board(swap(r, c, r, c - 1));
            neighbors.add(br);
        }
        if (c < n - 1) {
            br = new Board(swap(r, c, r, c + 1));
            neighbors.add(br);
        }
        if (r > 0) {
            br = new Board(swap(r, c, r - 1, c));
            neighbors.add(br);
        }
        if (r < n - 1) {
            br = new Board(swap(r, c, r + 1, c));
            neighbors.add(br);
        }

        return neighbors;

    }

    public String toString() // string representation of this board (in the
                             // output format specified below)
    {
        StringBuilder str = new StringBuilder();
        str.append(dimension() + "\n");
        for (int row = 0; row < boards.length; row++) {
            for (int col = 0; col < boards.length; col++)
                str.append(String.format("%2d ", boards[row][col]));
            str.append("\n");
        }

        return str.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {

    }
}
