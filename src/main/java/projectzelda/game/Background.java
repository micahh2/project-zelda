package projectzelda.game;

import projectzelda.engine.GraphicSystem;
import projectzelda.engine.UIObject;

import java.awt.*;

public class Background extends UIObject {
    public int width;
    public int height;


    Background(int x_, int y_, int width_, int height_, Color color_){
        super(0, 0);
        width =width_;
        height = height_;
        color = color_;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.fillRectScreen(x, y, width, height, color);
    }
}
