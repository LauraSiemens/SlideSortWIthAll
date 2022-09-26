package slidesort;

import java.util.ArrayList;;

import java.util.List;

public class Grid {
    private int[][] _grid;
    //stores a value where the index = the position it was stored in
    private int[] valueAtPosition;

    /**
     * Create a new grid
     * @param seedArray is not null
     *                  and seedArray.length > 0
     *                  and seedArray[0].length > 0
     */
    public Grid(int[][] seedArray) {
        int rows = seedArray.length;
        int cols = seedArray[0].length;
        _grid = new int[rows][cols];
        valueAtPosition = new int[rows*cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                _grid[i][j] = seedArray[i][j];
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Grid) {
            Grid g2 = (Grid) other;
            if (this._grid.length != g2._grid.length) {
                return false;
            }
            if (this._grid[0].length != g2._grid[0].length) {
                return false;
            }
            int rows = _grid.length;
            int cols = _grid[0].length;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (this._grid[i][j] != g2._grid[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check if this grid is a valid grid.
     * A grid is valid if, for c = min(rows, cols),
     * the grid contains zero or more values in [1, c]
     * exactly once and all other entries are 0s.
     *
     * @return true if this is a valid grid and false otherwise
     */
    public boolean isValid() {

        int m = _grid.length;
        int n = _grid[0].length;
        int min;

        if(m < n) {
            min = m;
        }
        else{
            min = n;
        }
        ArrayList check = new ArrayList();
        for(int i = 0; i < m; i++) {
            for( int j = 0; j < n; j++) {
                valueAtPosition[ i * n +j] = _grid[i][j];
                if(_grid[i][j] > min   || _grid[i][j] < 0) {
                    return false;
                }
                else if (check.contains(_grid[i][j]) && _grid[i][j] > 0) {
                    return false;
                }
                else {
                    check.add(_grid[i][j]);
                }
            }
        }
        return true;
    }

    /**
     * Check if this grid is sorted.
     * A grid is sorted iff it is valid and,
     *  for all pairs of entries (x, y)
     *  such that x > 0 and y > 0,
     *  if x < y then the position(x) < position(y).
     * If x is at location (i, j) in the grid
     * then position(x) = i * (number of cols) + j.
     *
     * @return true if the grid is sorted and false otherwise.
     */
    public boolean isSorted() {
        if(isValid() == false) {
            return false;
        }

        int m = _grid.length;
        int n = _grid[0].length;
        int x = 0;
        int xPosition = 0;
        int min;

        if(m < n) {
            min = m;
        }
        else{
            min = n;
        }

// find first non zero value (x)
// assign that value to the hashmap with key = position of x
// find next value (y) and if y>x yaayyyyyy
//x = y and find new y repeat
        for(int i = 0; i < valueAtPosition.length; i++) {
            if (valueAtPosition[i] != 0) {
                if (x == 0) {
                    x = valueAtPosition[i];
                    xPosition = i;
                }
                else if (x != 0) {
                    int y = valueAtPosition[i];
                    int yPosition = i;
                    if (x < y && xPosition < yPosition) {
                        x = y;
                        xPosition = yPosition;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Check if a list of moves is feasible.
     * A move is feasible if it starts with a non-zero entry,
     * does not move that number off the grid,
     * and it does not involve jumping over another non-zero number.
     *
     * @param   moveList is not null.
     * @return  true if the list of moves are all feasible
     *          and false otherwise.
     *          By definition an empty list is always feasible.
     */
    public boolean validMoves(List<Move> moveList) {
        if(isValid()) {
            int cols = _grid[0].length;
            int rows = _grid.length;
            for (Move checkMove : moveList) {
                //check that the starting position contains a non-zero value
                int startRow = checkMove.startingPosition.i;
                int startColumn = checkMove.startingPosition.j;
                int pos = startRow * cols + startColumn;
                if (valueAtPosition[pos] != 0) {
                    if (checkMove.rowMove == true) {
                        if (checkMove.displacement > 0 && checkMove.startingPosition.j + checkMove.displacement <= cols - 1) {
                            for (int j = startColumn; j < checkMove.displacement + startColumn; j++) {
                                if (valueAtPosition[pos + 1] == 0) {
                                    valueAtPosition[pos] = 0;
                                    valueAtPosition[pos + 1] = _grid[startRow][j];
                                    pos++;
                                } else {
                                    return false;
                                }
                            }
                        }
                        else if (checkMove.displacement < 0 && checkMove.startingPosition.j + checkMove.displacement <= 0) {
                            for (int j = startColumn; j > startColumn + checkMove.displacement; j--) {
                                if (valueAtPosition[pos - 1] == 0) {
                                    valueAtPosition[pos] = 0;
                                    valueAtPosition[pos - 1] = _grid[startRow][j];
                                    pos--;
                                }
                                else {
                                    return false;
                                }
                            }
                        }
                        else {return false;}
                    }
                    else {
                        if (checkMove.displacement > 0 && checkMove.startingPosition.i + checkMove.displacement <= rows - 1) {
                            for (int i = startRow; i < checkMove.displacement + startRow; i++) {
                                if (valueAtPosition[pos + cols] == 0) {
                                    valueAtPosition[pos] = 0;
                                    valueAtPosition[pos + cols] = _grid[i][startColumn];
                                    pos += cols;
                                } else {
                                    return false;
                                }
                            }
                        }
                        else if (checkMove.displacement < 0 && checkMove.startingPosition.i + checkMove.displacement <= 0) {
                            for (int i = startRow; i > checkMove.displacement + startRow; i--) {
                                if (valueAtPosition[pos - cols] == 0) {
                                    valueAtPosition[pos] = 0;
                                    valueAtPosition[pos - cols] = _grid[i][startColumn];
                                    pos -= cols;
                                }
                                else {
                                    return false;
                                }
                            }
                        }
                        else{return false;}
                    }
                }
                else{return false;}
                //if move is row move, check that j +mov displacement does not go off grid and that all positions
                // between j and j + displacement only contain 0.
                //if move is not row move, do the same thing but for i
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Apply the moves in moveList to this grid
     * @param moveList is a valid list of moves
     */
    public void applyMoves(List<Move> moveList) {
        int n = _grid[0].length;

        if(validMoves(moveList)){
            for(Move doMove : moveList) {
                if(doMove.rowMove) {
                    int num = _grid[doMove.startingPosition.i][doMove.startingPosition.j];
                    int col = doMove.startingPosition.j;
                    col += doMove.displacement;
                    _grid[doMove.startingPosition.i][doMove.startingPosition.j] = 0;
                    _grid[doMove.startingPosition.i][col] = num;
                }
                else{
                    int num = _grid[doMove.startingPosition.i][doMove.startingPosition.j];
                    int row = doMove.startingPosition.i;
                    row += doMove.displacement;
                    _grid[doMove.startingPosition.i][doMove.startingPosition.j] = 0;
                    _grid[row][doMove.startingPosition.j] = num;
                }
            }
        }
    }

    /**
     * Return a list of moves that, when applied, would convert this grid
     * to be sorted
     * @return a list of moves that would sort this grid
     */
    public List<Move> getSortingMoves() {
        // TODO: implement this method
        int m = _grid.length;
        int n = _grid[0].length;
        int min;
        boolean rowMove;
        List<Move> moveList = new ArrayList<Move>();
        List<Move> singleCheck = new ArrayList<>();
        int[][]_grid = this._grid;

        if(m < n) {
            min = m;
            rowMove = false;
        }
        else{
            min = n;
            rowMove = true;
        }
        if (isValid() == false) {
            return null;
        }

        while(isSorted() == false) {
            //to find non zero values, make an array of size min where index = num - 1
            // and the value stored at teh index is the row/colum number
            // make another array for index = num - 1 and valeu = position
            int[] rowOfVal = new int[min];
            int[] colOfVal = new int[min];
            int[] lineOfVal = new int[min];
            for(int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (_grid[i][j] != 0) {
                        rowOfVal[_grid[i][j] - 1] = i;
                        colOfVal[_grid[i][j] - 1] = j;
                        if(rowMove) {
                            lineOfVal[_grid[i][j] - 1] = j;
                        }
                        else {
                            lineOfVal[_grid[i][j] - 1] = i;
                        }
                    }
                }
            }

            for(int num = 1; num <= min; num++) {
                if(num - 1 != lineOfVal[num - 1]) {
                    Position startPos = new Position(rowOfVal[num - 1], colOfVal[num - 1]);
                    Move firstMove = new Move(startPos, rowMove, num - 1 - lineOfVal[num - 1]);
                    singleCheck.add(firstMove);
                    if(validMoves(singleCheck) == true) {
                        moveList.add(firstMove);
                        this.applyMoves(singleCheck);
                    }
                    singleCheck.clear();
                }
            }
            if(moveList.size() == 0) {
                rowMove ^= true;
            }
        }
        return moveList;
    }
    //number of rows or columns = values in the array
    //if value is not in teh row/column with the same number (j/i -1) = num
    //the displacemet will be num - current row/column
    //if the number cannot move in the direction you want, try to move in other direction, then try next number
}
