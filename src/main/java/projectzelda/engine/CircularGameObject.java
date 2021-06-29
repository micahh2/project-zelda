package projectzelda.engine;

import projectzelda.*;
import java.awt.Color;

public abstract class CircularGameObject extends GameObject {
    public int  radius = 7;

    public CircularGameObject(double x, double y, double alpha, double speed, int radius, Color color) {
        super(x, y, alpha, speed, color);
        this.radius = radius;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.draw(this);
    }

    @Override
    public boolean hasCollision(GameObject b) {
        return b.hasCollision(this);
    }

    public boolean hasCollision(CircularGameObject b) {
        // check if they touch each other
        double dist = radius + b.radius;
        double dx   = x-b.x;
        double dy   = y-b.y;

        return dx*dx+dy*dy < dist*dist;
    }

    public boolean hasCollision(RectangularGameObject b) {
        float halfWidth = b.width/2;
        float halfHeight = b.height/2;
        double x1 = Math.abs(x - b.x - halfWidth) - halfWidth;
        double y1 = Math.abs(y - b.y - halfHeight) -halfHeight;

        if (x1 > 0) {
            if (y1 > 0) {
                return (x1 * x1) + (y1 * y1) < radius*radius;
            }
            return x1 < radius;
        }
        return y1 < radius;
    }

    public void setClone(CircularGameObject r) {
        super.setClone(r);
        r.radius = radius;
    }
}
