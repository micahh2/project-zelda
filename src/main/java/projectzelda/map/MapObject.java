package projectzelda.map;

import projectzelda.engine.ImageRef;

public class MapObject {
    public enum Type { POLYGON, POINT, ELLIPSE }

    public Type type;
    public int id;
    public float x;
    public float y;
    public ImageRef startingBounds;
    public ImageRef imageRef;

    public MapObject(int id, float x, float y, Type type, ImageRef startingBounds, ImageRef imRef) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
        this.imageRef = imRef;
        this.startingBounds = startingBounds;
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
