
// (c) Thorsten Hasbargen
package projectzelda.engine;


import projectzelda.game.UIButton;

public interface GraphicSystem
{
  // prepare to draw a new Screen
  void clear(long currentTick);

  // draw foreground on-top of game objects
  void drawForeground(long currentTick);
  
  // draw ONE GameObject on the Screen
  void draw(CircularGameObject dot);
  void draw(RectangularGameObject dot);
 
  // draw ONE TextObject on the Screen
  void draw(TextObject obj);

  // draw ONE UIObject on the Screen
  void draw(UIObject obj);
  
  // display the completed Screen
  void redraw();
  
  // set world
  void setWorld(World world);  
}
