import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by CHARITHA on 02-Apr-17.
 */
public class PathFindingOnSquaredGrid {

    static int Ai; //x coordinate of the starting point
    static int Aj; //y coordinate of the starting point
    static int Bi; //x coordinate of the ending point
    static int Bj; //y coordinate of the ending point
    static String pathChoice;


    //static PriorityQueue<cell> closedList; //list of squares that need not to look at again

    // given an N-by-N matrix of open cells, return an N-by-N matrix
    // of cells reachable from the top
    public static boolean[][] flow(boolean[][] open) {
        int N = open.length;

        boolean[][] full = new boolean[N][N];
        for (int j = 0; j < N; j++) {
            flow(open, full, 0, j);
        }

        return full;
    }

    // determine set of open/blocked cells using depth first search
    public static void flow(boolean[][] open, boolean[][] full, int i, int j) {
        int N = open.length;

        // base cases
        if (i < 0 || i >= N) return ;    // invalid row
        if (j < 0 || j >= N) return ;    // invalid column
        if (!open[i][j]) return ;        // not an open cell
        if (full[i][j]) return ;         // already marked as open

        full[i][j] = true;

        flow(open, full, i+1, j);   // down
        flow(open, full, i, j+1);   // right
        flow(open, full, i, j-1);   // left
        flow(open, full, i-1, j);   // up
        flow(open, full, i-1, j-1); //top left
        flow(open, full, i-1, j+1); //top right
        flow(open, full, i+1, j-1); //bottom left
        flow(open, full, i+1, j-1); //top right


    }

    // determine set of open/blocked cells using depth first search
    public static boolean flow1(boolean[][] open, boolean[][] full, int i, int j) {
        int N = open.length;

        // base cases
        if ((i < 0 || i >= N) || (j < 0 || j >= N) || (!open[i][j]) || (full[i][j]) ){
            return true;
        }else
            return false;
        // invalid row
        // invalid column
        // not an open cell
        // already marked as open
    }

    // does the system percolate?
    public static boolean percolates(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        for (int j = 0; j < N; j++) {
            if (full[N-1][j]) return true;
        }

        return false;
    }

    // does the system percolate vertically in a direct way?
    public static boolean percolatesDirect(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        int directPerc = 0;
        for (int j = 0; j < N; j++) {
            if (full[N-1][j]) {
                // StdOut.println("Hello");
                directPerc = 1;
                int rowabove = N-2;
                for (int i = rowabove; i >= 0; i--) {
                    if (full[i][j]) {
                        // StdOut.println("i: " + i + " j: " + j + " " + full[i][j]);
                        directPerc++;
                    }
                    else break;
                }
            }
        }

        // StdOut.println("Direct Percolation is: " + directPerc);
        if (directPerc == N) return true;
        else return false;
    }

    // draw the N-by-N boolean matrix to standard draw
    public static void show(boolean[][] a, boolean which) {
        int N = a.length;
        StdDraw.setXscale(-1, N);;
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    StdDraw.square(j, N-i-1, .5);
                else StdDraw.filledSquare(j, N-i-1, .5);
    }

    // draw the N-by-N boolean matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle
    public static void show(boolean[][] a, boolean which, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);;
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    if ((i == x1 && j == y1) ||(i == x2 && j == y2)) {
                        StdDraw.circle(j, N-i-1, .5);
                    }
                    else StdDraw.square(j, N-i-1, .5);
                else StdDraw.filledSquare(j, N-i-1, .5);
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static boolean[][] random(int N, double p) {
        boolean[][] a = new boolean[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = StdRandom.bernoulli(p);
        return a;
    }



    // test client
    public static void main(String[] args) {
        // boolean[][] open = StdArrayIO.readBoolean2D();

        // The following will generate a 10x10 squared grid with relatively few obstacles in it
        // The lower the second parameter, the more obstacles (black cells) are generated
        boolean[][] randomlyGenMatrix = random(10, .8);

        StdArrayIO.print(randomlyGenMatrix);
        show(randomlyGenMatrix, true);



        if(percolates(randomlyGenMatrix)){

            System.out.println();
            System.out.println("The system percolates: " + percolates(randomlyGenMatrix));

            System.out.println();
            System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
            System.out.println();

            Scanner in = new Scanner(System.in);

            //Reading user's choice of metric to find the shortest pathway
            System.out.println("Select a metric to find the shortest pathway (Manhattan : M, Euclidean : E, Chebyshev : C): ");
            pathChoice = in.next();

            // Reading the coordinates for points A and B on the input squared grid.

            // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
            // Start the clock ticking in order to capture the time being spent on inputting the coordinates
            // You should position this command accordingly in order to perform the algorithmic analysis


            System.out.println("Enter i for A > ");
            Ai = in.nextInt();

            System.out.println("Enter j for A > ");
            Aj = in.nextInt();

            System.out.println("Enter i for B > ");
            Bi = in.nextInt();

            System.out.println("Enter j for B > ");
            Bj = in.nextInt();


            // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
            // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
            // You should position this command accordingly in order to perform the algorithmic analysis

            Stopwatch timerFlow = new Stopwatch();

            show(randomlyGenMatrix, true, Ai, Aj, Bi, Bj);

            ArrayList<AStar.cell> path = new AStar().pathFinding(randomlyGenMatrix, Ai, Aj, Bi, Bj, pathChoice);

            //StdDraw.setPenColor(Color.green);

           // for (AStar.cell Cell : path) {
             //   StdDraw.circle(Cell.y, randomlyGenMatrix.length - Cell.x - 1, .5);
            //}

            int squareCount =0;

            if (AStar.cellBoard[Bi][Bj].parentCell != null) {
                AStar.cell astarPa = AStar.cellBoard[Bi][Bj].parentCell;
                StdDraw.setPenColor(Color.green);
                StdDraw.filledSquare(AStar.cellBoard[Bi][Bj].y, AStar.arraySize - AStar.cellBoard[Bi][Bj].x -1, .5);
                while (astarPa.parentCell != null) {
                    StdDraw.filledSquare(astarPa.y, AStar.arraySize - astarPa.x -1, .5);
                    astarPa = astarPa.parentCell;
                    squareCount++;
                }
                StdDraw.filledSquare(AStar.cellBoard[Ai][Aj].y, AStar.arraySize - AStar.cellBoard[Ai][Aj].x -1, .5);
                //StdDraw.point(AStar.cellBoard[Ai][Aj].x, AStar.cellBoard[Ai][Aj].y);
                //StdDraw.line(AStar.cellBoard[Ai][Aj].x, AStar.cellBoard[Ai][Aj].y, AStar.cellBoard[Bi][Bj].x, AStar.cellBoard[Bi][Bj].y);
            }

            System.out.println("Total cost : " + (squareCount + 2));

            StdOut.println("Elapsed time = " + timerFlow.elapsedTime());








        }else{
            System.out.println();
            System.out.println("The system percolates: " + percolates(randomlyGenMatrix));

            System.out.println();
            System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
            System.out.println();
        }


    }


}

