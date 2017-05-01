package own;

import basic.Move;

/**
 * Created by Nils on 30.04.2017.
 */
public class MyHelper {
    public static int extractColumnIndex(Move move){
        if(move != null) {
            String extractedDigit = move.toString().replaceAll("\\D+", "");
            int columnIndex = Integer.parseInt(extractedDigit) - 1;

            return columnIndex;
        }
        else
            return -1;
    }
}
