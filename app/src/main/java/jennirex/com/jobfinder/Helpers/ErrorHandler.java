package jennirex.com.jobfinder.Helpers;

/**
 * Created by Tupelo on 2/17/2019.
 */

public class ErrorHandler {

    public boolean getErrorMessage(String response){
        String [] err = response.split(" ");
        if (err.length <= 1){
            err = "server replied".split(" ");
        }
        if (!err[1].equalsIgnoreCase("Error")) {
            return false;
        } else {
            return true;
        }
    }
}
