package own;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 13.05.2017.
 */
public class PotentialManager {
    private static PotentialManager ourInstance = new PotentialManager();
    private static List<VirtualPotential> virtualPotentialList = new ArrayList<>();

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

    public static List<VirtualPotential> getVirtualPotentialList() {
        return PotentialManager.getInstance().virtualPotentialList;
    }

    public static VirtualPotential getHighestPotential(VirtualGameBoard virtualGameBoard)
    {
        //TODO: run through virtual board and save highest scored position
        return null;
    }

    public static void resetAllPotentialScores(VirtualGameBoard virtualGameBoard){
        //TODO: run through virtual board and reset all virtual positions regarding their score
    }

    public static List<VirtualPotential> getDirectPotentialsWithNoGap(){
        List<VirtualPotential> virtualPotentialList = new ArrayList<>();

        // filter only potentials which are directly positioned next to a pattern (no gaps!)
        // filter only potentials which have no gap underneath (so they are short-term-relevant)
        for(VirtualPotential virtualPotential : PotentialManager.getInstance().virtualPotentialList){
            if(virtualPotential.getDistanceFromPattern() == 1 && virtualPotential.getGapDepthUnderneathPosition() == 0)
                virtualPotentialList.add(virtualPotential);
        }

        return virtualPotentialList;
    }

    public static void reset(VirtualGameBoard virtualGameBoard) {
        resetAllPotentialScores(virtualGameBoard);
        virtualPotentialList.clear();
    }
}
