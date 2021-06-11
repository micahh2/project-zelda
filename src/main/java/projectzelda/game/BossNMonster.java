package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.GraphicSystem;
import projectzelda.engine.ImageRef;
import projectzelda.engine.WorldInfo;
import projectzelda.engine.UIObject;

public class BossNMonster extends GoblinAI
{
    public BossNMonster(double x, double y, ImageRef imageRef, WorldInfo worldInfo)
    {
        super(x, y, worldInfo);
        this.imageRef = imageRef;
    }

    @Override 
    public void draw(GraphicSystem gs) {
        gs.draw(this);
        healthBar.draw(gs);
    }
}
