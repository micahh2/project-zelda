package projectzelda.map;

import projectzelda.engine.ImageRef;

public class MapObject {
    public int id;
    public float x;
    public float y;
    public ImageRef imageRef;

    public MapObject(int id, float x, float y, ImageRef imRef) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.imageRef = imRef;
    }

    @Override
    public String toString() {
        return "MapObject { " +
                "id=" + id +
                "; x=" + x +
                "; y=" + y +
                "; imageRef=" + imageRef +
                "; }";
    }
}
