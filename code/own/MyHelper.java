package own;

import basic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 30.04.2017.
 */
public class MyHelper {
    public static int extractColumnIndex(Move move)
    {
        if(move != null) {
            String extractedDigit = move.toString().replaceAll("\\D+", "");
            int columnIndex = Integer.parseInt(extractedDigit) - 1; // -1 because it´s not 0-based

            return columnIndex;
        }
        else
            return -1;
    }

    public static List<Integer> extractPossibleColumnNumbers(List<Move> moveList)
    {
        List<Integer>  listOfPossibleColumns = new ArrayList<>();

        for (Move move:moveList) {
            listOfPossibleColumns.add(extractColumnIndex(move) + 1); // +1 because it´s not 0-based
        }

        return listOfPossibleColumns;
    }

    public static List<VirtualPattern> filterPotentials(List<VirtualPattern> virtualPatternList, int threshold)
    {
        List<VirtualPattern> resultList = new ArrayList<>();

        for (VirtualPattern virtualPattern:virtualPatternList){
            if(scorePotentialOfPattern(virtualPattern) > threshold)
                resultList.add(virtualPattern);
        }

        return resultList;
    }

    public static int scorePotentialOfPattern(VirtualPattern virtualPattern)
    {
        switch(virtualPattern.getPatternType())
        {
            // 2-chained, uncritical
            case Vertical_2:
                return 0;
            case Horizontal_2:
                return 0;

            // 3-chained, uncritical
            case Vertical_3:
                return 0;
            case Horizontal_3:
                return 0;

            // 2-chained, potentially critical
            case Vertical_2_pt:
                return 1;
            case Horizontal_2_pr:
                return 1;
            case Horizontal_2_pl:
                return 1;

            // 2-chained, potentially critical with double likelihood
            case Horizontal_2_plr:
                return 2;

            // 3-chained, critical --> one coin adds to four
            case Vertical_3_pt:
                return 3;
            case Horizontal_3_pr:
                return 3;
            case Horizontal_3_pl:
                return 3;


            // 2-chained, with gap (= 3-chained) --> one coin adds to four
            case Horizontal_2_pl_d:
                return 3;
            case Horizontal_2_pr_d:
                return 3;

            // 2-chained, with gap (= 3-chained) --> one coin adds to four, double likelihood
            case Horizontal_2_plr_d:
                return 4;


            // 3-chained --> one coin adds to four
            case Horizontal_3_plr:
                return 4; //worst case! --> if rival acts smart, he´ll win




            case Diagonal_2_hr:
                return -1;
            case Diagonal_2_hl:
                return -1;
            case Diagonal_3_hr:
                return -1;
            case Diagonal_3_hl:
                return -1;




            default:
                return -1;
        }
    }

    public static List<VirtualPattern> sortPatternsOnPotentials(List<VirtualPattern> virtualPatternList){
        virtualPatternList.sort( (VirtualPattern o1, VirtualPattern o2) -> o2.getPotentialRating() - o1.getPotentialRating()); // lambda-expression for sorting/comparing descending

        return virtualPatternList;
    }

    // dispatcher for read of gaps: routes requests according to pattern type
    public static List<VirtualPotential> scanPotentialsOfPattern(VirtualPattern virtualPattern)
    {
        List<VirtualPotential> virtualPotentialList = new ArrayList<>();

        // scan former scan-direction for pattern-recognition to read potentials
        virtualPotentialList.addAll(followDirectionAndScanPotentials(virtualPattern.getStartPosition(), virtualPattern.getVirtualGameBoard(), virtualPattern.getScanDirection()));

        // scan also opposite of former scan-direction for pattern-recognition to read potentials
        virtualPotentialList.addAll(followDirectionAndScanPotentials(virtualPattern.getStartPosition(), virtualPattern.getVirtualGameBoard(), getOppositeDirection(virtualPattern.getScanDirection())));

        return virtualPotentialList;
    }

    private static List<VirtualPotential> followDirectionAndScanPotentials(VirtualPosition startPosition, VirtualGameBoard virtualGameBoard, ScanDirection scanDirection) {
        List<VirtualPotential> virtualPotentialList = new ArrayList<>();

        VirtualPosition[][] arrayOfPositions = virtualGameBoard.getArrayOfPositions();

        int sizeHorizontal  = virtualGameBoard.getListOfColumns().size();
        int sizeVertical    = virtualGameBoard.getListOfRows().size();
        int iV              = startPosition.getVerticalPosition();
        int iH              = startPosition.getHorizontalPosition();

        iV = nextVerticalIndex(iV,scanDirection);
        iH = nextHorizontalIndex(iH,scanDirection);

        VirtualPosition currentPosition = arrayOfPositions[iH][iV];

        while(MyHelper.checkPositionStillInBounds(iH,iV,sizeHorizontal-1, sizeVertical-1))
        {
            if(currentPosition.getPlayerColor() == PlayerColor.Empty)
                virtualPotentialList.add(new VirtualPotential(scanDirection,currentPosition,getGapDepthUnderneathPosition(virtualGameBoard,currentPosition)));
            else
                break;

            iV = nextVerticalIndex(iV,scanDirection);
            iH = nextHorizontalIndex(iH,scanDirection);
        }

        return virtualPotentialList;
    }

    private static int getGapDepthUnderneathPosition(VirtualGameBoard virtualGameBoard, VirtualPosition startPosition)
    {
        VirtualPosition[][] arrayOfPositions = virtualGameBoard.getArrayOfPositions();

        int depthCounter    = 0;
        int sizeHorizontal  = virtualGameBoard.getListOfColumns().size();
        int sizeVertical    = virtualGameBoard.getListOfRows().size();
        int iV              = startPosition.getVerticalPosition() + 1; // one position underneath
        int iH              = startPosition.getHorizontalPosition();

        VirtualPosition currentPosition = arrayOfPositions[iH][iV];

        while(MyHelper.checkPositionStillInBounds(iH,iV,sizeHorizontal-1, sizeVertical-1))
        {
            if(currentPosition.getPlayerColor() == PlayerColor.Empty)
                depthCounter++;
            else
                break;

            iV++; // go deeper
        }

        return depthCounter;
    }

    private static int nextVerticalIndex(int currentIndex, ScanDirection scanDirection)
    {
        switch (scanDirection)
        {
            case UpperLeftToLowerRight:
                return ++currentIndex;
            case LeftToRight:
                return ++currentIndex;
            case TopToBottom:
                return ++currentIndex;
            case LowerRightToUpperLeft:
                return --currentIndex;
            case RightToLeft:
                return --currentIndex;
            case BottomToTop:
                return --currentIndex;
            case LowerLeftToUpperRight:
                return --currentIndex;
            case UpperRightToLowerLeft:
                return ++currentIndex;
            default:
                return currentIndex; // this case should never happen
        }
    }

    private static int nextHorizontalIndex(int currentIndex, ScanDirection scanDirection)
    {
        switch (scanDirection)
        {
            case UpperLeftToLowerRight:
                return ++currentIndex;
            case LeftToRight:
                return ++currentIndex;
            case TopToBottom:
                return ++currentIndex;
            case LowerRightToUpperLeft:
                return --currentIndex;
            case RightToLeft:
                return --currentIndex;
            case BottomToTop:
                return --currentIndex;
            case LowerLeftToUpperRight:
                return --currentIndex;
            case UpperRightToLowerLeft:
                return ++currentIndex;
            default:
                return currentIndex; // this case should never happen
        }
    }

    private static ScanDirection getOppositeDirection(ScanDirection scanDirection){
        switch (scanDirection) {
            case UpperLeftToLowerRight:
                return ScanDirection.LowerRightToUpperLeft;
            case LeftToRight:
                return ScanDirection.RightToLeft;
            case TopToBottom:
                return ScanDirection.BottomToTop;
            case LowerRightToUpperLeft:
                return ScanDirection.UpperLeftToLowerRight;
            case RightToLeft:
                return ScanDirection.LeftToRight;
            case BottomToTop:
                return ScanDirection.TopToBottom;
            case LowerLeftToUpperRight:
                return ScanDirection.UpperRightToLowerLeft;
            case UpperRightToLowerLeft:
                return ScanDirection.LowerLeftToUpperRight;
            default:
                return scanDirection; // in case no opposite defined, return original direction
        }
    }

    public static List<VirtualPosition> defineStartPositionsForScanDirection(VirtualGameBoard virtualGameBoard, ScanDirection scanDirection)
    {
        List<VirtualPosition> listOfStartPositions = new ArrayList<>();
        List<List<VirtualPosition>> listOfRows = virtualGameBoard.getListOfRows();

        int rowCount = listOfRows.size();
        int columnCount = virtualGameBoard.getListOfColumns().size();
        int relevantIndex = 0;

        switch (scanDirection){
            case UpperLeftToLowerRight:
                relevantIndex = 0;  // start at left-most position
                break;
            case UpperRightToLowerLeft:
                relevantIndex = columnCount - 1; // start at right-most position
                break;
        }

        // read and add every item of top-row / header-row
        for(int i=0; i<columnCount; i++)
        {
            listOfStartPositions.add(listOfRows.get(0).get(i));
        }

        // read and add items at left-most position OR at right-most position
        for(int i=1; i<rowCount; i++)
        {
            listOfStartPositions.add(listOfRows.get(i).get(relevantIndex));
        }

        return listOfStartPositions;
    }

    public static boolean checkPositionStillInBounds(int iCurrentH, int iCurrentV, int iMaxH, int iMaxV){
        return iCurrentH >= 0 && iCurrentH <= iMaxH && iCurrentV >= 0 && iCurrentV <= iMaxV;
    }

    public static void flushPatternFlagsOfBoard(VirtualGameBoard virtualGameBoard){
        for (List<VirtualPosition> currentColumn:virtualGameBoard.getListOfColumns()) {
            for (VirtualPosition virtualPosition:currentColumn) {
                virtualPosition.flushPatternFlags();
            }
        }
    }

    public static PatternType redefinePatternType(VirtualPattern virtualPattern) {

        boolean upperLeftToLowerRight   = false;
        boolean leftToRight             = false;
        boolean lowerRightToUpperLeft   = false;
        boolean rightToLeft             = false;
        boolean bottomToTop             = false;
        boolean lowerLeftToUpperRight   = false;
        boolean upperRightToLowerLeft   = false;
        int spaceToNextCoin ? //TODO: GO ON HERE and underneath

        for(VirtualPotential virtualPotential:virtualPattern.getListOfPotentials())
        {
            switch (virtualPotential.getScanDirection())
            {
                case UpperLeftToLowerRight:
                    upperLeftToLowerRight   = true;
                    break;
                case LeftToRight:
                    leftToRight             = true;
                    break;
                case LowerRightToUpperLeft:
                    lowerRightToUpperLeft   = true;
                    break;
                case RightToLeft:
                    rightToLeft             = true;
                    break;
                case BottomToTop:
                    bottomToTop             = true;
                    break;
                case LowerLeftToUpperRight:
                    lowerLeftToUpperRight   = true;
                    break;
                case UpperRightToLowerLeft:
                    upperRightToLowerLeft   = true;
                    break;
            }
        }

        switch (virtualPattern.getPatternType())
        {
            case Diagonal:
                switch (virtualPattern.getChainLength())
                {
                    case 2:
                        if(upperLeftToLowerRight && upperRightToLowerLeft)
                            return PatternType.Diagonal_ullr_urll_2;
                        if(upperLeftToLowerRight)
                            return PatternType.Diagonal_ullr_2;
                        if(upperRightToLowerLeft)
                            return PatternType.Diagonal_urll_2;
                        break;
                    case 3:
                        if(upperLeftToLowerRight && upperRightToLowerLeft)
                            return PatternType.Diagonal_ullr_urll_3;
                        if(upperLeftToLowerRight)
                            return PatternType.Diagonal_ullr_3;
                        if(upperRightToLowerLeft)
                            return PatternType.Diagonal_urll_3;
                        break;
                }
                break;
            case Horizontal:
                switch (virtualPattern.getChainLength())
                {
                    case 2:
                        if(leftToRight && rightToLeft)
                            return PatternType.Horizontal_2_plr;
                        if(leftToRight)
                            return PatternType.Horizontal_2_pr;
                        if(rightToLeft)
                            return PatternType.Horizontal_2_pl;
                        break;
                    case 3:
                        if(leftToRight && rightToLeft)
                            return PatternType.Horizontal_3_plr;
                        if(leftToRight)
                            return PatternType.Horizontal_3_pr;
                        if(rightToLeft)
                            return PatternType.Horizontal_3_pl;
                        break;
                }
                break;
            case Vertical:
                switch (virtualPattern.getChainLength())
                {
                    case 2:
                        break;
                    case 3:
                        break;
                }
                break;
        }
    }

    public static boolean checkCompleteability(VirtualPattern virtualPattern) {
    }
}
