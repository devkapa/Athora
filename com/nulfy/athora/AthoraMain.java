package com.nulfy.athora;

import com.nulfy.athora.assets.AthoraAssets;

import java.io.InputStream;

import static java.lang.System.out;

public class AthoraMain {

    public static void main(String[] args) {

        out.println(AthoraAssets.welcome);

        AthoraLogic.startGame();

    }

}