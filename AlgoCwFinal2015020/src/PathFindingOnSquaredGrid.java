import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by CHARITHA on 02-Apr-17.
 */
public class PathFindingOnSquaredGrid {

    static int Ai; //y coordinate of the starting point
    static int Aj; //x coordinate of the starting point
    static int Bi; //y coordinate of the ending point
    static int Bj; //x coordinate of the ending point
    static String pathChoice;
    
    private static DecimalFormat decimal2 = new DecimalFormat(".##");


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
        boolean[][] randomlyGenMatrix = random(10, .6);

        StdArrayIO.print(randomlyGenMatrix);
        show(randomlyGenMatrix, true);

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
        
            
        AStar.cell astarEn = AStar.cellBoard[Bi][Bj];
        double mCost = astarEn.totalValue; //total movement cost
        double tCost = 0.0; //total cost
        double numberOfCells = 0.0; //total number of cells in the shortest path
        
        StdDraw.setPenColor(Color.red);
        //StdDraw.point(AStar.cellBoard[Bi][Bj].xCell, AStar.cellBoard[Bi][Bj].yCell);
        StdDraw.square(AStar.cellBoard[Bi][Bj].xCell, randomlyGenMatrix.length-AStar.cellBoard[Bi][Bj].yCell-1, 0.4);
        StdDraw.filledCircle(AStar.cellBoard[Ai][Aj].xCell, randomlyGenMatrix.length-AStar.cellBoard[Ai][Aj].yCell-1, 0.4);
            
        StdDraw.setPenColor(Color.green);

        for (AStar.cell Cell : path) {
            StdDraw.filledSquare(Cell.xCell, 10 - Cell.yCell - 1, .5);
        }
        
       if(pathChoice.equalsIgnoreCase("M")){
            
                    StdDraw.setPenColor(Color.RED);
        StdDraw.setPenRadius(0.01);

        for (AStar.cell Cell : path) {
            try{
                StdDraw.line(Cell.xCell, randomlyGenMatrix.length - Cell.yCell - 1, Cell.parentCell.xCell,randomlyGenMatrix.length - Cell.parentCell.yCell-1  );
            }catch(NullPointerException nullPointer){
                
            }
            
        }
            
        }
        

        
        for (AStar.cell Cell : path) {
            tCost = tCost + Cell.totalValue;
            //System.out.println(t);
            numberOfCells++;
        }
        
        StdDraw.filledSquare(AStar.cellBoard[Bi][Bj].xCell, randomlyGenMatrix.length-AStar.cellBoard[Bi][Bj].yCell-1, 0.2);
        StdDraw.filledCircle(AStar.cellBoard[Ai][Aj].xCell, randomlyGenMatrix.length-AStar.cellBoard[Ai][Aj].yCell-1, 0.2);

        System.out.println("Number of cells in the path : " + numberOfCells);
        System.out.println("Total movement cost of the shortest path : " + decimal2.format(mCost));
        System.out.println("Total cost (Fcost of the cells in shortest path) : " + decimal2.format(tCost));

            StdOut.println("Elapsed time = " + timerFlow.elapsedTime());




    }


}

