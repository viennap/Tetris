import java.awt.Color; 

/**
 * Tetrad describes a tetris shape, consisting of four colored blocks.
 * 
 * @author Vienna Parnell
 * @version 3.17.20
 */
public class Tetrad
{
    private MyBoundedGrid<Block> grid; 
    private Block [] blocks; 
    private int rand; 
    private final static Color [] COLORS = {Color.RED, Color.GRAY, Color.CYAN, 
                                            Color.YELLOW,Color.MAGENTA, Color.BLUE, Color.GREEN};  
    private boolean isValid;                                                                                     
    /**
     * Constructs tetrad. 
     * 
     * @param gr     Grid that tetrad is placed into.
     */
    public Tetrad(MyBoundedGrid<Block> gr)
    {
        isValid = true; 
        rand = (int) (Math.random()*7);  
        blocks = new Block[4]; 
        for (int i = 0; i < 4; i++)
        {
            blocks[i] = new Block(); 
        }
        grid = gr; 
        Location [] locs = new Location[4]; 
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();  
        if (rand == 0) 
        {
            locs[0] = new Location(1, cols/2);
            locs[1] = new Location(0, cols/2);
            locs[2] = new Location(2, cols/2);
            locs[3] = new Location(3, cols/2);
        }
        else if (rand == 1)
        {
            locs[2] = new Location(0, (cols/2) - 1);
            locs[1] = new Location(0, (cols/2) + 1);
            locs[0] = new Location(0, cols/2);
            locs[3] = new Location(1, cols/2);
        }
        else if (rand == 2)
        {
            locs[0] = new Location(0, (cols/2) - 1);
            locs[1] = new Location(0, (cols/2));
            locs[2] = new Location(1, (cols/2) - 1); 
            locs[3] = new Location(1, cols/2); 
        }
        else if (rand == 3)
        {
            locs[1] = new Location(0, (cols/2)-1);
            locs[0] = new Location(1, (cols/2)-1);
            locs[2] = new Location(2, (cols/2)-1); 
            locs[3] = new Location(2, (cols/2)); 
        }
        else if (rand == 4)
        { 
            locs[1] = new Location(0, (cols/2)+1);
            locs[0] = new Location(1, (cols/2)+1);
            locs[2] = new Location(2, (cols/2)+1); 
            locs[3] = new Location(2, (cols/2)); 
        }
        else if (rand == 5)
        {
            locs[0] = new Location(0, cols/2);
            locs[1] = new Location(0, (cols/2) + 1);
            locs[2] = new Location(1, (cols/2)-1); 
            locs[3] = new Location(1, cols/2);  
        }
        else if (rand == 6)
        {
            locs[0] = new Location(0, cols/2);
            locs[1] = new Location(0, (cols/2)-1);
            locs[2] = new Location(1, (cols/2)); 
            locs[3] = new Location(1, (cols/2)+1);
        } 
        if (!areEmpty(grid,locs))
        {
            isValid = false; 
        }
        addToLocations(grid,locs);
    }
    
    /**
     * Returns whether or not isValid instance variable is true.
     * isValid indicates if tetrad shape is placed in valid locations
     * 
     * @return      true if instance variable isValid is true, otherwise;
     *              false
     */
    public boolean getValid()
    {
        return isValid; 
    }
    
    /**
     * Tetrad is added to grid at specified locations. 
     * 
     * @precondition     Tetrad blocks are not in any grid. 
     * @param gr       Grid that tetrad is placed into. 
     * @param locs       Location where tetrad will be placed. 
     * 
     */
    private void addToLocations(MyBoundedGrid<Block> gr, Location [] locs) 
    {
        for (int i = 0; i < blocks.length; i++) 
        {
            blocks[i].setColor(COLORS[rand]); 
            blocks[i].putSelfInGrid(grid, locs[i]); 
        }
    }
    
    /**
     * Removes tetrad blocks from the grid.
     *  
     * @return       Grid that tetrad is placed into. 
     */
    private Location [] removeBlocks()
    {
        Location [] locs = new Location[4]; 
        for (int i = 0; i < 4; i++)
        {
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid(); 
        }
        return locs; 
    }
    
    /**
     * Determines if locations are valid and empty. 
     * 
     * @param gr      Grid that tetrad is placed into. 
     * @param locs      Locations that tetrad is placed into. 
     * 
     * @return       true if locations are valid and empty; otherwise,
     *               false
     */
    private boolean areEmpty(MyBoundedGrid<Block> gr, Location [] locs)
    {
        for (int i = 0; i < locs.length; i++)
        {
            if (!grid.isValid(locs[i]) || grid.get(locs[i]) != null)
            {
                return false; 
            }
        }
        return true; 
    }
    
    /**
     * Determines if tetrad can be translated and does so, if valid. 
     * 
     * @param deltaRow       Number of units location's row is shifted.                     
     * @param deltaCol       Number of units location's column is shifted.        
     * 
     * @return       true if tetrad can be translated; otherwise,
     *               false
     */
    public boolean translate(int deltaRow, int deltaCol)
    {
        Location [] original = removeBlocks(); 
        Location [] shifted = new Location[4]; 
        for (int i = 0; i < 4; i++) 
        {
            int col = original[i].getCol(); 
            int row = original[i].getRow(); 
            col += deltaCol; 
            row += deltaRow; 
            Location loc = new Location(row,col); 
            shifted[i] = loc; 
        }
        if (areEmpty(grid, shifted))
        {
            addToLocations(grid, shifted); 
            return true; 
        }
        else 
        {
            addToLocations(grid, original); 
            return false; 
        }
    }
    
    /**
     * Rotates tetrid shape around central pivoting point. 
     * 
     * @return      true if shape can rotate; otherwise,
     *              false
     */
    public boolean rotate()
    {
        if (rand != 2)
        {
            Location [] original = removeBlocks(); 
            Location [] shifted = new Location[4]; 
            int col = original[0].getCol(); 
            int row = original[0].getRow(); 
            shifted[0] = new Location(row,col); 
            for (int i = 1; i < 4; i++)
            {
                shifted[i] = new Location(row-col+original[i].getCol(), 
                                          row+col-original[i].getRow()); 
            }
            if (areEmpty(grid, shifted))
            {
                addToLocations(grid,shifted); 
                return true; 
            }
            else
            {
                addToLocations(grid, original); 
                return false; 
            }
        }
        return false; 
    }
}
