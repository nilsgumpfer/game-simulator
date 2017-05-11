package own;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 01.05.2017.
 */
public class PatternScanner {
    public static List<VirtualPattern> scanForAllPatterns(VirtualGameBoard virtualGameBoard, boolean onlyPotentials){
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        // TODO: GO ON HERE --> iterate through Array of Positions and check if neighbors are same-colored,
        // if so, follow this "direction" and count length of uninterrupted chain, but also consider potentials
        // in same direction (1-coin-gaps), consider only looking in one direction / only recognizing patterns once,
        // so maybe mark positions checked in one direction, e.g. vertical, then go on horizontal, etc.)

        // concat sub-results
        listOfVirtualPatterns.addAll(scanForAllVerticalPatterns(virtualGameBoard, onlyPotentials));
        listOfVirtualPatterns.addAll(scanForAllHorizontalPatterns(virtualGameBoard, onlyPotentials));
        listOfVirtualPatterns.addAll(scanForAllDiagonalPatterns(virtualGameBoard, onlyPotentials));

        return listOfVirtualPatterns;
    }

    public static List<VirtualPattern> scanForAllPatternsForColor(VirtualGameBoard virtualGameBoard, PlayerColor playerColor, boolean onlyPotentials){
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        // concat sub-results
        listOfVirtualPatterns.addAll(scanForVerticalPatternsForColor(virtualGameBoard, playerColor, onlyPotentials));
        listOfVirtualPatterns.addAll(scanForHorizontalPatternsForColor(virtualGameBoard, playerColor, onlyPotentials));
        listOfVirtualPatterns.addAll(scanForDiagonalPatternsForColor(virtualGameBoard, playerColor, onlyPotentials));

        return listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForAllVerticalPatterns(VirtualGameBoard virtualGameBoard, boolean onlyPotentials)
    {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();
        listOfVirtualPatterns.addAll(scanForVerticalPatternsForColor(virtualGameBoard,PlayerColor.Own, onlyPotentials));
        listOfVirtualPatterns.addAll(scanForVerticalPatternsForColor(virtualGameBoard,PlayerColor.Rival, onlyPotentials));

        return  listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForAllHorizontalPatterns(VirtualGameBoard virtualGameBoard, boolean onlyPotentials)
    {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        listOfVirtualPatterns.addAll(scanForHorizontalPatternsForColor(virtualGameBoard,PlayerColor.Own, onlyPotentials));
        listOfVirtualPatterns.addAll(scanForHorizontalPatternsForColor(virtualGameBoard,PlayerColor.Rival, onlyPotentials));

        return  listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForAllDiagonalPatterns(VirtualGameBoard virtualGameBoard, boolean onlyPotentials)
    {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        listOfVirtualPatterns.addAll(scanForDiagonalPatternsForColor(virtualGameBoard,PlayerColor.Own, onlyPotentials));
        listOfVirtualPatterns.addAll(scanForDiagonalPatternsForColor(virtualGameBoard,PlayerColor.Rival, onlyPotentials));

        return  listOfVirtualPatterns;
    }

    private static List<VirtualPattern> scanForDiagonalPatternsForColor(VirtualGameBoard virtualGameBoard, PlayerColor playerColor, boolean onlyPotentials) {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        listOfVirtualPatterns.addAll(scanForDiagonalPatternsForColorForDirection(virtualGameBoard,playerColor, onlyPotentials, ScanDirection.UpperLeftToLowerRight));
        listOfVirtualPatterns.addAll(scanForDiagonalPatternsForColorForDirection(virtualGameBoard,playerColor, onlyPotentials, ScanDirection.UpperRightToLowerLeft));

        return  listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForVerticalPatternsForColor(VirtualGameBoard virtualGameBoard, PlayerColor playerColor, boolean onlyPotentials)
    {
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

                                // if same color, chain
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
        MyHelper.flushPatternFlagsOfBoard(virtualGameBoard);

        return listOfVirtualPatterns;
    }

    public static  List<VirtualPattern> scanForHorizontalPatternsForColor(VirtualGameBoard virtualGameBoard, PlayerColor playerColor, boolean onlyPotentials)
    {
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();

        for (List<VirtualPosition> currentRow:virtualGameBoard.getListOfRows())
        {
            int iMax = currentRow.size() - 1;

            for (int i=0; i<=iMax; i++)
            {
                VirtualPosition currentPosition = currentRow.get(i);

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
                        VirtualPosition tmpNextPosition = null;

                        // go on only if position isn´t part of pattern already
                        if(currentPosition.isTmpIsPartOfHorizontalPattern())
                            goOn = false;

                        while(goOn)
                        {
                            // check if you´re already at the edge of your row
                            if (i + gap <= iMax) {
                                tmpNextPosition = currentRow.get(i + gap);

                                // if same color, chain
                                if(tmpNextPosition.getPlayerColor() == playerColor)
                                {
                                    // mark start-position as used
                                    currentPosition.setTmpIsPartOfHorizontalPattern(true);

                                    // mark found position as used
                                    tmpNextPosition.setTmpIsPartOfHorizontalPattern(true);

                                    // temporary save this found position as end-position of pattern
                                    patternEndPosition = tmpNextPosition;

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
                            // define pattern-type using chain-length and if potential is present
                            PatternType patternType = checkHorizontalPotential(virtualGameBoard, patternStartPosition, patternEndPosition, chainLength);

                            // generate new pattern-object and save it
                            listOfVirtualPatterns.add(new VirtualPattern(patternStartPosition, patternEndPosition, patternType, playerColor));
                        }
                    }
                }
            }
        }

        // reset all flags, previously set for temporary pattern recognition
        MyHelper.flushPatternFlagsOfBoard(virtualGameBoard);

        return listOfVirtualPatterns;
    }

    private static List<VirtualPattern> scanForDiagonalPatternsForColorForDirection(VirtualGameBoard virtualGameBoard, PlayerColor playerColor, boolean onlyPotentials, ScanDirection scanDirection)
    {
        List<VirtualPosition> listOfStartPositions = MyHelper.getStartPositionsForDirection(virtualGameBoard, scanDirection);
        List<VirtualPattern> listOfVirtualPatterns = new ArrayList<>();
        VirtualPosition[][] arrayOfPositions = virtualGameBoard.getArrayOfPositions();

        int sizeVertical = virtualGameBoard.getListOfRows().size();
        int sizeHorizontal = virtualGameBoard.getListOfColumns().size();

        for (VirtualPosition currentStartPosition:listOfStartPositions) {
            // start at position
            int iV = currentStartPosition.getVerticalPosition();
            int iH = currentStartPosition.getHorizontalPosition();

            // as long as position is not out of bounds, look for chains of equal colors heading from corner to corner or parallel
            while(MyHelper.checkPosition(iH,iV,sizeHorizontal-1, sizeVertical-1))
            {
                // get position by using indexes
                VirtualPosition currentPosition = arrayOfPositions[iH][iV];
                currentPosition.setPositionScanned(true);

                // look for correct color, then start scanning further
                if(currentPosition.getPlayerColor() == playerColor)
                {
                    // go on only if this position is not already part of before recognized pattern
                    if(currentPosition.isTmpIsPartOfDiagonalPattern() == false)
                    {
                        VirtualPosition patternStartPosition = currentPosition;
                        VirtualPosition patternEndPosition = null;
                        int gap = 1;
                        int iHGap = 0;
                        int iVGap = 0;
                        int chainLength = 1;
                        boolean goOn = true;
                        VirtualPosition tmpNextPosition = null;

                        // go on only if position isn´t part of pattern already
                        if(currentPosition.isTmpIsPartOfHorizontalPattern())
                            goOn = false;

                        while(goOn)
                        {
                            // increase to go down
                            iVGap = iV + gap;

                            if(scanDirection == ScanDirection.UpperLeftToLowerRight)
                                // increase to go right
                                iHGap = iH + gap;
                            else
                                // instead of increasing (going right), decrease and go left
                                iHGap = iH - gap;

                            // check if you´re already at the edge of your row
                            if (MyHelper.checkPosition(iHGap,iVGap,sizeHorizontal-1, sizeVertical-1))
                            {
                                tmpNextPosition = arrayOfPositions[iHGap][iVGap];

                                // if same color, chain
                                if(tmpNextPosition.getPlayerColor() == playerColor)
                                {
                                    // mark start-position as used
                                    currentPosition.setTmpIsPartOfDiagonalPattern(true);

                                    // mark found position as used
                                    tmpNextPosition.setTmpIsPartOfDiagonalPattern(true);

                                    // temporary save this found position as end-position of pattern
                                    patternEndPosition = tmpNextPosition;

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
                                // if end of bounds reached, break loop
                                goOn = false;
                            }

                            // increase gap-counter
                            gap++;

                            // print board for debugging
                            //System.out.println(virtualGameBoard);
                        }

                        // go on only if pattern was found
                        if(chainLength > 1)
                        {
                            // define pattern-type using chain-length and if potential is present
                            PatternType patternType = checkDiagonalPotential(virtualGameBoard, scanDirection, patternStartPosition, patternEndPosition, chainLength);

                            // generate new pattern-object and save it
                            listOfVirtualPatterns.add(new VirtualPattern(patternStartPosition, patternEndPosition, patternType, playerColor));
                        }
                    }
                }

                if(scanDirection == ScanDirection.UpperRightToLowerLeft)
                    iH--;
                else
                    iH++;

                iV++;
            }

            // reset all flags, previously set for temporary pattern recognition
            MyHelper.flushPatternFlagsOfBoard(virtualGameBoard);
        }

        return listOfVirtualPatterns;
    }

    private static PatternType checkDiagonalPotential(VirtualGameBoard virtualGameBoard, ScanDirection scanDirection, VirtualPosition patternStartPosition, VirtualPosition patternEndPosition, int chainLength)
    {
        switch(chainLength){
            case 2:
                if(scanDirection == ScanDirection.UpperLeftToLowerRight)
                    return PatternType.Diagonal_ullr_2;
                else
                    return PatternType.Diagonal_urll_2;
            case 3:
                if(scanDirection == ScanDirection.UpperLeftToLowerRight)
                    return PatternType.Diagonal_ullr_3;
                else
                    return PatternType.Diagonal_urll_3;
            default:
                return PatternType.Diagonal_UNKNOWN;
        }
    }


    public static PatternType checkHorizontalPotential(VirtualGameBoard virtualGameBoard, VirtualPosition patternStartPosition, VirtualPosition patternEndPosition, int patternSize){
        VirtualPosition[][] arrayOfPositions = virtualGameBoard.getArrayOfPositions();
        int checkPositionLeftV = patternStartPosition.getVerticalPosition();
        int checkPositionLeftH = patternStartPosition.getHorizontalPosition() - 1; // go on left to get neighbor
        int checkPositionRightV = patternEndPosition.getVerticalPosition();
        int checkPositionRightH = patternEndPosition.getHorizontalPosition() + 1; // go on right to get neighbor
        int maxV = virtualGameBoard.getListOfRows().size();
        int maxH = virtualGameBoard.getListOfColumns().size();
        boolean hasPotentialOnLeftSide = false;
        boolean hasPotentialOnRightSide = false;
        boolean hasDangerousPotentialOnLeftSide = false;
        boolean hasDangerousPotentialOnRightSide = false;
        PatternType patternType = null;
        PlayerColor playerColor = patternStartPosition.getPlayerColor();

        // ensure position has valid indexes
        if(checkPositionLeftH >= 0 && checkPositionLeftH < maxH && checkPositionLeftV >=0 && checkPositionLeftV < maxV)
        {
            // check potential on left side
            if(arrayOfPositions[checkPositionLeftH][checkPositionLeftV].getPlayerColor() == PlayerColor.Empty)
                hasPotentialOnLeftSide = true;
        }

        // ensure position has valid indexes
        if(checkPositionRightH >= 0 && checkPositionRightH < maxH && checkPositionRightV >=0 && checkPositionRightV < maxV)
        {
            // check potential on right side
            if(arrayOfPositions[checkPositionRightH][checkPositionRightV].getPlayerColor() == PlayerColor.Empty)
                hasPotentialOnRightSide = true;
        }

        // go on and check if dangerous one-coin gap is present
        checkPositionLeftH--;
        checkPositionRightH++;

        // ensure position has valid indexes
        if(checkPositionRightH >= 0 && checkPositionRightH < maxH && checkPositionRightV >=0 && checkPositionRightV < maxV)
        {
            // check dangerous potential on right side
            if(arrayOfPositions[checkPositionRightH][checkPositionRightV].getPlayerColor() == playerColor)
                hasDangerousPotentialOnRightSide = true;
        }

        if(checkPositionLeftH >= 0 && checkPositionLeftH < maxH && checkPositionLeftV >=0 && checkPositionLeftV < maxV)
        {
            // check potential on left side
            if(arrayOfPositions[checkPositionLeftH][checkPositionLeftV].getPlayerColor() == playerColor)
                hasDangerousPotentialOnLeftSide = true;
        }

        // define pattern-type using chain-length and if potential is present
        switch (patternSize)
        {
            case 2:
                if(hasPotentialOnLeftSide && hasPotentialOnRightSide)
                    if(hasDangerousPotentialOnLeftSide && hasDangerousPotentialOnRightSide)
                        patternType = PatternType.Horizontal_2_plr_d;
                    else
                        patternType = PatternType.Horizontal_2_plr;
                else if(hasPotentialOnLeftSide)
                    if(hasDangerousPotentialOnLeftSide)
                        patternType = PatternType.Horizontal_2_pl_d;
                    else
                        patternType = PatternType.Horizontal_2_pl;
                else if(hasPotentialOnRightSide)
                    if(hasDangerousPotentialOnRightSide)
                        patternType = PatternType.Horizontal_2_pr_d;
                    else
                        patternType = PatternType.Horizontal_2_pr;
                else
                    patternType = PatternType.Horizontal_2;
                break;
            case 3:
                if(hasPotentialOnLeftSide && hasPotentialOnRightSide)
                    patternType = PatternType.Horizontal_3_plr;
                else if(hasPotentialOnLeftSide)
                    patternType = PatternType.Horizontal_3_pl;
                else if(hasPotentialOnRightSide)
                    patternType = PatternType.Horizontal_3_pr;
                else
                    patternType = PatternType.Horizontal_3;
                break;
        }

        return patternType;
    }













































}
