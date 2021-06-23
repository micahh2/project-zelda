package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class Arrow extends CircularGameObject
{ 
    private double lifeTime = 5;

    public enum Dir {
        LEFT,
        UP_LEFT,
        DOWN_LEFT,
        RIGHT,
        UP_RIGHT,
        DOWN_RIGHT,
        UP,
        DOWN
    }

    public Dir dir;
    public float rotation = 0.0f;

    public Arrow(double x, double y, Dir dir, ImageRef imageRef)
    {
        super(x,y, 0, 500, 4, Color.BLUE);
        this.isMoving = true;
        this.dir = dir;
        this.imageRef = imageRef;
        setDestination();
    }

    public void setDestination() {
        int diffx=0;
        int diffy=0;
        switch(dir) {
            case LEFT:
                diffx = -10;
                rotation = -0.5f;
                break;
            case UP_LEFT:
                diffx = -10;
                diffy = -10;
                rotation = -0.375f;
                break;
            case DOWN_LEFT:
                diffx = -10;
                diffy = 10;
                rotation = -0.625f;
                break;
            case RIGHT:
                diffx = 10;
                rotation = 0f;
                break;
            case UP_RIGHT:
                diffx = 10;
                diffy = -10;
                rotation = -0.125f;
                break;
            case DOWN_RIGHT:
                diffx = 10;
                diffy = 10;
                rotation = 0.125f;
                break;
            case UP:
                diffy = -10;
                rotation = -0.25f;
                break;
            case DOWN:
                diffy = 10;
                rotation = 0.25f;
                break;
        }
        setDestination(x+diffx, y+diffy);
    }

    @Override
    public void move(double diffSeconds)
    { 
        setDestination();
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
                case ROCK:
                case WALL:
                case TREE:
                    this.isLiving = false;
                    break;
                case ANIMAL:
                case NPC:
                    this.isLiving = false;
                    NPC guy = (NPC)obj;
                    guy.hit();
                    break;
                case GOBLIN:
                    this.isLiving = false;
                    EnemyAI bad = (EnemyAI)obj;
                    bad.hit();
                    break;
            }  
        }

        super.move(diffSeconds);
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.drawImage(imageRef, (int)x, (int)y, (int)x+imageRef.x2, (int)y+imageRef.y2, rotation);
    }

    public final int type() { return Const.Type.SHOT.ordinal();}

}
