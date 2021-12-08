package athora;

import athora.assets.AthoraAssets;

import static java.lang.System.out;

public class AthoraMain {

    public static void main(String[] args) {

        out.println(AthoraAssets.welcome);

        AthoraLogic.startGame();

    }

}