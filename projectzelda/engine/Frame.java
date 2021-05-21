
// (c) Thorsten Hasbargen

package projectzelda.engine;

public interface Frame 
{
  // appear on Screen
  void displayOnScreen();
  
  // return Subsystems
  GraphicSystem getGraphicSystem();
  InputSystem   getInputSystem();
}
