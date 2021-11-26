import java.util.ArrayList;

public class AthoraPlayer {

    long hp;
    ArrayList<String> inventory;

    public AthoraPlayer(long hp, ArrayList<String> inventory) {
        this.hp = hp;
        this.inventory = inventory;
    }

    public long getHp() {
        return hp;
    }

    public void changeHp(int amount){
        hp += amount;
    }

    public ArrayList<String> getInventory(){
        return inventory;
    }

}
