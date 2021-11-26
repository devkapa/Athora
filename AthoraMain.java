import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class AthoraMain {

    public static void main(String[] args) throws IOException, ParseException {

        out.println(AthoraAssets.welcome);

        AthoraLogic.AwaitMovement();

    }

}