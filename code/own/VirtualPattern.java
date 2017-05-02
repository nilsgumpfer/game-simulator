package own;

/**
 * Created by Nils on 01.05.2017.
 */
public class VirtualPattern {
    private VirtualPosition startPosition;
    private VirtualPosition endPosition;
    private PatternType patternType;
    private PlayerColor playerColor;

    public VirtualPattern(VirtualPosition startPosition, VirtualPosition endPosition, PatternType patternType, PlayerColor playerColor) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.patternType = patternType;
        this.playerColor = playerColor;
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

    @Override
    public String toString(){
        return  "(" + startPosition.getHorizontalPosition() + "|" + startPosition.getVerticalPosition() + ")" +
                "(" + endPosition.getHorizontalPosition() + "|" + endPosition.getVerticalPosition() + ")" +
                " Pattern: " + patternType + " Color: " + playerColor;
    }
}
