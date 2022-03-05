package athora;

import athora.map.AthoraMap;

import java.io.IOException;

public class AthoraMain {

    public static void main(String[] args) {

        try {

            System.out.println(new String(AthoraMain.class.getResourceAsStream("/Athora.txt").readAllBytes()));

            AthoraLogic.startGame(AthoraMap.getMap(AthoraMap.chooseMap()));

        } catch (IOException e) {
            System.out.println("\nAthora ran into a problem. Error: \n" + e.getMessage());
        }

        System.out.println("\nPress 'Enter' key to exit.");

        System.console().readLine();

    }

}