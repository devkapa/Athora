package athora.map;

public class AthoraDirection {

    public Integer id;
    public String message;
    public Integer health;

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

    public String getMessage() {
        return message;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getId() {
        return id;
    }

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
