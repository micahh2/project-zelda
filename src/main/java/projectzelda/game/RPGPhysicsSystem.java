
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
      if(obj2 == object) continue;
      
      if(object.hasCollision(obj2)) { result.add(obj2); }
    }
    
    return result;
  }
}
