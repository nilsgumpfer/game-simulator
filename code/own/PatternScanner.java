package own;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 01.05.2017.
 */
public class PatternScanner {
    public static List<VirtualPattern> scanForAllPatterns(VirtualGameBoard virtualGameBoard){
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        // TODO: GO ON HERE --> iterate through Array of Positions and check if neighbors are same-colored,
        // if so, follow this "direction" and count length of uninterrupted chain, but also consider potentials
        // in same direction (1-coin-gaps), consider only looking in one direction / only recognizing patterns once,
        // so maybe mark positions checked in one direction, e.g. vertical, then go on horizontal, etc.)

        // concat sub-results
        listOfVirtualPatterns.addAll(scanForAllVerticalPatterns(virtualGameBoard));
        //listOfVirtualPatterns.addAll(scanForAllHorizontalPatterns(virtualGameBoard));
        //listOfVirtualPatterns.addAll(scanForAllDiagonalPatterns(virtualGameBoard));

        return listOfVirtualPatterns;
    }

    public static void flushPatternFlagsOfBoard(VirtualGameBoard virtualGameBoard){
        for (List<VirtualPosition> currentColumn:virtualGameBoard.getListOfColumns()) {
            for (VirtualPosition virtualPosition:currentColumn) {
                virtualPosition.flushPatternFlags();
            }
        }
    }

    public static  List<VirtualPattern> scanForAllVerticalPatterns(VirtualGameBoard virtualGameBoard)
    {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();
        listOfVirtualPatterns.addAll(scanForVerticalPatternsForColor(virtualGameBoard,PlayerColor.Own, false));
        listOfVirtualPatterns.addAll(scanForVerticalPatternsForColor(virtualGameBoard,PlayerColor.Rival, false));

        return  listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForAllHorizontalPatterns(VirtualGameBoard virtualGameBoard) {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();
        listOfVirtualPatterns.addAll(scanForHorizontalPatternsForColor(virtualGameBoard,PlayerColor.Own));
        listOfVirtualPatterns.addAll(scanForHorizontalPatternsForColor(virtualGameBoard,PlayerColor.Rival));

        return  listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForAllDiagonalPatterns(VirtualGameBoard virtualGameBoard) {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();
        listOfVirtualPatterns.addAll(scanForDiagonalPatternsForColor(virtualGameBoard,PlayerColor.Own));
        listOfVirtualPatterns.addAll(scanForDiagonalPatternsForColor(virtualGameBoard,PlayerColor.Rival));

        return  listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForVerticalPatternsForColor(VirtualGameBoard virtualGameBoard, PlayerColor playerColor, boolean onlyPotentials) {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        for (List<VirtualPosition> currentColumn:virtualGameBoard.getListOfColumns())
        {
            int iMax = currentColumn.size() - 1;
            boolean hasPotential = true;

            for (int i=0; i<=iMax; i++)
            {
                VirtualPosition currentPosition = currentColumn.get(i);

                // potential is initially given, this means the space over the existing coins in this column is free,
                // so you can drop new ones and extend possible patterns, etc.
                // --> if a coin is found which makes it impossible to gain a winning-row, potential is lost
                if(i == 0 && currentPosition.getPlayerColor() != PlayerColor.Empty)
                    hasPotential = false;

                if(currentPosition.getPlayerColor() != playerColor && currentPosition.getPlayerColor() != PlayerColor.Empty)
                    hasPotential = false;

                // look for correct color, then start scanning further
                if(currentPosition.getPlayerColor() == playerColor)
                {
                    // go on only if this position is not already part of before recognized pattern
                    if(currentPosition.isTmpIsPartOfVerticalPattern() == false)
                    {
                        VirtualPosition patternStartPosition = currentPosition;
                        VirtualPosition patternEndPosition = null;
                        int gap = 1;
                        int chainLength = 1;
                        boolean goOn = true;
                        VirtualPosition tmpNeighborPosition = null;

                        // if potential is required for collection and not given, stop here
                        if(onlyPotentials && hasPotential == false)
                            goOn = false;

                        while(goOn)
                        {
                            // check if you´re already at the edge of your column
                            if (i + gap <= iMax) {
                                tmpNeighborPosition = currentColumn.get(i + gap);

                                if(tmpNeighborPosition.getPlayerColor() == playerColor)
                                {
                                    // mark start-position as used
                                    currentPosition.setTmpIsPartOfVerticalPattern(true);

                                    // mark found position as used
                                    tmpNeighborPosition.setTmpIsPartOfVerticalPattern(true);

                                    // temporary save this found position as end-position of pattern
                                    patternEndPosition = tmpNeighborPosition;

                                    // increase number of found occurences
                                    chainLength++;
                                }
                                else
                                {
                                    // if end of chain reached, break loop
                                    goOn = false;
                                }

                            }
                            else
                            {
                                // if end reached, break loop
                                goOn = false;
                            }

                            // increase gap-counter
                            gap++;
                        }

                        // go on only if pattern was found
                        if(chainLength > 1)
                        {
                            PatternType patternType = null;

                            // define pattern-type using chain-length and if potential is present
                            switch (chainLength)
                            {
                                case 2:
                                    if(hasPotential)
                                        patternType = PatternType.Vertical_2_pt;
                                    else
                                        patternType = PatternType.Vertical_2;
                                    break;
                                case 3:
                                    if(hasPotential)
                                        patternType = PatternType.Vertical_3_pt;
                                    else
                                        patternType = PatternType.Vertical_3;
                                    break;
                            }

                            listOfVirtualPatterns.add(new VirtualPattern(patternStartPosition, patternEndPosition, patternType, playerColor));
                        }
                    }
                }
            }
        }

        // reset all flags, previously set for temporary pattern recognition
        flushPatternFlagsOfBoard(virtualGameBoard);

        return listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForHorizontalPatternsForColor(VirtualGameBoard virtualGameBoard, PlayerColor playerColor) {
        return new ArrayList<>();
    }

    public static  List<VirtualPattern> scanForDiagonalPatternsForColor(VirtualGameBoard virtualGameBoard, PlayerColor playerColor) {
        return new ArrayList<>();
    }
}
