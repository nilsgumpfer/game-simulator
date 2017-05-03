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

    public static List<VirtualPattern> filterHighPotentials(List<VirtualPattern> virtualPatternList)
    {
        List<VirtualPattern> resultList = new ArrayList<>();

        for (VirtualPattern virtualPattern:virtualPatternList){
            if(scorePotential(virtualPattern) > 2)
                resultList.add(virtualPattern);
        }

        return resultList;
    }

    public static int scorePotential(VirtualPattern virtualPattern)
    {
        switch(virtualPattern.getPatternType())
        {
            case Vertical_2_pt:
                return 2;
            case Vertical_3_pt:
                return 3;
            case Horizontal_2_pr:
                return 2;
            case Horizontal_2_pl:
                return 2;
            case Horizontal_3_pr:
                return 3;
            case Horizontal_3_pl:
                return 3;
            case Horizontal_3_plr:
                return 4; //worst case! --> if rival acts smart, he´ll win
            default:
                return 0;
        }
    }
}
