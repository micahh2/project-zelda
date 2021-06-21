
package projectzelda.engine;

import projectzelda.*;
import java.awt.Color;

public abstract class RectangularGameObject extends GameObject {
    public int  width = 4;
    public int  height = 4;

    public RectangularGameObject(double x, double y, double alpha, double speed, int width, int height, Color color) {
        super(x, y, alpha, speed, color);
        this.width = width;
        this.height = height;
    }


    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.draw(this);
    }

    @Override
    public boolean hasCollision(GameObject b) {
        return b.hasCollision(this);
    }

    public boolean hasCollision(CircularGameObject a) {
        // Utilized that this is implemented the in the CircularGameObject class
        return a.hasCollision(this);
    }

    public boolean hasCollision(RectangularGameObject b) {
        RectangularGameObject a = this;
        double firstWidth = a.x > b.x ? a.width : b.width;
        double firstHeight = a.y > b.y ? a.height : b.height;

        return Math.abs(b.x - a.x) < firstWidth && Math.abs(b.y - a.y) < firstHeight;
    }
}
