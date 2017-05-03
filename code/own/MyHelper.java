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
            case Vertical_2:
                return 0;
            case Vertical_2_pt:
                return 2;
            case Vertical_3:
                return 0;
            case Vertical_3_pt:
                return 3;
            case Horizontal_2:
                return 0;
            case Horizontal_2_pr:
                return 2;
            case Horizontal_2_pl:
                return 2;
            case Horizontal_3:
                return 0;
            case Horizontal_3_pr:
                return 3;
            case Horizontal_3_pl:
                return 3;
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
            case Horizontal_2_plr:
                return 3;
            case Horizontal_2_plr_d:
                return 4;
            case Horizontal_2_pl_d:
                return 3;
            case Horizontal_2_pr_d:
                return 3;
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
}
