/*
* Name: Jagannathan, Harshavardhan
* ID: 54805
* Lab: Recursion Lab 2
*/

package recursionlab2;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

public class RecursionLab2 
{
    public static void main(String[] args) 
    {
        MazeGUI maze = new MazeGUI();
    }
}

class MazeGUI 
{
    JFrame frame;
    JButton[] button;
    int numRows, numCols;
    private final int delay = 50;
    private String mazeFile = "maze.txt";
    private char[][] grid;      // 2D char array
    private int[] start;        // starting square {row, col}
    private int[] exit;         // exiting square {row, col}
    private char wall = 'X';    // character that represents a wall
    private char crumb = 'o';   // breadcrumb used to mark the path

    public MazeGUI() {
        start = new int[2];     // start[0] = row, start[1] = col
        exit = new int[2];      // exit[0]  = row, exit[1]  = col
        readMaze(mazeFile);     // load maze file
        createGUI(grid, start, exit, wall);
        
        if(!(findPath(start[0], start[1], false)))
            System.out.println("No Path Found");
    }

    /**
     * The findPath method will recursively find a path through the maze. You
     * should mark your path with a crumb as you go. You need to remove crumbs
     * and replace them with spaces as you back out from dead ends. Call
     * updateGUI(grid) anytime you place or remove a crumb. The base condition
     * will be reached when you hit a dead end or when row==exit[0] and
     * col==exit[1]. You can use pathFound as one of your base conditions.
     */
    public final boolean findPath(int row, int col, boolean pathFound) 
    {

        grid[row][col] = crumb;
        updateGUI();
        
        
        
        if (row == exit[0] && col == exit[1]) 
        {
            pathFound = true;
            return pathFound;
        }

        if(!pathFound&&col<numCols-1&&grid[row][col+1]==' ')
        {
            pathFound = findPath(row, col+1, pathFound);
        }
        if(!pathFound&&row>0&&grid[row-1][col]==' ')
        {
            pathFound = findPath(row-1, col, pathFound);
        }
        if(!pathFound&&row<numRows-1&&grid[row+1][col]==' ')
        {
            pathFound = findPath(row+1, col, pathFound);
        }
        if(!pathFound&&col>0&&grid[row][col-1]==' ')
        {
            pathFound = findPath(row, col-1, pathFound);
        }
        
        if(!pathFound)
        {
            grid[row][col] = ' ';
            updateGUI();
        }
        return pathFound;
    }

    /**
     * The findOpenings method finds the two openings (start & exit) in the
     * maze. The start and exit are int arrays with a size of 2. Your algorithm
     * should randomly choose which of the two openings is the start and which
     * is the exit. There will be only two openings and they can be on any of
     * the four sides. There will NOT be an opening in a corner.
     */
    public void findOpenings() 
    {
        boolean flag = false;
        for(int a = 0; a<grid[0].length; a++)
        {
            if(flag&&grid[0][a]==' ')
            {
                exit[0] = 0;
                exit[1] = a;
            }
            else if(grid[0][a]==' ')
            {
                start[0] = 0;
                start[1] = a;
                flag = true;
            }
        }
        for(int b = 0; b<grid.length; b++)
        {
            if(flag&&grid[b][0] == ' ')
            {
                exit[0] = b;
                exit[1] = 0;
            }
            else if(grid[b][0] == ' ')
            {
                start[0] = b;
                start[1] = 0;
                flag = true;
            }
        }
        for(int c=0; c<grid[grid.length-1].length; c++)
        {
            if(flag&&grid[grid.length-1][c]==' ')
            {
                exit[0] = grid.length-1;
                exit[1] = c;
            }
            else if(grid[grid.length-1][c]==' ')
            {
                start[0] = grid.length-1;
                start[1] = c;
                flag = true;
            }
        }
        for(int d=0; d<grid.length; d++)
        {
            if(flag&&grid[d][grid[d].length-1]==' ')
            {
                exit[0] = d;
                exit[1] = grid[d].length-1;
            }
            else if(grid[d][grid[d].length-1]==' ')
            {
                start[0] = d;
                start[1] = grid[d].length-1;
                flag = true;
            }
        }
        if((int)(Math.random()*2)==1)
        {
            int temp[] = start;
            start = exit;
            exit = temp;
            
        }
    }

    public final void readMaze(String file) 
    {
        try 
        {
            Scanner read = new Scanner(new File(file));
            int rows = read.nextInt();
            grid = new char[rows][];
            read.nextLine();
            for (int i = 0; i < rows; i++) 
            {
                grid[i] = read.nextLine().toCharArray();
            }
            numRows = grid.length;
            numCols = grid[0].length;
            findOpenings();
        } catch (FileNotFoundException ex) 
        {
            System.out.println("Error reading file: " + file);
        }
    }

    public final void createGUI(char[][] grid, int[] start, int[] exit, char c) {
        frame = new JFrame("Maze");
        button = new JButton[numRows * numCols];
        for (int i = 0; i < button.length; i++) {
            button[i] = new JButton("");
            button[i].setFont(new Font("Arial", Font.BOLD, 20));
            button[i].setBackground(new Color(255, 200, 150));
            if (grid[i / numCols][i % numCols] == wall) {
                button[i].setBackground(new Color(0, 25, 0));
            }
            frame.add(button[i]);
        }
        button[start[0] * numCols + start[1]].setOpaque(true);
        button[start[0] * numCols + start[1]].setBackground(new Color(0, 255, 0));
        button[exit[0] * numCols + exit[1]].setOpaque(true);
        button[exit[0] * numCols + exit[1]].setBackground(new Color(255, 0, 0));
        frame.setLayout(new GridLayout(numRows, numCols));
        frame.setSize(numCols * 45 + 45, numRows * 45 + 45);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public final void updateGUI() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                button[row * numCols + col].setText("" + grid[row][col]);
            }
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
        }
    }
}