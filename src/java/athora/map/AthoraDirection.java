package athora.map;

public record AthoraDirection(int index, int value) {

    public int index() {
        return index;
    }

    public int value() {
        return value;
    }

    public static AthoraDirection getIndex(String direction){
        switch(direction){
            case "north" -> {
                return new AthoraDirection(0, 1);
            }
            case "east" -> {
                return new AthoraDirection(2, 1);
            }
            case "south" -> {
                return new AthoraDirection(0, -1);
            }
            case "west" -> {
                return new AthoraDirection(2, -1);
            }
            case "up" -> {
                return new AthoraDirection(1, 1);
            }
            case "down"-> {
                return new AthoraDirection(1, -1);
            }
        }
        return new AthoraDirection(0, 0);
    }

}
