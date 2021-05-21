
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class Tree extends GameObject
{
  public Tree(double x, double y, int r)
  {
    super(x,y,0,0,r,new Color(64,160,64));
    this.isMoving = false;
  }
  
  public int type() { return Const.TYPE_TREE; }
}
