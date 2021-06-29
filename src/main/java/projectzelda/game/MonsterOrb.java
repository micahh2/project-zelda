package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class MonsterOrb extends CircularGameObject
{ 
    public double lifeTime = 10;

    public GameObject target;

    public MonsterOrb(double x, double y, GameObject target)
    {
        super(x,y,Math.atan2(target.y-y, target.x-x), 75, 12, new Color(230, 191, 0));
        this.isMoving = true;
        this.target= target;
    }

    public void move(double diffSeconds)
    { 
        lifeTime -= diffSeconds;
        if (lifeTime <= 0) { 
            this.isLiving=false; 
            return;
        }
        alfa = Math.atan2(target.y-y, target.x-x);

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
        gs.drawOval((int)x, (int)y, radius, radius, 1, Color.BLACK);
    }

    public final int type() { return Const.Type.SHOT.ordinal();}

    public GameObject clone() {
        MonsterOrb m = new MonsterOrb(x, y, target);
        setClone(m);
        return m;
    }

    public void setClone(MonsterOrb m) {
        super.setClone(m);
        m.target = target;
        m.lifeTime = lifeTime;
    }
}
