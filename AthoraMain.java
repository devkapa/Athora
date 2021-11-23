import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class AthoraMain {

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        out.println("Hello. Welcome to Athora.\n" +
                "\n" +
                "The year is 2137. Renowned billionaire Felon Muks, CEO of Tesca, The Interesting Company and SpaceF, has mutilated the structure of society.\n" +
                "\n" +
                "With slavery rates skyrocketing, humanity prays in fear as the world crumbles beneath his evil fist...\n");

        TimeUnit.SECONDS.sleep(5);

        AthoraLogic.AwaitMovement();

    }

}