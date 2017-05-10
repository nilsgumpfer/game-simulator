package own;

import basic.Move;

import java.util.ArrayList;
import java.util.Collections;
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


    public static List<Integer> readGapsOfPattern(VirtualPattern virtualPattern)
    {
        List<Integer> listOfGaps = new ArrayList<>();

        switch (virtualPattern.getPatternType())
        {
            case Vertical_2:
                break;
            case Vertical_2_pt:
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()); // column index of pattern
                break;
            case Vertical_3:
                break;
            case Vertical_3_pt:
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()); // column index of pattern
                break;
            case Horizontal_2:
                break;
            case Horizontal_2_pr:
                listOfGaps.add(virtualPattern.getEndPosition().getHorizontalPosition()+1); // column index of gap
                break;
            case Horizontal_2_pl:
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()-1); // column index of gap
                break;
            case Horizontal_3:
                break;
            case Horizontal_3_pr:
                listOfGaps.add(virtualPattern.getEndPosition().getHorizontalPosition()+1); // column index of gap
                break;
            case Horizontal_3_pl:
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()-1); // column index of gap
                break;
            case Horizontal_3_plr:
                listOfGaps.add(virtualPattern.getEndPosition().getHorizontalPosition()+1); // column index of gap
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()-1); // column index of gap
                break;
            case Diagonal_2_hr:
                break;
            case Diagonal_2_hl:
                break;
            case Diagonal_3_hr:
                break;
            case Diagonal_3_hl:
                break;
            case Horizontal_2_plr:
                listOfGaps.add(virtualPattern.getEndPosition().getHorizontalPosition()+1); // column index of gap
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()-1); // column index of gap
                break;
            case Horizontal_2_plr_d:
                listOfGaps.add(virtualPattern.getEndPosition().getHorizontalPosition()+1); // column index of gap
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()-1); // column index of gap
                break;
            case Horizontal_2_pl_d:
                listOfGaps.add(virtualPattern.getStartPosition().getHorizontalPosition()-1); // column index of gap
                break;
            case Horizontal_2_pr_d:
                listOfGaps.add(virtualPattern.getEndPosition().getHorizontalPosition()+1); // column index of gap
                break;
        }

        return listOfGaps;
    }

    public static List<VirtualPosition> getStartPositionsForDirection(VirtualGameBoard virtualGameBoard, ScanDirection scanDirection)
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

    public static boolean checkPosition(int iCurrentH, int iCurrentV, int iMaxH, int iMaxV){
        return iCurrentH >= 0 && iCurrentH <= iMaxH && iCurrentV >= 0 && iCurrentV <= iMaxV;
    }
}
