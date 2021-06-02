package projectzelda.engine;

public class ImageRefTo extends ImageRef {
    public boolean horizontallyFlipped = false;
    public boolean verticallyFlipped = false;
    public boolean diagonallyFlipped = false;

    public int destx1;
    public int desty1;
    public int destx2;
    public int desty2;

    public ImageRefTo(String name, int x1, int y1, int x2, int y2, int destx, int desty, int destx2, int desty2) {
        super(name, x1, y1, x2, y2);
        this.destx1 = destx;
        this.desty1 = desty;
        this.destx2 = destx2;
        this.desty2 = desty2;
    }
    public ImageRefTo(String name, int x1, int y1, int x2, int y2, int destx, int desty, int destx2, int desty2, boolean horz, boolean vert, boolean diag) {
        super(name, x1, y1, x2, y2);
        this.destx1 = destx;
        this.desty1 = desty;
        this.destx2 = destx2;
        this.desty2 = desty2;
        horizontallyFlipped = horz;
        verticallyFlipped = vert;
        diagonallyFlipped = diag;
    }
    public ImageRefTo(ImageRef ir, int destx, int desty, int destx2, int desty2, boolean horz, boolean vert, boolean diag) {
        super(ir.name, ir.x1, ir.y1, ir.x2, ir.y2);
        this.destx1 = destx;
        this.desty1 = desty;
        this.destx2 = destx2;
        this.desty2 = desty2;
        horizontallyFlipped = horz;
        verticallyFlipped = vert;
        diagonallyFlipped = diag;
    }
}
