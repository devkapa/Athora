package athora;

import athora.map.AthoraMap;

import java.io.IOException;

import static athora.assets.AthoraAssets.ANSI_RESET;

public class AthoraMain {

    public static void main(String[] args) {

        try {

            System.out.println(ANSI_RESET + new String(AthoraMain.class.getResourceAsStream("/Athora.txt").readAllBytes()));

            AthoraLogic.startGame(AthoraMap.getMap(AthoraMap.chooseMap()));

        } catch (IOException e) {
            System.out.println("\nAthora ran into a problem. Error: \n" + e.getMessage());
        }

        System.out.println(ANSI_RESET + "\nPress 'Enter' key to exit.");

        System.console().readLine();

    }

}