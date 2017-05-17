package CCPlayerPackage;

import basic.Move;
import basic.CCPlayer;
import basic.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Carlo on 07.05.2017.
 */
public class MoveGenerator {

    Position position;
    List<Move> moves;
    List<Integer> detectedMoves = new ArrayList<>();
    List<Integer> blockedColumns = new ArrayList<>();

    Move plannedMove;
    CCPlayer ownPlayer;
    Manager manager;
    WinSituationDetector winSituationDetector;
    int move = 0;

    public MoveGenerator(Position p, List<Move> moves, Manager manager, WinSituationDetector winSituationDetector, CCPlayer ownPlayer) {
        position = p;
        this.moves = moves;
        this.manager = manager;
        this.winSituationDetector = winSituationDetector;
        this.ownPlayer = ownPlayer;
    }

    public Move getOwnWinMove() {

        List<DetectedChain> winnableChains = new ArrayList<>();
        //Eigene 2er/3er Reihe zum Gewinn führen
        //get >= 2-coin chains
        for (int i = 0; i < winSituationDetector.getOwnDetectedChains().size(); i++) {
            if (winSituationDetector.getOwnDetectedChains().get(i).getSize() >= 2) {
                winnableChains.add(winSituationDetector.getOwnDetectedChains().get(i));
            }
        }

        //VERTICAL
        for(int i = 0; i < winnableChains.size(); i++){
            if(winnableChains.get(i).getChainType()==ChainType.VERTICAL){
                if(winnableChains.get(i).getSize()==3) {
                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getStartPositionCol()) != null) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getStartPositionCol()) == PlayerEnum.EMPTY) {
                            return getMoveOfColumn(winnableChains.get(i).getStartPositionCol());
                        }
                    }
                }
            }
        }

        //HORIZONTAL --> win 3 coin chain
        for (int i = 0; i < winnableChains.size(); i++) {
            //SET ON LEFT
            if (winnableChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                if (winnableChains.get(i).getSize() == 3) {
                    if(winnableChains.get(i).getStartPositionCol()-1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //WIN IT!
                                return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                            }
                        }
                    }

                       //SET ON RIGHT
                    if(winnableChains.get(i).getEndPositionCol()+1 < 7){
                            if(winnableChains.get(i).getEndPositionCol() + 1 <7) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) != null) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        //WIN IT!
                                        return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }

                    }
                }
        }

            //HORIZONTAL --> complete chain with missing coin
            for(int i = 0; i < winnableChains.size(); i++) {
                if (winnableChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                    if (winnableChains.get(i).getSize() == 2) {
                        //left
                        if(winnableChains.get(i).getStartPositionCol()-2 >= 0) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 2) != null) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 2) == PlayerEnum.OWN) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                        return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }

                            //right
                            if(winnableChains.get(i).getEndPositionCol()+2 < 7) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 2) == PlayerEnum.OWN) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            //DIAGONAL_TOP_LEFT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getStartPositionCol() - 1 >= 0 && winnableChains.get(i).getStartPositionRow() -1 >=0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow()-1, winnableChains.get(i).getStartPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow()-1, winnableChains.get(i).getStartPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                }
                            }
                            }
                        }
                    }
                }
            }


            //DIAGONAL_TOP_RIGHT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() + 1 < 7 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
            //DIAGONAL_BOTTOM_RIGHT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() + 1 < 7 && winnableChains.get(i).getEndPositionRow() +1 <7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+1, winnableChains.get(i).getEndPositionCol() +1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+1, winnableChains.get(i).getEndPositionCol() +1) == PlayerEnum.EMPTY) {
                               //check falling
                                if(winnableChains.get(i).getEndPositionRow()==6) {
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+2, winnableChains.get(i).getEndPositionCol() +1) != PlayerEnum.EMPTY){
                                        return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            //DIAGONAL_BOTTOM_LEFT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT){
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getStartPositionCol() - 1 >= 0 && winnableChains.get(i).getStartPositionRow() +1 < 7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow()+1, winnableChains.get(i).getStartPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow()+1, winnableChains.get(i).getStartPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if(winnableChains.get(i).getStartPositionRow()==6) {
                                    return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow()+2, winnableChains.get(i).getStartPositionCol() -1) != PlayerEnum.EMPTY){
                                        return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //NO WINMOVE FOUND
        return null;
    }

    public Move preventEnemyWin(){
        List<DetectedChain> harmfulEnemyMoves = new ArrayList<>();

        //get 2 & 3-coin rival chains
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() >= 2) {
                harmfulEnemyMoves.add(winSituationDetector.getRivalDetectedChains().get(i));
            }
        }

        //BLOCK COLUMNS
        for(int i = 0; i < harmfulEnemyMoves.size(); i++){
                if(harmfulEnemyMoves.get(i).getSize()==3){
                    //HORIZONTAL LEFT
                    if(harmfulEnemyMoves.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyMoves.get(i).getStartPositionCol()-1 >= 0) {
                        if (harmfulEnemyMoves.get(i).getChainType() == ChainType.HORIZONTAL) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (harmfulEnemyMoves.get(i).getStartPositionRow() - 1 >= 0) {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow() - 1, harmfulEnemyMoves.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(harmfulEnemyMoves.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                        
                    //HORIZONTAL RIGHT
                    if(harmfulEnemyMoves.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyMoves.get(i).getStartPositionCol()-1 >= 0) {
                        if (harmfulEnemyMoves.get(i).getChainType() == ChainType.HORIZONTAL) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (harmfulEnemyMoves.get(i).getEndPositionRow() + 1 < 7) {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow() + 1, harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }


                    //DIAGONAL TOP RIGHT
                    if(harmfulEnemyMoves.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyMoves.get(i).getEndPositionRow()-1 >= 0) {
                        if(harmfulEnemyMoves.get(i).getChainType()==ChainType.DIAGONAL_TOP_RIGHT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow()-1, harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }

                    //DIAGONAL TOP LEFT
                    if(harmfulEnemyMoves.get(i).getEndPositionCol()-1 >= 0 && harmfulEnemyMoves.get(i).getEndPositionRow()-1 >= 0) {
                        if(harmfulEnemyMoves.get(i).getChainType()==ChainType.DIAGONAL_TOP_LEFT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow()-1, harmfulEnemyMoves.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(harmfulEnemyMoves.get(i).getEndPositionCol() -1);
                                }
                            }
                        }
                    }

                    //DIAGONAL BOTTOM RIGHT
                    if(harmfulEnemyMoves.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyMoves.get(i).getEndPositionRow()+1 < 7) {
                        if(harmfulEnemyMoves.get(i).getChainType()==ChainType.DIAGONAL_BOTTOM_RIGHT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow()+1, harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(harmfulEnemyMoves.get(i).getEndPositionCol() +1);
                                }
                            }
                        }
                    }

                    //DIAGONAL BOTTOM LEFT
                    if(harmfulEnemyMoves.get(i).getEndPositionCol()-1 >= 0 && harmfulEnemyMoves.get(i).getEndPositionRow()+1 < 7) {
                        if(harmfulEnemyMoves.get(i).getChainType()==ChainType.DIAGONAL_BOTTOM_LEFT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow()+1, harmfulEnemyMoves.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(harmfulEnemyMoves.get(i).getEndPositionCol() -1);
                                }
                            }
                        }
                    }

                    //TODO: Implement other ones
                }
            }
        

        //VERTICAL
        for(int i = 0; i < harmfulEnemyMoves.size(); i++){
            if(harmfulEnemyMoves.get(i).getChainType()==ChainType.VERTICAL){
                //WARNING: 3 coins!
                if(harmfulEnemyMoves.get(i).getSize()==3){
                    if(harmfulEnemyMoves.get(i).getStartPositionRow()-1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow() - 1, harmfulEnemyMoves.get(i).getStartPositionCol()) == PlayerEnum.EMPTY) {
                            return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol());
                        }
                    }
                }
            }
        }

        //HORIZONTAL --> prevent completion of chain
        for(int i = 0; i < harmfulEnemyMoves.size(); i++) {
            if (harmfulEnemyMoves.get(i).getChainType() == ChainType.HORIZONTAL) {
                if (harmfulEnemyMoves.get(i).getSize() == 2) {
                    //left
                    if(harmfulEnemyMoves.get(i).getStartPositionCol()-2 >= 0){
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 2) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 2) == PlayerEnum.RIVAL) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol() - 1);
                            }
                        }
                    }
                    }
                    //right
                    if((harmfulEnemyMoves.get(i).getEndPositionCol()+2) <7) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 2) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                    }
                }
            }


        //HORIZONTAL 2 coins with empty fields on left & right
        for(int i = 0; i < harmfulEnemyMoves.size(); i++) {
            if (harmfulEnemyMoves.get(i).getChainType() == ChainType.HORIZONTAL && harmfulEnemyMoves.get(i).getSize() == 2) {
                if(harmfulEnemyMoves.get(i).getStartPositionCol() - 1 >= 0){
                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 1) != null) {
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                Random random = new Random();
                                int setLeft = random.nextInt(2);
                                if (setLeft == 1) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol()-1);
                                } else {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getEndPositionCol()+1);
                                }
                            }
                        }
                    }
                }
            }
            }
        }

        //HORIZONTAL
        for(int i = 0; i < harmfulEnemyMoves.size(); i++) {
            //chainSize: 3
            if (harmfulEnemyMoves.get(i).getChainType() == ChainType.HORIZONTAL && harmfulEnemyMoves.get(i).getSize() >= 3) {
                //SET ON LEFT
                if (harmfulEnemyMoves.get(i).getStartPositionCol() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                            //check falling
                            if (harmfulEnemyMoves.get(i).getEndPositionRow() == 6) {
                                return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol() - 1);
                            } else {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow() + 1, harmfulEnemyMoves.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol() - 1);
                                }
                            }
                        }
                    }
                }
                //SET ON RIGHT
                if (harmfulEnemyMoves.get(i).getEndPositionCol() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            //check falling
                            if (harmfulEnemyMoves.get(i).getEndPositionRow() == 6) {
                                return getMoveOfColumn(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                            } else {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow() + 1, harmfulEnemyMoves.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL_TOP_LEFT - complete 3 coins
        for(int i = 0; i < harmfulEnemyMoves.size(); i++) {
            if (harmfulEnemyMoves.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (harmfulEnemyMoves.get(i).getSize() == 3) {
                    if (harmfulEnemyMoves.get(i).getStartPositionCol() - 1 >= 0 && harmfulEnemyMoves.get(i).getStartPositionRow() -1 >=0) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow()-1, harmfulEnemyMoves.get(i).getStartPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow()-1, harmfulEnemyMoves.get(i).getStartPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow(), harmfulEnemyMoves.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }


        //DIAGONAL_TOP_RIGHT - complete 3 coins
        for(int i = 0; i < harmfulEnemyMoves.size(); i++) {
            if (harmfulEnemyMoves.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (harmfulEnemyMoves.get(i).getSize() == 3) {
                    if (harmfulEnemyMoves.get(i).getEndPositionCol() + 1 < 7 && harmfulEnemyMoves.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow() - 1, harmfulEnemyMoves.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow() - 1, harmfulEnemyMoves.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow(), harmfulEnemyMoves.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL_BOTTOM_RIGHT - complete 3 coins
            for(int i = 0; i < harmfulEnemyMoves.size(); i++) {
            if (harmfulEnemyMoves.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                if (harmfulEnemyMoves.get(i).getSize() == 3) {
                    if (harmfulEnemyMoves.get(i).getEndPositionCol() + 1 < 7 && harmfulEnemyMoves.get(i).getEndPositionRow() +1 <7) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow()+1, harmfulEnemyMoves.get(i).getEndPositionCol() +1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow()+1, harmfulEnemyMoves.get(i).getEndPositionCol() +1) == PlayerEnum.EMPTY) {
                                //check falling
                                if(harmfulEnemyMoves.get(i).getEndPositionRow()==6) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getEndPositionRow() + 2, harmfulEnemyMoves.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                        return getMoveOfColumn(harmfulEnemyMoves.get(i).getEndPositionCol() + 1);
                                    }
                                }
                                }
                            }
                        }
                    }
                }
            }

        //DIAGONAL_BOTTOM_LEFT - complete 3 coins
        for(int i = 0; i < harmfulEnemyMoves.size(); i++) {
            if (harmfulEnemyMoves.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT){
                if (harmfulEnemyMoves.get(i).getSize() == 3) {
                    if (harmfulEnemyMoves.get(i).getStartPositionCol() - 1 >= 0 && harmfulEnemyMoves.get(i).getStartPositionRow() +1 < 7) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow()+1, harmfulEnemyMoves.get(i).getStartPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow()+1, harmfulEnemyMoves.get(i).getStartPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if(harmfulEnemyMoves.get(i).getStartPositionRow()==6) {
                                    return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyMoves.get(i).getStartPositionRow()+2, harmfulEnemyMoves.get(i).getStartPositionCol() -1) != PlayerEnum.EMPTY){
                                        return getMoveOfColumn(harmfulEnemyMoves.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //no prevention needed
        return null;
    }

    public Move getBasicMoveWithoutLogic(int lastOwnColumn) {

        List<Integer> remainingColumns = manager.getRemainingColumns();
        Random random = new Random();
        int rnd = random.nextInt(2);

        //first Move
        if(lastOwnColumn == 8){
            return moves.get(3);
        } else {
            //other moves
            for(int i = 0; i < remainingColumns.size(); i++){
                //place left (rnd = 0)
                if(rnd == 0){
                    if(remainingColumns.get(i)==lastOwnColumn-2) {
                        return moves.get(i);
                    }
                    else{
                            if(remainingColumns.get(i)==lastOwnColumn+2) {
                                return moves.get(i);
                            }
                        }
                } else {
                    //place right (rnd = 1)
                        if(remainingColumns.get(i)==lastOwnColumn+2){
                            return moves.get(i);
                        }
                        else {
                            if(remainingColumns.get(i)==lastOwnColumn-2){
                                return moves.get(i);
                            }
                        }
                }
            }

        }

        //if nothing works..
        return moves.get(moves.size()/2);
    }

    public Move getMove(boolean meFirst) {

        //logic sequence
        //win it!
        if(getOwnWinMove() != null){
            plannedMove = getOwnWinMove();
            return plannedMove;
        } else {
            //prevent enemy
            if(preventEnemyWin()!=null){
                plannedMove = preventEnemyWin();
            } else {
                //basic move
                plannedMove = getBasicMoveWithoutLogic(manager.getLastOwnColumn());
            }
        }

        //check if move is on blocked list
        for(int i = 0; i < blockedColumns.size(); i++){
            while(ownPlayer.getColumnOfMoveAsInt(plannedMove)==blockedColumns.get(i)){
                plannedMove = getBasicMoveWithoutLogic(manager.getLastOwnColumn());
            }
        }

        //return move
        return plannedMove;
    }

    public Move getPlannedMove() {
        return plannedMove;
    }

    public Move getMoveOfColumn(int column){
        Move returnMove = null;
        for(int i = 0; i < moves.size(); i++){
            String moveString = moves.get(i).toString();
            if(Integer.parseInt(Character.toString(moveString.charAt(moveString.length()-2)))==column+1){
                returnMove = moves.get(i);
            }
        }
        return returnMove;
    }

    public String getColumnOfMoveAsString(Move move)
    {
        String moveString = move.toString();
        return Character.toString(moveString.charAt(moveString.length()-2));
    }
}

