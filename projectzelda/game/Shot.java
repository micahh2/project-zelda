
package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class Shot extends GameObject
{ 
  private double lifeTime = 1.2;

  public Shot(double x, double y, double xDest, double yDest)
  {
    super(x,y,Math.atan2(yDest-y, xDest-x),500,4,Color.YELLOW);
    this.isMoving = true;
  }
  
  public Shot(double x, double y, double a, double s, double time)
  { super(x,y,a,s,4,Color.YELLOW);
    lifeTime = time;
    this.isMoving = true;
  }
  
  
  public void move(double diffSeconds)
  { 
	lifeTime -= diffSeconds;
	if(lifeTime<=0)
	{ this.isLiving=false;
	  return;
	}
	
	
    // handle collisions of the zombie
	GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
	for(int i=0; i<collisions.size(); i++)
	{
	  GameObject obj = collisions.get(i);
	  
	  int type = obj.type();
	  
	  // tree: shot is deleted
	  if(type==Const.TYPE_TREE)
	  { this.isLiving=false;
	  }
	  // Zombie: inform Zombie it is hit
	  else if(type==Const.TYPE_ZOMBIE && obj.isLiving)
	  { 
	    ZombieAI zombie = (ZombieAI)obj;
	    zombie.hasBeenShot();
        this.isLiving=false;
	  }
	}  
	
	super.move(diffSeconds);
  }
    
  public final int type() { return Const.TYPE_SHOT;}
}
