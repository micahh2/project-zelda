
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.engine.*;

class RPGPhysicsSystem extends PhysicsSystem
{
	
  RPGPhysicsSystem(World w)
  { super(w);
  }
  
  
  //
  // collisions for circle Objects only...
  //
  public GameObjectList getCollisions(GameObject object)
  {
    GameObjectList result = new GameObjectList();
    
    int len = world.gameObjects.size();
    for(int i=0; i<len; i++)
    {
      GameObject obj2 = world.gameObjects.get(i);
      
      // an object doesn't collide with itself
      if(obj2==object) continue;
      
      // check if they touch each other
      double dist = object.radius+obj2.radius;
      double dx   = object.x-obj2.x;
      double dy   = object.y-obj2.y;
      
      if(dx*dx+dy*dy < dist*dist)
      { result.add(obj2);
      }
    }
    
    return result;
  }
 
  
}
