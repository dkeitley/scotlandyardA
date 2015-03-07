/**
 * Created by om0000 on 23/02/15.
 */

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(
//                InitialisationTests.class,
//                MovesTests.class,
                ValidMovesTests.class);
//                RoundsTests.class,
//                MrXLocationsTests.class);
        int numFailures = result.getFailureCount();


        int failcount = 1;
        for (Failure failure : result.getFailures()) {
            System.out.println("-------");
            System.out.println("Failure ( " + failcount + "/" + numFailures + " ): ");
            System.out.println(" --> " + failure.getDescription());
            System.out.println(" --> " + failure.getMessage());
            failcount++;

        }

    }
}
