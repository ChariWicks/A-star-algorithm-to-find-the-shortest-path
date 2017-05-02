
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
    
    
    final double horVerMan = 1.0; //horizontal and vertical distance values for manhattan distance

    final double horVerCheb = 1.0; //horizontal and vertical distance values for chebyshev distance
    final double diagCheb = 1.4; //diagonal distance value for chebyshev distance
    final double horVerEuc = 1.0; //horizontal and vertical distance values for euclidean distance
    final double diagEuc = 1.4; //diagonal distance value for euclidean distance

    static int arraySize; //size of randomlyGenMatrix array
    static  cell[][] cellBoard; //2d array which represents N*N grid


    ArrayList<cell> pathFinding(boolean[][] randomlyGenMatrix, int Ai, int Aj, int Bi, int Bj, String pathChoice) {

        arraySize = randomlyGenMatrix.length;

        //Creating starting and ending cell objects
             /*assigning user entered coordinates to the starting object*/
        startingPoint = new cell(Ai, Aj);
        endingPoint = new cell(Bi, Bj);
        cellBoard = new cell[arraySize][arraySize];
        
        
        //saving cells and blocked cells in a array
        for (int i = 0; i < arraySize; ++i) {
            for (int j = 0; j < arraySize; ++j) {
                cellBoard[i][j] = new cell(i, j);
                cellBoard[i][j].heuristicValue = calculateHeuristicVal(cellBoard[i][j].xCell,cellBoard[i][j].yCell, Bj, Bi, pathChoice);
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

        ArrayList<cell> closedList = new ArrayList<>(); //cells in the shortest path
        openList.add(startingPoint); //adding starting cell into the open list

        startingPoint.gValue = 0; //g value of the starting point is zero
        startingPoint.totalValue = 0; //setting zero to the total value
      
            while (!openList.isEmpty()) {
            currentPoint = openList.poll(); //cell with the lowest F cost
            if (currentPoint.yCell == Bi && currentPoint.xCell == Bj) { //if current point is final destination break the loop
                break;
            }
            java.util.List<cell>  neighbourList= adjecentCells(currentPoint, cellBoard, pathChoice);
            for (cell neighbour : neighbourList) {
                if (!closedList.contains(neighbour) && !neighbour.visitState && neighbour.gValue > (currentPoint.gValue + selectGValueD(pathChoice) )) { 
                                                                                             //conditions to skip a neighbouring cell
                        if( (neighbour.yCell == currentPoint.yCell-1 && neighbour.xCell == currentPoint.xCell-1) ||
                            (neighbour.yCell == currentPoint.yCell+1 && neighbour.xCell == currentPoint.xCell-1) || //checking dioganal cells
                            (neighbour.yCell == currentPoint.yCell-1 && neighbour.xCell == currentPoint.xCell+1) || 
                            (neighbour.yCell == currentPoint.yCell+1 && neighbour.xCell == currentPoint.xCell+1) ){
                           
                                  neighbour.gValue = currentPoint.gValue + selectGValueD(pathChoice); //g value of the neighbour
                                  neighbour.totalValue = neighbour.gValue + neighbour.heuristicValue; //final value of the neighbour
                                  
                                  neighbour.parentCell = currentPoint;//make current point parent of the neighbouring cell
                                  openList.add(neighbour); //add neighbour to the open list
                            
                        }else{
                                  //horizontal and vertical cells
                                  neighbour.gValue = currentPoint.gValue + selectGValueHV(pathChoice);//g value of the neighbour
                                  neighbour.totalValue = neighbour.gValue + neighbour.heuristicValue;//final value of the neighbour
                                 
                                  neighbour.parentCell = currentPoint;//make current point parent of the neighbouring cell
                                  openList.add(neighbour);//add neighbour to the open list
                        }
                }
            }
            //closedList.add(currentPoint);
            currentPoint.visitState = true;

        }      
        
                // Checking if a path exists
       if (!(cellBoard[endingPoint.yCell][endingPoint.xCell].gValue == Integer.MAX_VALUE)) {
            
            cell current = cellBoard[endingPoint.yCell][endingPoint.xCell];
            closedList.add(cellBoard[endingPoint.yCell][endingPoint.xCell]);

            //Trace back the path
            while (current.parentCell != null) {
              
                closedList.add(current.parentCell);
                current = current.parentCell;
                
                if(current.parentCell==startingPoint){
                    closedList.add(cellBoard[startingPoint.yCell][startingPoint.xCell]);
                    break;
                }
               
            }
        } else System.out.println("No possible path");



        
        return  closedList;
        }



    private java.util.List<cell> adjecentCells(cell currentPoint, cell[][] cellBoard, String pathC) {

        java.util.List<cell> adjacentCells = new ArrayList<>(); //arraylist for adjecent cells
        int cellBoardSize = cellBoard.length;
        
        if(pathC.equalsIgnoreCase("E") || pathC.equalsIgnoreCase("C")){
                    //top
                    if(currentPoint.yCell - 1 >= 0){
                            //top left
                            if ((currentPoint.xCell-1) >=0 && !cellBoard[currentPoint.yCell - 1][currentPoint.xCell-1].blockState == true) {
                                adjacentCells.add(cellBoard[currentPoint.yCell - 1][currentPoint.xCell-1]);
                            }
                            
                            //top right
                            if ((currentPoint.xCell+1) < cellBoardSize &&  !cellBoard[currentPoint.yCell - 1][currentPoint.xCell+1].blockState == true) {
                                adjacentCells.add(cellBoard[currentPoint.yCell - 1][currentPoint.xCell+1]);
                            }
                            
                    }
                                  
                    //bottom
                    if( (currentPoint.yCell + 1) < cellBoardSize){
                            //bottom left
                            if ((currentPoint.xCell-1) >=0 && !cellBoard[currentPoint.yCell + 1][currentPoint.xCell - 1].blockState == true) {
                                adjacentCells.add(cellBoard[currentPoint.yCell + 1][currentPoint.xCell-1]);
                            }
                            
                            //bottom right
                            if ((currentPoint.xCell+1) < cellBoardSize  && !cellBoard[currentPoint.yCell + 1][currentPoint.xCell +1].blockState == true) {
                                adjacentCells.add(cellBoard[currentPoint.yCell + 1][currentPoint.xCell+1]);
                            }        
                            
                    }
                           
        }
        
        //top
        if(currentPoint.yCell - 1 >= 0){
            
                    //top top    
                    if (!cellBoard[currentPoint.yCell - 1][currentPoint.xCell].blockState == true) {
                        adjacentCells.add(cellBoard[currentPoint.yCell - 1][currentPoint.xCell]);
                    }
        }

        //right
        if ((currentPoint.xCell + 1) < cellBoardSize && !cellBoard[currentPoint.yCell][currentPoint.xCell+1].blockState == true) {
            adjacentCells.add(cellBoard[currentPoint.yCell][currentPoint.xCell+1]);
        }
        //left
        if ((currentPoint.xCell - 1) >= 0 && !cellBoard[currentPoint.yCell][currentPoint.xCell-1].blockState == true) {
            adjacentCells.add(cellBoard[currentPoint.yCell][currentPoint.xCell-1]);
        }
        
        
        //bottom
        if( (currentPoint.yCell + 1) < cellBoardSize){
                     //bottom bottom
                    if (!cellBoard[currentPoint.yCell + 1][currentPoint.xCell].blockState == true) {
                        adjacentCells.add(cellBoard[currentPoint.yCell + 1][currentPoint.xCell]);
                    }
                       
        }
        return (java.util.List<cell>) adjacentCells;
    }
        
    //return heuristic value of a cell using three different metrics
    public double calculateHeuristicVal(int startX, int startY, int endX, int endY, String pathC){

        

        //calculating H value using Chebyshev metric
        if(pathC.equalsIgnoreCase("C")){

            double hTotalC = Math.max( Math.abs(endX - startX), Math.abs(endY - startY));//get the total manhattan heuristic value
            return hTotalC;
        }

        //calculating H value using Manhattan metric
        if(pathC.equalsIgnoreCase("M")){

            double hTotalM = Math.abs(endX - startX) + Math.abs(endY - startY);//get the total manhattan heuristic value
            return hTotalM;
            
        }
        
        //calculating H value using Euclidean metric
        if(pathC.equalsIgnoreCase("E")){

            double hTotalE; //total manhattan heuristic value
            hTotalE = Math.sqrt( (Math.pow(endX - startX, 2.0) ) + (Math.pow(endY - startY, 2.0)) ); //square root of the total
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

        if(pathC.equalsIgnoreCase("E")){
            return diagEuc;
        }
        if(pathC.equalsIgnoreCase("C"));
        return diagCheb;
    }

    static class cell{

        int yCell; //y coordinate of the cell
        int xCell; //x coordinate of the cell
        double gValue  =     Integer.MAX_VALUE; //g cost of a cell
        double heuristicValue = 0; //heuristic cost of a cell (h)
        double totalValue = 0; //total cost of a cell (g + h)
        boolean blockState; //blocked statement of a cell
        boolean visitState; //visited statement of a cell
        cell parentCell;

        cell(int yCo, int xCo){
            yCell = yCo;
            xCell = xCo;
        }

        public double getTotalValue(){
            return heuristicValue+gValue;
        }



    }
}

