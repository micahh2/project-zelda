
package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class Bones extends CircularGameObject
{
    public Bones(double x, double y,  ImageRef bonesImage)
    {

        super(x, y, 0, 0, 10, Color.GRAY);
        this.imageRef = bonesImage;
    }

    public int type() { return Const.Type.BONES.ordinal(); }
}
