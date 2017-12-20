package com.company;

public class Main {

    public static void main(String[] args) {
        long st = System.currentTimeMillis();
        FruitRageGame3 game = new FruitRageGame3();
        game.start();
        long end = System.currentTimeMillis()-st;
        System.out.println("Time taken: "+end);
    }
}
