package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.GraphicSystem;
import projectzelda.engine.ImageRef;
import projectzelda.engine.WorldInfo;
import projectzelda.engine.Point;

public class BossNMonster extends GoblinAI
{
    public Avatar avatar;

    public BossNMonster(double x, double y, ImageRef imageRef, Avatar avatar)
    {
        super(x, y, null);
        this.imageRef = imageRef;
        this.avatar = avatar;
    }

    @Override
    public Point newDestination() {
        return new Point<Double>(avatar.x, avatar.y);
    }

    @Override
    public boolean readyForNewDestination() {
        double dist = world
                .getPhysicsSystem()
                .distance(x, y, avatar.x, avatar.y);

        return dist < 160;
    }

    @Override 
    public void draw(GraphicSystem gs) {
        gs.draw(this);
        healthBar.draw(gs);
    }
}
