package com.kiba.twoupfx;

public class Coin {

    private final int HEADS = 0;
    private int twoFace;

    public int flip () {
        twoFace = (int) (Math.random() * 2);
        return twoFace;
    }

    public boolean isHeads(int coin) {
        return (coin == HEADS);
    }

    public String toString () {
        return (twoFace == HEADS) ? "Heads" : "Tails";
    }


}
