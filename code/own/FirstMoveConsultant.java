package own;

import basic.Move;

import java.util.List;

/**
 * Created by Nils on 30.04.2017.
 */
public class FirstMoveConsultant implements IMoveConsultant {
    private VirtualGameBoard virtualGameBoard = new VirtualGameBoard();

    @Override
    public void incorporateMoveOfRival(Move lastMove) {
        incorporateMove(lastMove, PlayerColor.Rival);
    }

    @Override
    public void incorporateMoveOfMyself(Move lastMove) {
        incorporateMove(lastMove, PlayerColor.Own);
    }

    @Override
    public Move getBestPossibleMove(List<Move> possibleMoves) {
        List<Integer> listOfPossibleColumns = MyHelper.extractPossibleColumnNumbers(possibleMoves);
        int selectedColumn = listOfPossibleColumns.get(0);

        // scan for all potentials of rival, planning to block them
        List<VirtualPattern> verticalPatterns = PatternScanner.scanForVerticalPatternsForColor(virtualGameBoard, PlayerColor.Rival, true);

        // print recognized patterns
        System.out.println("Recognized Patterns: ");
        for (VirtualPattern virtualPattern:verticalPatterns) {
            System.out.println(virtualPattern);
        }

        // if possible patterns/potentials found, block them (here: just first one found)
        if(verticalPatterns.size() > 0)
            selectedColumn = verticalPatterns.get(0).getStartPosition().getHorizontalPosition() + 1;

        return new Move(selectedColumn);
    }

    private void incorporateMove(Move move, PlayerColor playerColor){
        if(move != null) {
            int columnIndex = MyHelper.extractColumnIndex(move);
            virtualGameBoard.addCoinToColumn(columnIndex, playerColor);

            System.out.println(virtualGameBoard);
            /*
            List<VirtualPattern> virtualPatternList = PatternScanner.scanForAllVerticalPatterns(virtualGameBoard);

            System.out.println("Recognized Patterns: ");
            for (VirtualPattern virtualPattern:virtualPatternList) {
                System.out.println(virtualPattern);
            }*/
        }
    }
}
