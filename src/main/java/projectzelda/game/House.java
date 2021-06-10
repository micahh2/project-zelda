package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.RectangularGameObject;
import java.awt.Color;
import java.awt.*;

class House extends RectangularGameObject
{
    public House(double x, double y, int width, int height){
        super(x,y,0,0, width, height, new Color(64,160,64));
        this.isMoving = false;
    }

    public int type() { return 0; }
}
// (c) Thorsten Hasbargen

