package com.company;

import java.io.*;
import java.util.*;

public class FruitRageGame3 {
    private int n,p;
    private float time;
    private FileOutputStream out;
    private FileInputStream in;
    private int grid[][];
    private int copy[];
    private HashSet<Integer> s = new HashSet<>();
    private Queue<Positions> q = new LinkedList<>();
    private int bestMove[] = new int[2];
    private int prevC=0;
    private int GAME_DEPTH=3;
    private int st=0;
    long b = System.currentTimeMillis();
    long e=0;
    private long TIME=8*1000;
    //private long finaltime = 8*1000;
    int branch=1;
    private String input = "/Users/nishantrevur/Java Code/AI Assignment/Assignment 2/input_7.txt";
    public void start()
    {
        getInput();
        bestMove[0]=0;bestMove[1]=0;
        //printGrid();
        if(time<1000)
        {
            GAME_DEPTH=1;
        }
        else if(time<TIME)
        {
            TIME = (long)(time/2);
            if(TIME<1)
            {
                GAME_DEPTH = 1;
            }
        }
        findBestMove();
        char row,col;
        col = (char) (bestMove[0]+65);
        String a="";
        a+=col;a+=(bestMove[1]+1);
        makeMove(bestMove[0],bestMove[1],grid);
        printAnswerToFile(grid,out,a);

    }

    private ArrayList<Positions> findMoves(int fruitGrid[][])
    {
        int visited[][] = new int[n][n];
        ArrayList<Positions> similarMoves= new ArrayList<>();
        int no=0;
        for (int i=0; i<n; i++)
        {
            for (int j=0; j<n; j++)
            {
                if(fruitGrid[i][j]!=-11 && visited[i][j]!=1)
                {
                    trialMove(i,j,similarMoves,visited,fruitGrid);
                    no++;
                }
            }
        }
        return similarMoves;
    }

    private void trialMove(int row,int col, ArrayList<Positions> similarMoves, int[][] visited,int fruitGrid[][])
    {
        int currentFruit = fruitGrid[row][col];
        int x,y;
        Positions p = new Positions(row,col);
        q.add(new Positions(row,col));
        if((System.currentTimeMillis()-b)>TIME)
        {
            char r,c;
            c = (char) (bestMove[0]+65);
            String a="";
            a+=c;a+=(bestMove[1]+1);
            makeMove(bestMove[0],bestMove[1],grid);
            printAnswerToFile(grid,out,a);
            System.exit(0);
        }
        while(!q.isEmpty())
        {
            x = q.element().x;
            y = q.element().y;
            q.remove();
            for(int i=y; i<n; i++)
            {
                if(fruitGrid[x][i] == currentFruit && visited[x][i]!=1)
                {
                    visited[x][i] = 1;
                    p.size++;
                    if(x!=0 && fruitGrid[x-1][i]==currentFruit)
                    {
                        q.add(new Positions(x-1,i));
                    }
                    if(x!=n-1 && fruitGrid[x+1][i]==currentFruit)
                    {
                        q.add(new Positions(x+1,i));
                    }
                }
                else
                    break;
            }
            for(int i=y-1; i>-1; i--)
            {
                if(fruitGrid[x][i] == currentFruit && visited[x][i]!=1)
                {
                    visited[x][i] = 1;
                    p.size++;
                    if(x!=0 && fruitGrid[x-1][i]==currentFruit)
                    {
                        q.add(new Positions(x-1,i));
                    }
                    if(x!=n-1 && fruitGrid[x+1][i]==currentFruit)
                    {
                        q.add(new Positions(x+1,i));
                    }
                }
                else
                    break;
            }
        }
        similarMoves.add(p);
        q.clear();
    }

    private int makeMove(int row,int col,int fruitGrid[][])
    {
        int currentFruit = fruitGrid[row][col];
        int x,y;
        int score=0;
        q.add(new Positions(row,col));
        while(!q.isEmpty())
        {
            x = q.element().x;
            y = q.element().y;
            q.remove();
            for(int i=y; i<n; i++)
            {
                if(fruitGrid[x][i] == currentFruit)
                {
                    fruitGrid[x][i] = -11;
                    score++;
                    if(x!=0 && fruitGrid[x-1][i]==currentFruit && fruitGrid[x-1][i]!=-11)
                    {
                        q.add(new Positions(x-1,i));
                    }
                    if(x!=n-1 && fruitGrid[x+1][i]==currentFruit && fruitGrid[x+1][i]!=-11)
                    {
                        q.add(new Positions(x+1,i));
                    }
                }
                else
                    break;
            }
            for(int i=y-1; i>-1; i--)
            {
                if(fruitGrid[x][i] == currentFruit)
                {
                    fruitGrid[x][i] = -11;
                    score++;
                    if(x!=0 && fruitGrid[x-1][i]==currentFruit && fruitGrid[x-1][i]!=-11)
                    {
                        q.add(new Positions(x-1,i));
                    }
                    if(x!=n-1 && fruitGrid[x+1][i]==currentFruit && fruitGrid[x+1][i]!=-11)
                    {
                        q.add(new Positions(x+1,i));
                    }
                }
                else
                    break;
            }
            s.add(x);
        }
        q.clear();
        gravity2(fruitGrid);
        return score;
    }

    private void gravity2(int fruitGrid[][])
    {
        Iterator<Integer> i = s.iterator();
        while(i.hasNext())
        {
            int row = i.next();
            int s = fruitGrid[row].length-1;
            int d = fruitGrid[row].length-1;
            while (s >= 0) {
                if (fruitGrid[row][s] != -11) {
                    fruitGrid[row][d--] = fruitGrid[row][s];
                }
                s--;
            }
            while (d >= 0) fruitGrid[row][d--] = -11;
        }
        s.clear();
    }

    private void getInput()
    {
        try {
            in = new FileInputStream(input);
            out = new FileOutputStream("/Users/nishantrevur/Java Code/AI Assignment/Assignment 2/output.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            n = Integer.parseInt(br.readLine());
            p = Integer.parseInt(br.readLine());
            time = Float.parseFloat(br.readLine())*1000;
            String fileLine;
            char lineChar[];
            fileLine = br.readLine();
            copy = new int[n];
            grid = new int[n][n];
            for (int i = 0; i < n; i++) {
                lineChar = fileLine.toCharArray();
                for (int j = 0; j < n; j++) {
                    if(lineChar[j]!='*') {
                        grid[j][i] = Character.getNumericValue(lineChar[j]);
                    }
                    else {
                        grid[j][i] = -11;
                    }
                }
                fileLine = br.readLine();
                copy[i] = -11;
            }

        }catch(Exception e)
        {

        }
    }

    public void printAnswerToFile(int grid[][], FileOutputStream out,String a) {
        String t = "";
        try {
                out.write((a+"\n").getBytes());
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (grid[j][i] == -11)
                            t += "*";
                        else
                            t += grid[j][i];
                    }
                    t += "\n";
                    out.write(t.getBytes());
                    t = "";
                }
            out.close();
        } catch (IOException e) {

        }
    }

    private int max(int a,int b)
    {
        if(a>b)
            return a;
        else
            return b;
    }

    private int max(int a,int b,int x,int y)
    {
        if(a>b)
        {
            return a;
        }
        else
        {
            bestMove[0]=x;
            bestMove[1]=y;
            return b;
        }
    }

    private int min(int a,int b,int x,int y)
    {
        if(a<b)
        {
            return a;
        }
        else
        {
            return b;
        }
    }

    private void findBestMove()
    {
        int alpha,beta;
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
        int value;
        int max=0,min=0;
        ArrayList<Positions> moves;
        moves = findMoves(grid);
        branch = moves.size()*branch;
        Iterator<Positions> i = moves.iterator();
        int score = 0;
        Positions p;
        int c=0;
        while (i.hasNext()) {
            value = Integer.MIN_VALUE;
            max = 0;
            int grid[][] = new int[n][n];
            copyGrid(grid, this.grid);
            p = largestMove(moves);
            //p = i.next();
            if(p==null)
                return;
            score = makeMove(p.x,p.y,grid);
            max = max + (score*score);
            value = minimax(alpha, beta, false, max, min, 1, grid);
            if (value > alpha) {
                bestMove[0] = p.x;
                bestMove[1] = p.y;
                alpha = value;
            }
            moves.remove(p);
        }
    }

    private Positions largestMove(ArrayList<Positions> p)
    {
        Iterator<Positions> i = p.iterator();
        Positions pos,t;
        int size=0;
        pos = null;
        if(p.size()==1)
            return p.iterator().next();
        else if(p.size()==0)
            return null;
        for(int j=0;j<p.size();j++)
        {
            if(p.get(j).size>size)
            {
                pos = p.get(j);
                size = pos.size;
            }
        }
        return pos;
    }

    private int minimax(int alpha,int beta, boolean maxPlayer,int max,int min,int depth,int fruitGrid[][])
    {
        int value;
        ArrayList<Positions> moves;
        moves = findMoves(fruitGrid);
        Iterator<Positions> i = moves.iterator();
        Positions p;
        branch = moves.size()*branch;
        if((System.currentTimeMillis()-b)>TIME)
        {
            char r,c;
            c = (char) (bestMove[0]+65);
            String a="";
            a+=c;a+=(bestMove[1]+1);
            makeMove(bestMove[0],bestMove[1],grid);
            printAnswerToFile(grid,out,a);
            System.exit(0);
        }
        st++;
        if(depth == GAME_DEPTH)
        {
            return (max-min);
        }
        if(moves.size()==0)
        {
            return (max-min);
        }
        if(maxPlayer)
        {
            value = Integer.MIN_VALUE;
            while(i.hasNext())
            {

                int grid[][] = new int[n][n];
                int score=0;
                copyGrid(grid,fruitGrid);
                p = largestMove(moves);
                //p = i.next();
                if(p==null)
                    return value;
                score = makeMove(p.x,p.y,grid);
                max = max + (score*score);
                value = minimax(alpha,beta,false,max,min,depth+1,grid);
                if(value>=beta){
                    return value;
                }
                moves.remove(p);
                max = max - (score*score);
                alpha = max(alpha,value);
                value = alpha;
            }
        }
        else
        {
            value = Integer.MAX_VALUE;
            while(i.hasNext())
            {
                int grid[][] = new int[n][n];
                int score = 0;
                copyGrid(grid,fruitGrid);
                p = largestMove(moves);
                //p = i.next();
                if(p==null)
                    return value;
                score = makeMove(p.x,p.y,grid);
                min = min +  (score*score);
                value = minimax(alpha,beta,true,max,min,depth+1,grid);
                if(value<=alpha)
                {
                    return value;
                }
                moves.remove(p);
                min = min - (score*score);
                beta = min(value,beta,p.x,p.y);
                value = beta;
            }

        }
        return value;
    }

    private void copyGrid(int g[][],int c[][])
    {
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                g[i][j]=c[i][j];
            }
        }
    }
    private void printGrid()
    {
        int c=0;
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(grid[j][i]==-11) {
                    c++;
                    System.out.print("*  ");
                }
                else
                    System.out.print(grid[j][i]+"  ");
            }
            System.out.println();
        }
        System.out.println(c-prevC);
        prevC = c;
        System.out.println("----------------------");
    }
}

class Positions implements Comparator<Positions>
{
    int x,y;
    int size;
    public Positions(int row,int col)
    {
        x=row;
        y=col;
    }

    @Override
    public int compare(Positions o1, Positions o2) {
        return o2.size-o1.size;
    }
}
