package athora;

import athora.map.AthoraMap;

import java.io.File;
import java.io.IOException;

import static athora.assets.AthoraAssets.ANSI_RESET;

public class AthoraMain {

    public static void main(String[] args) {

        try {

            System.out.println(ANSI_RESET + new String(AthoraMain.class.getResourceAsStream("/Athora.txt").readAllBytes()));

            if(args.length > 0) {
                File map = new File(args[0]);
                System.out.println("\nLoading map from file: " + map.getName());
                AthoraLogic.startGame(AthoraMap.getMap(map));
            } else {
                AthoraLogic.startGame(AthoraMap.getMap(AthoraMap.chooseMap()));
            }

        } catch (IOException e) {
            System.out.println("\nAthora ran into a problem. Error: \n" + e.getMessage());
        }

        System.out.println(ANSI_RESET + "\nPress 'Enter' key to exit.");

        System.console().readLine();

    }

}