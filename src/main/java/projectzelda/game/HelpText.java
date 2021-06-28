package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;
import java.awt.Font;

class HelpText extends TextObject
{

    int width;
    int height;
    int rectX;
    int rectY;
    Font font;

    public HelpText(int x, int y)
    { 
        super(x,y, Color.BLACK);
        font = new Font("TEXT FONT", Font.ROMAN_BASELINE, 23);
    }

    public String toString()
    { String display = "Move: WASD      Interact: E   First objective: Find Brutus   "+
        "Attack: Space Bar    Pause: Esc   End: Q";
        return display;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        if(world.gameState != GameState.MAIN_MENU) {
            if (width == 0) {
                width = gs.getTextWidth(toString()) + 50;
                height = gs.getTextHeight() + 24;
                rectX = x - 25;
                rectY = y - height / 2 - 10;
            }
            gs.fillRoundRectScreen(rectX, rectY, width, height, 5, 5, Color.WHITE);
            gs.drawRoundRectScreen(rectX, rectY, width, height, 5, 5, 2, Color.BLACK);
            gs.drawCenteredTextScreen(rectX, rectY+4, width, height, color, font, toString());
        }
    }

}
