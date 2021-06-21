
package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

class House extends RectangularGameObject
{
  public House(double x, double y, int width, int height)
  {
    super(x, y, 0, 0, width, height, null);
    this.isMoving = false;
  }

  // Invisible
  @Override
  public void draw(GraphicSystem gs, long tick) { return; }
  
  public int type() { return Const.Type.TREE.ordinal(); }
}
