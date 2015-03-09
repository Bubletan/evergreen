package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class MovementPacket implements AbstractGamePacket {
    
    private final boolean ctrlRun;
    private final int firstX;
    private final int firstY;
    private final byte[][] offsetXY;
    
    public MovementPacket(boolean ctrlRun, int firstX, int firstY, byte[][] offsetXY) {
        this.ctrlRun = ctrlRun;
        this.firstX = firstX;
        this.firstY = firstY;
        this.offsetXY = offsetXY;
    }
    
    public boolean isCtrlRun() {
        return ctrlRun;
    }
    
    public int getJumpPointCount() {
        return offsetXY.length + 1;
    }
    
    public int getJumpPointX(int index) {
        if (index == 0) {
            return firstX;
        } else {
            return firstX + offsetXY[index - 1][0];
        }
    }
    
    public int getJumpPointY(int index) {
        if (index == 0) {
            return firstY;
        } else {
            return firstY + offsetXY[index - 1][1];
        }
    }
}
