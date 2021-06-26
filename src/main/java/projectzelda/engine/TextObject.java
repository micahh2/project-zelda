
// (c) Thorsten Hasbargen
package projectzelda.engine;

import java.awt.Color;


public abstract class TextObject
{
  public static World world;
  
  // yes, public :(
  public int     x,y;
  public Color color;
  
  public TextObject(int x_, int y_, Color color_)
  { x=x_; y=y_; color=color_;
  }
  
  public abstract String toString();
  
  public static void setWorld(World w){world=w;}

  public void draw(GraphicSystem gs, long tick) {
      gs.draw(this);
  }
}
