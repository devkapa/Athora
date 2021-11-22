import java.util.ArrayList;

public class AthoraScene {

    long id;
    String name;
    ArrayList<Long> directions;
    String setting;

    public AthoraScene(long id, String name, ArrayList<Long> directions, String setting) {
        this.id = id;
        this.name = name;
        this.directions = directions;
        this.setting = setting;
    }

    public long getId(){
        return id;
    }

}

