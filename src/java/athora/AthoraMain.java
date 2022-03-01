package athora;

import athora.assets.AthoraAssets;

import static java.lang.System.out;

public class AthoraMain {

    public static void main(String[] args) {

        AthoraLogic.startGame();

        out.println("\nPress 'Enter' key to exit.");

        System.console().readLine();

    }

}