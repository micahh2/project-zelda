package projectzelda.map;

import java.util.List;

public class Polygon {
    public String points;

    public Polygon(String points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Polygon { " +
                "points=" + points +
                "; }";
    }
}
