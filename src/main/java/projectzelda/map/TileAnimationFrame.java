package projectzelda.map;

public class TileAnimationFrame {
    public int tileId;
    public int duration;

    public TileAnimationFrame(int tileId, int duration) {
        this.tileId = tileId;
        this.duration = duration;
    }

    public String toString() {
        return "TileAnimationFrame { "
            + "tileId="  + tileId
            + "; duration=" + duration
        + " }";
    }
}
