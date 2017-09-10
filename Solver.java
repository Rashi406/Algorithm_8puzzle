import edu.princeton.cs.algs4.MinPQ;
import java.util.*;
import edu.princeton.cs.algs4.In;

public class Solver {
    private final Board bd;
    private int move;
    private final int N;
    private ArrayList<Node> path;

    private class Node implements Comparable<Node> {
        private Board br;
        private Node prev;
        private int man;
        private boolean open;
        private int move;

        public Node(Board board, Node previ) {
            br = board;
            man = board.manhattan();
            open = false;
            prev = previ;
            if (previ != null)
                move = prev.move + 1;
            else
                move = 0;

        }

        public int compareTo(Node that) {

            int priority1 = this.man + this.move;
            int priority2 = that.man + that.move;
            if (priority1 > priority2)
                return 1;
            else if (priority1 == priority2 && this.man > that.man)
                return 1;
            else if (priority1 == priority2 && this.man == that.man)
                return 0;
            else
                return -1;
        }

    }

    public Solver(Board initial) // find a solution to the initial board (using
                                 // the A* algorithm)
    {
        if (initial == null)
            throw new java.lang.NullPointerException();
        bd = initial;
        move = 0;
        // boolean r;
        N = initial.dimension();

        if (this.isSolvable())
            path = bfs(new Node(initial, null));

    }

    private ArrayList<Node> backtrace(Node start, Node end) {
        ArrayList<Node> path1 = new ArrayList<Node>();
        path1.add(end);
        Node nd = end;
        while (nd != start) {
            path1.add(nd.prev);
            nd = nd.prev;
        }

        move = end.move;
        return path1;
    }

    private ArrayList<Node> bfs(Node node) {

        // MinPQ<Node> q = new MinPQ<Node>();
        // Comparator<Node> comparator;

        MinPQ<Node> q = new MinPQ<Node>();
        q.insert(node);
        // int g=0;
        /*
         * ArrayList<Node> path = new ArrayList<Node>(); path.add(node);
         */
        // int pr = 0;
        Node element = null;
        while (!q.isEmpty()) {
            // g++;
            element = q.delMin();

            /*
             * if(g<15) { System.out.println(element.move+element.man);
             * System.out.println(element.br.toString()); }
             */
            if (element.br.isGoal()) {
                return backtrace(node, element);
            }

            // pr = prior(element);
            for (Board neighbo : element.br.neighbors()) {
                Node n = new Node(neighbo, element);
                if (element.prev == null || neighbo.equals(element.prev.br) == false) {

                    if (!n.open && n != null)// && n.man<=pr)
                    {
                        n.open = true;
                        q.insert(n);
                        /*
                         * if(g==1) { System.out.println(n.move+n.man);
                         * System.out.println(n.br.toString()); }
                         */
                    }

                }
            }
            // break;

        }
        return null;
    }

    /*
     * private int prior(Node a) { int nq = 0; for (Board neighbo :
     * a.br.neighbors()) { Node n = new Node(neighbo, a); if (a.prev == null ||
     * !neighbo.equals(a.prev.br)) { if (n.man <= a.man) return n.man; else nq =
     * n.man; } } return nq;
     * 
     * }
     */

    public boolean isSolvable() // is the initial board solvable?
    {
        if (bd.isGoal())
            return true;
        int k = 0;
        String str = bd.toString();
        StringTokenizer st = new StringTokenizer(str);
        int[] arr = new int[(N * N)];
        while (st.hasMoreTokens()) {
            if (k == 0) {
                st.nextToken();
                k++;
                continue;
            }
            if (k < N * N)
                arr[k] = Integer.parseInt(st.nextToken());
            else
                arr[0] = Integer.parseInt(st.nextToken());
            k++;
        }
        /*
         * for(int j=0;j<arr.length;j++) System.out.println(arr[j]);
         */
        int r = 0, count = 0;

        if (N % 2 != 0) {
            count = getInvCount(arr);
            if (count % 2 != 0)
                return false;
            else
                return true;
        } else {
            count = getInvCount(arr);
            if (arr[0] == 0)
                r = N - 1;

            for (int i = 1; i < arr.length; i++) {
                if (arr[i] == 0) {
                    r = (i - 1) / N;
                    break;

                }
            }

            // System.out.println(" "+r);

            if ((r + count) % 2 != 0)
                return true;

            else
                return false;
        }

    }

    private int getInvCount(int arr[]) {
        int inv_count = 0;
        // int c=arr[0];

        for (int i = 1; i < N * N - 1; i++) {
            for (int j = i + 1; j < N * N; j++) {
                // count pairs(i, j) such that i appears
                // before j, but i > j.
                if (arr[j] != 0 && arr[i] != 0 && arr[i] > arr[j])
                    inv_count++;
            }
            if (arr[0] != 0 && arr[i] != 0 && arr[i] > arr[0])
                inv_count++;

        }
        if (arr[0] != 0 && arr[(N * N) - 1] != 0 && arr[(N * N) - 1] > arr[0])
            inv_count++;
        return inv_count;
    }

    public int moves() // min number of moves to solve initial board; -1 if
                       // unsolvable
    {
        if (this.isSolvable())
            return move;
        else
            return -1;
    }

    public Iterable<Board> solution() // sequence of boards in a shortest
                                      // solution; null if unsolvable
    {
        if (!this.isSolvable())
            return null;
        ArrayList<Board> bord = new ArrayList<Board>();
        for (int i = path.size() - 1; i >= 0; i--) {
            bord.add(path.get(i).br);
        }
        return bord;

    }

    public static void main(String[] args) // solve a slider puzzle (given
                                           // below)
    {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {

            System.out.println("No solution possible");
        } else {
            // System.out.println(solver.isSolvable());
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                System.out.println(board);
        }
    }
}
