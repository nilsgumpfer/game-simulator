package own;

/**
 * Created by Nils on 13.05.2017.
 */
public class PotentialManager {
    private static PotentialManager ourInstance = new PotentialManager();

    public static PotentialManager getInstance() {
        return ourInstance;
    }

    private PotentialManager() {
    }
}
