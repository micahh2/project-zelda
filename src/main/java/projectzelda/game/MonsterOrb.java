package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class MonsterOrb extends CircularGameObject
{ 
    private double lifeTime = 5;

    public MonsterOrb(double x, double y, double xDest, double yDest)
    {
        super(x,y,Math.atan2(yDest-y, xDest-x), 100, 10, Color.YELLOW);
        this.isMoving = true;
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
                    int r = ((Avatar)obj).radius;
                    double alpha = Math.atan2(obj.y-y, obj.x-x);
                    double spawnX = x - Math.cos(alpha)*r*2;
                    double spawnY = y - Math.sin(alpha)*r*2;
                    ((RPGWorld)world).addMonster(spawnX, spawnY);
                    this.isLiving = false;
                    return;
                case SWORD_SWING:
                    this.isLiving = false;
                    return;
            } 
        }

        super.move(diffSeconds);
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.fillOval((int)x, (int)y, radius, radius, color);
        gs.drawOval((int)x, (int)y, radius, radius, 1, Color.GRAY);
    }

    public final int type() { return Const.Type.SHOT.ordinal();}
}
