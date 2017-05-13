package own;

/**
 * Created by Nils on 13.05.2017.
 */
public class VirtualPotential {
    ScanDirection scanDirection;
    VirtualPosition virtualPosition;
    int gapDepthUnderneathPosition;

    public VirtualPotential(ScanDirection scanDirection, VirtualPosition virtualPosition, int gapDepthUnderneathPosition) {
        this.scanDirection = scanDirection;
        this.virtualPosition = virtualPosition;
        this.gapDepthUnderneathPosition = gapDepthUnderneathPosition;
    }

    public ScanDirection getScanDirection() {
        return scanDirection;
    }

    public VirtualPosition getVirtualPosition() {
        return virtualPosition;
    }

    public int getGapDepthUnderneathPosition() {
        return gapDepthUnderneathPosition;
    }
}


