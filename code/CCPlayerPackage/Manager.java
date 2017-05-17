package CCPlayerPackage;

import basic.Move;
import basic.CCPlayer;
import basic.Position;

import java.util.List;

/**
 * Created by Carlo on 02.05.2017.
 */
public class Manager {

    CCPlayer ownPlayer;
    VirtualGameBoard virtualGameBoard = new VirtualGameBoard();
    MoveGenerator moveGenerator;
    WinSituationDetector winSituationDetector = new WinSituationDetector(this);
    public int lastOwnColumn = 8;

    public Manager(CCPlayer ownPlayer){
        this.ownPlayer = ownPlayer;
    }

    public void createMoveGenerator(Position p, List<Move> moves){
        moveGenerator = new MoveGenerator(p, moves, this, this.winSituationDetector, ownPlayer);
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }

    public WinSituationDetector getWinSituationDetector() {
        return winSituationDetector;
    }

    public void initializeVirtualGameBoard(){
        virtualGameBoard.initializeBoard();
    }

    public void printVirtualGameBoard(){
        virtualGameBoard.printBoard();
    }

    public PlayerEnum[][] getVirtualGameBoard(){
        return virtualGameBoard.getVirtualGameBoard();
    }

    public PlayerEnum getPlayerEnumAtPosition(int row, int column){
        PlayerEnum playerEnum = virtualGameBoard.getPlayerEnumAtPosition(row, column);
        return playerEnum;
    }

    public void addCoinToBoard(PlayerEnum playerEnum, int col){
        virtualGameBoard.addCoinToBoard(playerEnum, col);
    }

    public Move getMove(){
        return null;
    }

    public int getLastOwnColumn() {
        return lastOwnColumn;
    }

    public void setLastOwnColumn(int lastOwnColumn) {
        this.lastOwnColumn = lastOwnColumn;
    }

    public List<Integer> getRemainingColumns(){
        return virtualGameBoard.getRemainingColumns();
    }
}
