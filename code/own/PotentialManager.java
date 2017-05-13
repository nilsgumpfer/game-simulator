package own;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 13.05.2017.
 */
public class PotentialManager {
    private static PotentialManager ourInstance = new PotentialManager();
    private List<VirtualPotential> virtualPotentialList = new ArrayList<>();

    private PotentialManager() {
    }

    public static PotentialManager getInstance() {
        return ourInstance;
    }

    public static void incorporatePotential(VirtualPotential virtualPotential)
    {
        getInstance().virtualPotentialList.add(virtualPotential);
        virtualPotential.getVirtualPosition().addPotentialScore(virtualPotential.getPotentialScore());
    }

    public static void incorporatePotentials(List<VirtualPotential> virtualPotentialList)
    {
        for(VirtualPotential virtualPotential:virtualPotentialList)
            incorporatePotential(virtualPotential);
    }

    public List<VirtualPotential> getVirtualPotentialList() {
        return virtualPotentialList;
    }

    public VirtualPotential getHighestPotential(VirtualGameBoard virtualGameBoard)
    {
        //TODO: run through virtual board and save highest scored position
        return null;
    }

    public void resetAllPotentialScores(VirtualGameBoard virtualGameBoard){
        //TODO: run through virtual board and reset all virtual positions regarding their score
    }
}
