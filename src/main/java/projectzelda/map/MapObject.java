package projectzelda.map;

public class MapObject {
    public int id;
    public float x;
    public float y;
    public Polygon polygon;

    public MapObject(int id, float x, float y, Polygon polygon) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.polygon = polygon;
    }

    @Override
    public String toString() {
        return "MapObject { " +
                "id=" + id +
                "; x=" + x +
                "; y=" + y +
                "; polygon=" + polygon +
                "; }";
    }
}
