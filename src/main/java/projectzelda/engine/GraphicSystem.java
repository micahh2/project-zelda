
// (c) Thorsten Hasbargen
package projectzelda.engine;
import java.awt.Color;
import java.awt.Font;

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

    // display the completed Screen
    void redraw();

    // set world
    void setWorld(World world);  

    // For drawing with absolute world coordinates
    void drawRect(int xAbs, int yAbs, int width, int height, Color color);

    void fillRect(int xAbs, int yAbs, int width, int height, Color color);

    void drawCenteredText(int xAbs, int yAbs, int width, int height, Color color, Font font, String text);

    void drawImage(ImageRef imageRef, int x1Abs, int y1Abs, int x2Abs, int y2Abs);
    void drawImage(ImageRef imageRef, int x1Abs, int y1Abs, int x2Abs, int y2Abs, float rotation);

    void drawOval(int xAbs, int yAbs, int r1, int r2, Color color);

    void fillOval(int xAbs, int yAbs, int r1, int r2, Color color);

    // For drawing with relative screen coordinates
    void drawRectScreen(int x, int y, int width, int height, Color color);

    void fillRectScreen(int x, int y, int width, int height, Color color);

    void drawOvalScreen(int x, int y, int r1, int r2, Color color);

    void fillOvalScreen(int x, int y, int r1, int r2, Color color);

    void drawCenteredTextScreen(int x, int y, int width, int height, Color color, Font font, String text);

    void drawCenteredTextScreenWithSub(int x, int y, int width, int height, Color color, Font font,Font helpFont, String text, String helpText);


    void drawImageScreen(ImageRef imageRef, int x1, int y1, int x2, int y2);
    void drawImageScreen(ImageRef imageRef, int x1, int y1, int x2, int y2, float rotation);
}
