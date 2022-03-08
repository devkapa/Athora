package athora;

import athora.map.AthoraMap;

import java.io.File;

public class AthoraMain {

    public static void main(String[] args) {

        String title = """
                Hello. Welcome to
                                    
                       d8888 888    888
                      d88888 888    888
                     d88P888 888    888
                    d88P 888 888888 88888b.   .d88b.  888d888 8888b.
                   d88P  888 888    888 "88b d88""88b 888P"      "88b
                  d88P   888 888    888  888 888  888 888    .d888888
                 d8888888888 Y88b.  888  888 Y88..88P 888    888  888
                d88P     888  "Y888 888  888  "Y88P"  888    "Y888888
                
                """;

        System.out.println(title);

        if(args.length > 0) {
            File map = new File(args[0]);
            System.out.println("Loading map from file: " + map.getName());
            AthoraLogic.startGame(AthoraMap.getMap(map));
        } else {
            AthoraLogic.startGame(AthoraMap.getMap(AthoraMap.chooseMap()));
        }

        System.out.println("Press 'Enter' key to exit.");

        System.console().readLine();

    }

}