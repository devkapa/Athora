package athora.map;

public class AthoraDirection {

    public Integer id;
    public String message;
    public Integer health;

    /*
        Overloading constructors for each type of AthoraDirection.
        - AthoraDirection(Integer id) is used when a direction directly corresponds to the ID of a scene
        - AthoraDirection(String message) is used when a direction does not correspond to a scene and instead prints a message
        - AthoraDirection(String message, Integer health) is used when a direction does not correspond to a scene, prints a message and affects the health of the player
     */

    public AthoraDirection(Integer id){
        this.id = id;
    }

    public AthoraDirection(String message){
        this.message = message;
    }

    public AthoraDirection(String message, Integer health){
        this.message = message;
        this.health = health;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getId() {
        return id;
    }

    // Returns an integer value, representing the index of the provided direction
    public static int getIndex(String direction){
        switch(direction){
            case "north" -> {
                return 0;
            }
            case "east" -> {
                return 1;
            }
            case "south" -> {
                return 2;
            }
            case "west" -> {
                return 3;
            }
            case "up" -> {
                return 4;
            }
            case "down"-> {
                return 5;
            }
        }
        return -1;
    }

}
