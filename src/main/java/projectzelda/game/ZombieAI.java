
// (c) Thorsten Hasbargen


package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;


class ZombieAI extends GameObject
{
    private static final int HUNTING  = 1;
    private static final int STUCK    = 2;
    private static final int CLEARING = 3;

    private int    state;
    private double alfaClear;
    private double secondsClear;

    // life of a zombie
    private double life = 1.0;


    public ZombieAI(double x, double y)
    {
        super(x,y,0,60,15,new Color(160,80,40));
        this.isMoving = false;

        state = HUNTING;

        // turn left or right to clear
        alfaClear = Math.PI;
        if(Math.random()<0.5) alfaClear = -alfaClear;

    }


    public void move(double diffSeconds)
    {
        double dist = world
            .getPhysicsSystem()
            .distance(x, y, world.avatar.x, world.avatar.y);

        // if avatar is too far away: stop
        if(dist > 800)
        { 
            isMoving=false;
            return;
        } else { 
            isMoving = true;
        }


        // state HUNTING
        //

        if(state==HUNTING)
        {
            setDestination(world.avatar); 

            super.move(diffSeconds);

            // handle collisions of the zombie
            GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
            for(int i=0; i<collisions.size(); i++)
            {
                GameObject obj = collisions.get(i);

                int type = obj.type();

                // if object is avatar, game over
                if (type == Const.TYPE_AVATAR) 
                { 
                    moveBack();
                    world.gameOver=true; 
                }

                // if object is zombie, step back
                if (type == Const.TYPE_ZOMBIE) 
                { 
                    moveBack(); 
                    state = STUCK;
                    return;
                }

                // if Object is a tree, move back one step
                if (obj.type() == Const.TYPE_TREE) 
                { 
                    moveBack(); 
                    state = STUCK;
                    return;
                }
            }
        }  

        // state STUCK
        //

        else if(state==STUCK)
        {
            // seconds left for clearing
            secondsClear = 1.0+Math.random()*0.5;
            // turn and hope to get clear
            alfa += alfaClear*diffSeconds;

            // try to clear
            state = CLEARING;	  
        }


        // state CLEARING
        //
        else if(state==CLEARING)
        {
            // check, if the clearing time has ended
            secondsClear -= diffSeconds;
            if(secondsClear<0)
            {
                state = HUNTING;
                return;
            }


            // try step in this direction
            super.move(diffSeconds);

            // check if path was unblocked
            GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
            if(collisions.size()>0)
            {
                moveBack();

                // stuck again
                this.state=STUCK;
                return;
            }

        }
    }


    // inform zombie it is hit
    public void hasBeenShot()
    { 
        // every shot decreases life
        life -= 0.21;

        // if Zombie is dead (haha), delete it	
        if(life<=0)
        {
            this.isLiving=false;
            return;
        }
    }


    public int type() { return Const.TYPE_ZOMBIE; }
}