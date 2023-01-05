
/**
 * Tetris is a game involving shifting and stacking 
 * shapes consisting of four-colored blocks
 * 
 * @author Vienna Parnell
 * @version 3.17.20
 */
public class Tetris implements ArrowListener 
{
    private MyBoundedGrid<Block>blocks;
    private static BlockDisplay display; 
    private Tetrad activeTetrad;
    private static int score; 
    private int speed; 
    private static boolean play; 
    
    /**
     * Constructs tetris board. 
     */
    public Tetris()
    {
        blocks = new MyBoundedGrid(20, 10); 
        display = new BlockDisplay(blocks); 
        display.setTitle("Tetris"); 
        activeTetrad = new Tetrad(blocks);
        display.setArrowListener(this); 
        display.showBlocks();
        score = 0; 
        speed = 1000; 
        play = true;
    }

    /**
     * Moves tetris shape up one block.
     */
    public void upPressed()
    {
        if (activeTetrad.rotate())
        {
            display.showBlocks(); 
        }
    }
    
    /**
     * Moves tetris down one block. 
     */
    public void downPressed()
    {
        if (activeTetrad.translate(1, 0))
        {
            display.showBlocks(); 
        }
    }
    
    /**
     * Moves tetris left one block. 
     */
    public void leftPressed()
    {
        if (activeTetrad.translate(0, -1))
        {
            display.showBlocks(); 
        }
    }
    
    /** 
     * Moves tetris right one block. 
     */
    public void rightPressed()
    {
        if (activeTetrad.translate(0, 1))
        {
            display.showBlocks(); 
        }
    }
    
    /**
     * Drops tetris shape to the bottom. 
     */
    public void spacePressed()
    {
        while (activeTetrad.translate(1,0))
        {
            display.showBlocks();
        }
    }
    
    /**
     * Shifts tetrad downward; when tetris can no longer move, new 
     * tetrad shape appears. 
     * 
     */
    public void play()
    {
        display.setScore(score); 
       
        while (activeTetrad.translate(1,0))
        {
            try
            {
                Thread.sleep(speed);
            }
            catch(InterruptedException e)
            {
                //
            }      
            display.showBlocks(); 
        }
        clearCompletedRows(); 
        Tetrad temp = new Tetrad(blocks);
        if (!temp.getValid())
        {
            play = false; 
            System.out.println("GAME OVER, YOU LOST!"); 
        }
        activeTetrad = temp; 
    }
    
    /**
     * Determines if row is horizontally filled with blocks.
     * 
     * @param row   the row that is being evaluated
     * 
     * @precondition    row is in the range [0, number of rows)
     * 
     * @return  true if row is completed; otherwise,
     *          false
     */
    private boolean isCompletedRow (int row) 
    {
        for (int c = 0; c < blocks.getNumCols(); c++) 
        {
            Location loc = new Location (row, c); 
            if (blocks.get(loc) == null)
                return false; 
        }
        return true; 
    }
    
    /**
     * Clears all the blocks that are horizontally in a row. 
     * 
     * @param row   the row that is being cleared
     * 
     * @precondition    row is completed
     * @precondition    row is in the range [0, number of rows)
     * 
     */
    private void clearRow(int row)
    {
        for (int c = 0; c < blocks.getNumCols(); c++)
        {
            Location loc = new Location (row,c); 
            blocks.get(loc).removeSelfFromGrid(); 
        }
        for (int r = row-1; r >= 0; r--)
        {
            for (int c = 0; c < blocks.getNumCols(); c++)
            {
                Location loc = new Location (r,c); 
                if (blocks.get(loc) != null)
                {
                    blocks.get(loc).moveTo(new Location (r+1,c)); 
                }
            }
        }
    }
    
    /**
     * Clears all horizontally completed rows. 
     */
    private void clearCompletedRows()
    {
        for (int r=0; r<blocks.getNumRows(); r++)
        {
            if (isCompletedRow(r))
            {
                clearRow(r);
                score++;
                speed-=75; 
            }
        }   
    }
    
    /**
     * Removes all blocks from the tetris board. 
     */
    public void clearBoard()
    {
        for (int c = 0; c < blocks.getNumCols(); c++)
        {
            for (int r = 0; r < blocks.getNumRows(); r++)
            {
                Location loc = new Location(r,c); 
                if (blocks.get(loc) != null)
                    blocks.remove(loc); 
            }
        }
    }
    
    /**
     * Determines if user has lost the game. 
     * 
     * @return  true if user has lost; otherwise,
     *          false
     */
    public boolean hasLost()
    {
        for (int c = 4; c <= 5; c++)
        {
            Location loc = new Location(0,c); 
            if (blocks.get(loc) != null)
                return true; 
        }
        return false; 
    }

    /**
     * Creates tetris game. 
     * 
     * @param args      arguments from the command line
     */
    public static void main (String [] args) 
    {
        Tetris t = new Tetris();
        while (play == true && score < 10)
            t.play(); 
        display.setScore(score);
        t.clearBoard();
        if (play == true)
            System.out.println("YOU WON!"); 
    }
}
