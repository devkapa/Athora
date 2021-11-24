import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class AthoraMain {

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        out.println(AthoraAssets.welcome);

        TimeUnit.SECONDS.sleep(5);

        AthoraLogic.AwaitMovement();

    }

}