package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class StartAnimation extends CircularGameObject
{ 
    public StartAnimation(double x, double y, Color color)
    {
        super(x,y, 0, 500, 4, color);
        this.isMoving = false;
        this.radius = 1000;
    }

    @Override
    public void move(double diffSeconds)
    { 
        if (world.gameState != GameState.PLAY) { return; }
        this.radius -= 2000*diffSeconds;
        if (this.radius < 0) {
            isLiving = false;
        }
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        if (world.gameState != GameState.PLAY) { return; }
        gs.drawOval((int)x-radius, (int)y-radius, radius*2, radius*2, color);
        gs.drawOval((int)x-radius/2, (int)y-radius/2, radius, radius, color);
        gs.fillOval((int)x-radius/4, (int)y-radius/4, radius/2, radius/2, color);
    }

    public final int type() { return Const.Type.ANIMATION.ordinal();}
}
