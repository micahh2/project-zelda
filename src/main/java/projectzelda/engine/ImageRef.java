
package projectzelda.engine;

public class ImageRef implements Cloneable {
    public boolean horizontallyFliped = false;
    public boolean verticallyFliped = false;
    public boolean diagonallyFliped = false;

    public String name;

    public int x1;
    public int y1;

    public int x2;
    public int y2;

    public ImageRef(String name, int x1, int y1, int x2, int y2) {
        this.name = name;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return x1 + "," +y1 + ":" + x2 + "," + y2;
    }

    public ImageRef clone() {
        ImageRef i = new ImageRef(name, x1, y1, x2, y2);
        i.horizontallyFliped = horizontallyFliped;
        i.verticallyFliped = verticallyFliped;
        i.diagonallyFliped = diagonallyFliped;
        return i;
    }
}
