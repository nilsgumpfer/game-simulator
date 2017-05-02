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
}
