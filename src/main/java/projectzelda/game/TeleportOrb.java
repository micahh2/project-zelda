package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class TeleportOrb extends CircularGameObject
{ 
    private double lifeTime = 5;
    boolean active = false;

    int reX;
    int reY;

    int[] posXs = {100, 200, 400, 500, 600, 700, 1200, 100};
    int[] posYs = {200, 300, 500, 300, 600, 100, 1200, 1300};
    static int index=0;

    public TeleportOrb(double x, double y, double xDest, double yDest)
    {
        super(x,y,Math.atan2(yDest-y, xDest-x), 350, 6, Color.CYAN);
        this.isMoving = true;
        reX = posXs[index];
        reY = posYs[index];
        index = (index+1)%posXs.length;
    }

    public void move(double diffSeconds)
    { 
        lifeTime -= diffSeconds;
        if (lifeTime <= 0) { 
            this.isLiving=false; 
            return;
        }

        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for(int i=0; i<collisions.size(); i++)
        {
            GameObject obj = collisions.get(i);

            Const.Type type = Const.Type.values()[obj.type()];

            switch(type) {
                case AVATAR:
                    Avatar avatar = (Avatar)obj;
                    avatar.teleport(reX, reY);
                    if (!active) {
                        active = true;
                        speed = 0;
                        radius *= 3;
                    }
                    return;
                case SWORD_SWING:
                    this.isLiving = false;
                    return;
                case SHOT:
                    if (active) {
                        obj.x = reX;
                        obj.y = reY;
                    }
                    break;

            } 
        }

        super.move(diffSeconds);
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        if (active) {
            gs.fillOval((int)x, (int)y, radius, radius, Color.WHITE);
            gs.drawOval((int)x, (int)y, radius, radius, 1, color);
            gs.drawOval(reX, reY, 3, 3, 1, Color.WHITE);
            return;
        }
        gs.drawOval((int)x, (int)y, radius, radius, 1, Color.WHITE);
        gs.fillOval((int)x, (int)y, radius, radius, color);
    }

    public final int type() { return Const.Type.SHOT.ordinal();}
}
