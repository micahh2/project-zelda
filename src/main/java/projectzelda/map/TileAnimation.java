package projectzelda.map;

import java.util.List;

public class TileAnimation {
    public int tileId;
    public List<TileAnimationFrame> frames;
    public int totalDuration;

    public TileAnimation(int tileId, List<TileAnimationFrame> frames, int totalDuration) {
        this.frames = frames;
        this.tileId = tileId;
        this.totalDuration = totalDuration;
    }

    public String toString() {
        return "TileAnimation { "
            + "tileId="  + tileId
            + "; frames=" + frames.toString()
            + "; totalDuration=" + totalDuration
        + " }";
    }
}
