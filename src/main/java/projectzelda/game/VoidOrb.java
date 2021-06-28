package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class VoidOrb extends CircularGameObject
{ 
    private double lifeTime = 5;
    GameObject parent;
    double targetDir;
    double xDest;
    double yDest;
    double totalDist;
    boolean inverse = false;

    public VoidOrb(double x, double y, double xDest, double yDest, GameObject parent)
    {
        super(x,y,Math.atan2(yDest-y, xDest-x), 300, 6, Color.BLACK);
        targetDir= Math.atan2(yDest-y, xDest-x);
        this.isMoving = true;
        this.parent = parent;
        this.totalDist = Math.sqrt(Math.pow(yDest-y,2) + Math.pow(xDest-x, 2));
        this.xDest = xDest;
        this.yDest = yDest;
        inverse = Math.random() > 0.5;
    }

    public void move(double diffSeconds)
    { 
        double dist = Math.sqrt(Math.pow(yDest-y,2) + Math.pow(xDest-x, 2));
        double alpha = Math.atan2(yDest-y, xDest-x);
        if (!inverse) {
            alfa = alpha - Math.cos(dist/totalDist*2);
        } else {
            alfa = alpha - Math.sin(1 - dist/totalDist*2);
        }

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
                    this.isLiving = false;
                    Avatar avatar = (Avatar)obj;
                    avatar.hit(0.1);
                    return;
                case SWORD_SWING:
                    this.isLiving = false;
                    return;
                case GOBLIN:
                    if (this.parent == obj) { break; }
                    this.isLiving = false;
                    EnemyAI bad = (EnemyAI)obj;
                    bad.hit();
                    return;
            } 
        }

        super.move(diffSeconds);
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.drawOval((int)x, (int)y, radius, radius, 1, Color.MAGENTA);
        gs.fillOval((int)x, (int)y, radius, radius, color);
    }

    public final int type() { return Const.Type.SHOT.ordinal();}
}
