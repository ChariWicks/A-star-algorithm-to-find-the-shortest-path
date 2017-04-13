import java.awt.List;
import java.util.*;

/**
 * Created by CHARITHA on 02-Apr-17.
 * IIT ID : 2015020
 * UOW ID : W1583049
 */
public class AStar{

    static cell startingPoint; //starting cell of the shortest pathway
    cell currentPoint; //current cell position
    static cell endingPoint; //goal position
    cell testPoint; //testing cell point
    PriorityQueue<cell> openList; //list of squares that need to be checked out
    //horizontal and vertical distance values for manhattan distance
    final double horVerMan = 1.0;
    //diagonal distance value for manhattan distance
    final double diagMan = 2.0;
    //horizontal and vertical distance values for chebyshev distance
    final double horVerCheb = 1.0;
    //diagonal distance value for chebyshev distance
    final double diagCheb = 1.0;
    //horizontal and vertical distance values for euclidean distance
    final double horVerEuc = 1.0;
    //diagonal distance value for euclidean distance
    final double diagEuc = 1.4;

    static int arraySize;
    static  cell[][] cellBoard;


    ArrayList<cell> pathFinding(boolean[][] randomlyGenMatrix, int Ai, int Aj, int Bi, int Bj, String pathChoice) {

        arraySize = randomlyGenMatrix.length;

        //Creating starting and ending cell objects
             /*assigning user entered coordinates to the starting object*/
        startingPoint = new cell(Ai, Aj);
        endingPoint = new cell(Bi, Bj);
        cellBoard = new cell[arraySize][arraySize];

        System.out.println("starting x: " + startingPoint.x);
        System.out.println("starting y: " + startingPoint.y);
        System.out.println("ending x: " + endingPoint.x);
        System.out.println("ending y: " + endingPoint.y);

        //saving cells and blocked cells in a array
        for (int i = 0; i < arraySize; ++i) {
            for (int j = 0; j < arraySize; ++j) {
                cellBoard[i][j] = new cell(i, j);
                new cell(i,j).heuristicValue = calculateHeuristicVal(Ai,Aj, Bi, Bj, pathChoice);
                if (randomlyGenMatrix[i][j] == false) {
                    cellBoard[i][j].blockState = true;
                }
            }
        }

        openList = new PriorityQueue<>(arraySize, new Comparator<cell>() {
            @Override
            public int compare(cell o1, cell o2) {
                if (o1.getTotalValue() > o2.getTotalValue()) {
                    return 1;
                } else if (o1.getTotalValue() < o2.getTotalValue()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        ArrayList<cell> closedList = new ArrayList<>();
        openList.add(startingPoint); //adding starting cell into the open list

        startingPoint.gValue = 0; //g value of the starting point is zero
        startingPoint.totalValue = 0; //setting zero to the total value
            /* calculating g + h value of the starting point*/
        //startingPoint.totalValue = selectGValue(pathChoice) + calculateHeuristicVal(Aj, Ai, Bj, Bi, pathChoice);

       while (!openList.isEmpty()) {
            currentPoint = openList.poll();
            if (currentPoint.x == Bi && currentPoint.y == Bj) {
                break;
            }
            java.util.List<cell>  neighbourList= adjecentCells(currentPoint, cellBoard, pathChoice);
            for (cell neighbour : neighbourList) {
                if (!closedList.contains(neighbour)) {
                    if (!openList.contains(neighbour)) {
                        if( (neighbour.x == currentPoint.x-1 && neighbour.y == currentPoint.y-1) || (neighbour.x == currentPoint.x+1 && neighbour.y == currentPoint.y-1) && (neighbour.x == currentPoint.x-1 && neighbour.y == currentPoint.y+1) &&     (neighbour.x == currentPoint.x+1 && neighbour.y == currentPoint.y+1) ){
                            neighbour.gValue = currentPoint.gValue + selectGValueD(pathChoice);
                            neighbour.parentCell = currentPoint;
                            openList.add(neighbour);
                        }else{
                            neighbour.gValue = currentPoint.gValue + selectGValueHV(pathChoice);
                            neighbour.parentCell = currentPoint;
                            openList.add(neighbour);
                        }
                    } else {
                        double gCost = currentPoint.gValue + 1;
                        if (neighbour.gValue > gCost) {
                            neighbour.gValue = gCost;
                            neighbour.parentCell = currentPoint;
                        }

                    }

                }
            }
            closedList.add(currentPoint);

        }
        return  closedList;


        /*while (openList.size() > 0) {

            currentPoint = openList.poll(); //remove lowest H value cell from open list and add it to the closed list
            if (currentPoint.x == Bi && currentPoint.y == Bj) {
                break;
            }

            java.util.List<cell> adjecentCells = adjecentCells(currentPoint, cellBoard, pathChoice);
            for (cell neighbours : adjecentCells) {
                if (!closedList.contains(neighbours)) {
                    neighbours.totalValue = neighbours.gValue + calculateHeuristicVal(Ai, Aj, Bi, Bj, pathChoice);
                    if (!openList.contains(neighbours)) {
                        openList.add(neighbours);
                    } else {
                        cell nxtNeighbour = openList.element();
                        if (neighbours.gValue < nxtNeighbour.gValue) {
                            nxtNeighbour.gValue = neighbours.gValue;
                            nxtNeighbour.parentCell = neighbours.parentCell;
                        }
                    }
                }

            }
            closedList.add(currentPoint);*/


            //cell testPoint; //testing cell point
            //System.out.println("current y: " +currentPoint.y);
            // System.out.println("current x: " +currentPoint.x);

                /*checking cells around
                 whether they are blocked cells, accessible cells or they are
                 already being checked*/


            //  currentPoint.visitState = true;
            //}


            //return closedList;

            // closedList.add(currentPoint); //adding current point to closed list

            // System.out.println("Coordinates for A: [" + Ai + "," + Aj + "]");
            // System.out.println("Coordinates for B: [" + Bi + "," + Bj + "]");

        }
        // Checking if a path exists
        /*if (!(cellBoard[endingPoint.x][endingPoint.y].gValue == Integer.MAX_VALUE)) {
            //Trace back the path
            cell current = cellBoard[endingPoint.x][endingPoint.y];

            while (current.parentCell != null) {
                closedList.add(current.parentCell);
                current = current.parentCell;
            }
        } else System.out.println("No possible path");*/
        //return  closedList;


    private java.util.List<cell> adjecentCells(cell currentPoint, cell[][] cellBoard, String pathC) {

        java.util.List<cell> adjacentCells = new ArrayList<>();
        int cellBoardSize = cellBoard.length;
        //up
        if ((currentPoint.x - 1) >= 0 && !cellBoard[currentPoint.x - 1][currentPoint.y].blockState == true) {
            //cellBoard[currentPoint.x-1][currentPoint.y].gValue = currentPoint.gValue + selectGValueHV(pathC);
            //cellBoard[currentPoint.x-1][currentPoint.y].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x - 1][currentPoint.y]);
        }
        //up left
        if ((currentPoint.x - 1) >= 0 && (currentPoint.y-1) >=0 && !cellBoard[currentPoint.x - 1][currentPoint.y-1].blockState == true) {
            //cellBoard[currentPoint.x-1][currentPoint.y - 1].gValue = currentPoint.gValue + selectGValueD(pathC);
            //cellBoard[currentPoint.x-1][currentPoint.y - 1].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x - 1][currentPoint.y-1]);
        }
        //up right
        if ((currentPoint.x - 1) >= 0 && (currentPoint.y+1) < cellBoardSize &&  !cellBoard[currentPoint.x - 1][currentPoint.y+1].blockState == true) {
            //cellBoard[currentPoint.x-1][currentPoint.y+1].gValue = currentPoint.gValue + selectGValueD(pathC);
            //cellBoard[currentPoint.x-1][currentPoint.y+1].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x - 1][currentPoint.y+1]);
        }
        //right
        if ((currentPoint.y + 1) < cellBoardSize && !cellBoard[currentPoint.x][currentPoint.y+1].blockState == true) {
            //cellBoard[currentPoint.x][currentPoint.y+1].gValue = currentPoint.gValue + selectGValueHV(pathC);
            //cellBoard[currentPoint.x][currentPoint.y+1].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x][currentPoint.y+1]);
        }
        //left
        if ((currentPoint.y - 1) >= 0 && !cellBoard[currentPoint.x][currentPoint.y-1].blockState == true) {
            //cellBoard[currentPoint.x][currentPoint.y-1].gValue = currentPoint.gValue + selectGValueHV(pathC);
            //cellBoard[currentPoint.x][currentPoint.y-1].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x][currentPoint.y-1]);
        }
        //down
        if ((currentPoint.x + 1) < cellBoardSize && !cellBoard[currentPoint.x + 1][currentPoint.y].blockState == true) {
            //cellBoard[currentPoint.x+1][currentPoint.y].gValue = currentPoint.gValue + selectGValueHV(pathC);
            //cellBoard[currentPoint.x+1][currentPoint.y].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x + 1][currentPoint.y]);
        }
        //down left
        if ((currentPoint.x + 1) < cellBoardSize && (currentPoint.y-1) >=0 && !cellBoard[currentPoint.x + 1][currentPoint.y - 1].blockState == true) {
            //cellBoard[currentPoint.x+1][currentPoint.y-1].gValue = currentPoint.gValue + selectGValueD(pathC);
            //cellBoard[currentPoint.x+1][currentPoint.y-1].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x + 1][currentPoint.y-1]);
        }
        //down right
        if ((currentPoint.x + 1) < cellBoardSize && (currentPoint.y+1) < cellBoardSize  && !cellBoard[currentPoint.x + 1][currentPoint.y +1].blockState == true) {
            //cellBoard[currentPoint.x+1][currentPoint.y+1].gValue = currentPoint.gValue + selectGValueD(pathC);
           // cellBoard[currentPoint.x+1][currentPoint.y+1].parentCell = currentPoint;
            adjacentCells.add(cellBoard[currentPoint.x + 1][currentPoint.y+1]);
        }

        return (java.util.List<cell>) adjacentCells;

    }

    //return heuristic value of a cell using three different metrics
    public double calculateHeuristicVal(int startX, int startY, int endX, int endY, String pathC){

        cell cellH =  new cell(startX, startY);

        //calculating H value using Chebyshev metric
        if(pathC.equalsIgnoreCase("C")){

            double firstHalfC = endX - startX; //total of first half of the chebyshev metric
            double lastHalfC = endY - startY; //total of other half of the chebyshev metric

            double firstHalfCMin = firstHalfC * (-1);
            double lastHalfCMin = lastHalfC * (-1);

            double hTotalC = Math.max(firstHalfCMin, lastHalfCMin);//get the total manhattan heuristic value
            //cellH.totalValue = hTotalC;

            return hTotalC;
        }

        //calculating H value using Manhattan metric
        if(pathC.equalsIgnoreCase("M")){

            double firstHalfM = endX - startX; //total of first half of the manhattan metric
            double lastHalfM = endY - startY; //total of other half of the manhattan metric

            //checking above values need to multiply by one
            if(firstHalfM < 0){
                firstHalfM = firstHalfM * (-1);
            }

            if(lastHalfM < 0){
                lastHalfM = lastHalfM * (-1);
            }

            double hTotalM = firstHalfM + lastHalfM;//get the total manhattan heuristic value
            //cellH.totalValue = hTotalM;

            return hTotalM;
        }
        //calculating H value using Euclidean metric
        if(pathC.equalsIgnoreCase("E")){

            double firstHalfE = endX - startX; //total of first half of the euclidean metric
            double lastHalfE = endY - startY; //total of other half of the euclidean metric

            double firstHalfEPow = Math.pow(firstHalfE, 2.0); //power value of firstHalfE
            double lastHalfEPow = Math.pow(lastHalfE, 2.0); //power value of lastHalfE

            double hTotalE; //total manhattan heuristic value
            hTotalE = Math.sqrt(firstHalfEPow + lastHalfEPow); //square root of the total

           // cellH.totalValue = hTotalE;
            return hTotalE;
        }
        else return 0;
    }

    public double selectGValueHV(String pathC){ //G value of the vertical and horizontal cells
        if(pathC.equalsIgnoreCase("M")){
            return horVerMan;
        }
        if(pathC.equalsIgnoreCase("E")){
            return horVerEuc;
        }
        if(pathC.equalsIgnoreCase("C"));
        return horVerCheb;
    }

    public double selectGValueD(String pathC){ //G value of the diagonal cells
        if(pathC.equalsIgnoreCase("M")){
            return diagMan;
        }
        if(pathC.equalsIgnoreCase("E")){
            return diagEuc;
        }
        if(pathC.equalsIgnoreCase("C"));
        return diagCheb;
    }

    static class cell{

        int x; //x coordinate of the cell
        int y; //y coordinate of the cell
        double gValue  =     Integer.MAX_VALUE; //g cost of a cell
        double heuristicValue = 0; //heuristic cost of a cell (h)
        double totalValue = 0; //total cost of a cell (g + h)
        boolean blockState; //blocked statement of a cell
        boolean visitState; //visited statement of a cell
        cell parentCell;

        cell(int x, int y){
            this.x = x;
            this.y = y;
        }

        public double getTotalValue(){
            return heuristicValue+gValue;
        }



    }
}

