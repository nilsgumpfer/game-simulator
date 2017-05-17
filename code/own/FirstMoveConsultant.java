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

        MyHelper.chainLengthTreshold_Vertical = 1; // should patterns have at least 2 coins to be detected, etc.?
        MyHelper.chainLengthTreshold_Horizontal = 2; // should patterns have at least 2 coins to be detected, etc.?
        MyHelper.chainLengthTreshold_Diagonal = 2; // should patterns have at least 2 coins to be detected, etc.?

        List<Integer> listOfPossibleColumns = MyHelper.extractPossibleColumnNumbers(possibleMoves);

        // this is the standard-move: middle position of possible columns
        // TODO: this is currently dumb - consider sourcing this out to potential manager: he can look at game situation and position coins on existing ones, etc.
        Move finalMove = new Move(listOfPossibleColumns.get(listOfPossibleColumns.size()/2));

        // scan for all potentials of rival, planning to block them
        List<VirtualPattern> rivalPotentials = PatternScanner.scanForAllPatternsForColor(virtualGameBoard, PlayerColor.Rival, true);

        // scan for all own potentials, planning to grow them
        List<VirtualPattern> ownPotentials = PatternScanner.scanForAllPatternsForColor(virtualGameBoard, PlayerColor.Own, true);

        // focus on potentials that are relevant for this move, future: consider smart "building"
        PotentialManager.filterOutPotentialsWithGapGreaterThan(0);

        // let potentialManager chose the highest available potential
        VirtualPosition position = PotentialManager.getHighestPotential(virtualGameBoard);

        // check if sth. was found - if not, use std. position
        if(position != null)
            finalMove = position.generateMoveForThisPosition();

        virtualGameBoard.printScores();

        // cleanup to be ready for following requests in future
        PotentialManager.reset(virtualGameBoard);

        return finalMove;
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
