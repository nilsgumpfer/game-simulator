package own;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 01.05.2017.
 */
public class VirtualPattern {
    private VirtualPosition startPosition;
    private VirtualPosition endPosition;
    private PatternType patternType;
    private PlayerColor playerColor;
    private int potentialRating = -1;
    private List<Integer> listOfGaps = new ArrayList<>();

    public VirtualPattern(VirtualPosition startPosition, VirtualPosition endPosition, PatternType patternType, PlayerColor playerColor) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.patternType = patternType;
        this.playerColor = playerColor;
        potentialRating = MyHelper.scorePotentialOfPattern(this);
        listOfGaps = MyHelper.readGapsOfPattern(this);

        System.out.println("Created new Pattern:" + this);
    }

    public VirtualPosition getStartPosition() {
        return startPosition;
    }

    public VirtualPosition getEndPosition() {
        return endPosition;
    }

    public PatternType getPatternType() {
        return patternType;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public int getPotentialRating() {
        return potentialRating;
    }

    public List<Integer> getListOfGaps() {
        return listOfGaps;
    }

    @Override
    public String toString(){
        return  "(" + startPosition.getHorizontalPosition() + "|" + startPosition.getVerticalPosition() + ")" +
                "(" + endPosition.getHorizontalPosition() + "|" + endPosition.getVerticalPosition() + ")" +
                " Pattern: " + patternType + " Color: " + playerColor + " Potential: " + potentialRating;
    }
}
