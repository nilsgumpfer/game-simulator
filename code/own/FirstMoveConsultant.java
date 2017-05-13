package own;

import basic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 30.04.2017.
 */
public class FirstMoveConsultant extends AMoveConsultant
{
    @Override
    public void incorporateRivalMove(Move lastMove) {
        incorporateMove(lastMove, PlayerColor.Rival);
    }

    @Override
    public void incorporateOwnMove(Move lastMove) {
        incorporateMove(lastMove, PlayerColor.Own);
    }

    @Override
    public Move getBestPossibleMove(List<Move> possibleMoves) {
        // TODO: act like this: if own potentials present, grow them. if rival potentials present, block them. if only one of them present, act same.
        // consider blocking rival potentials only if critical (=3), block just at last moment, grow own potential from first point

        List<Integer> listOfPossibleColumns = MyHelper.extractPossibleColumnNumbers(possibleMoves);
        int selectedColumn = listOfPossibleColumns.get(0);

        // scan for all potentials of rival, planning to block them
        List<VirtualPattern> rivalPotential = scanHighPotentialPatterns(virtualGameBoard, PlayerColor.Rival);

        // scan for all own potentials, planning to grow them
        List<VirtualPattern> ownPotential = scanHighPotentialPatterns(virtualGameBoard, PlayerColor.Own);

        /******************************************************************************************************/
        /*
        // concat all patterns for displaying
        List<VirtualPattern> allPatterns = new ArrayList<>();
        allPatterns.addAll(rivalPotential);
        allPatterns.addAll(ownPotential);

        // print recognized patterns
        System.out.println("Recognized critical patterns: ");
        for (VirtualPattern virtualPattern:allPatterns) {
            System.out.println(virtualPattern);
        }*/
        /******************************************************************************************************/

        // if critical patterns/potentials found, block them (here: just first one) //TODO: make this smarter!
        if(rivalPotential.size() > 0)
            if(rivalPotential.get(0).getListOfGaps().size() > 0)
                selectedColumn = rivalPotential.get(0).getListOfGaps().get(0).getHorizontalPosition() + 1;

        return new Move(selectedColumn);
    }

    private List<VirtualPattern> scanHighPotentialPatterns(VirtualGameBoard virtualGameBoard, PlayerColor playerColor){
        // scan for all potentials
        List<VirtualPattern> allPotentials = PatternScanner.scanForAllPatternsForColor(virtualGameBoard, playerColor, true);

        // filter just high potentials, here: 3 and more
        //allPotentials = MyHelper.filterPotentials(allPotentials, 2); //TODO: just for testing

        // sort Potentials descending
        //allPotentials = MyHelper.sortPatternsOnPotentials(allPotentials); //TODO: just for testing

        return allPotentials;
    }
}
