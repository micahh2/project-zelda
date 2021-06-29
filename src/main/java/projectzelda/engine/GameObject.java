
// (c) Thorsten Hasbargen


package projectzelda.engine;

import projectzelda.*;
import java.awt.Color;

public abstract class GameObject implements Cloneable
{
    // yes, public  :(
    //
    public double  x,y;
    public double  alfa  = 0;
    public double  speed = 0;
    public Color   color;
    public ImageRef imageRef;

    // if the object is existing, moving etc
    public boolean isLiving = true;
    public boolean isMoving = true;

    // destination the object shall move to,
    // old position etc
    public double  destX, destY;
    public boolean hasDestination = false;
    public double  xOld,  yOld;


    // GameObjects sometimes call physics methods
    public static World         world;


    // construct GameObject
    public GameObject(double x_, double y_, double a_, double s_, Color color_)
    { 
        x=x_;    y=y_; 
        xOld=x;  yOld=y;
        alfa=a_; speed=s_;
        color = color_;
    }


    // move one step to direction <alfa>
    public void move(double diffSeconds)
    {  
        if(!isMoving) return;	  

        // move if object has a destination
        if(hasDestination)
        {
            // stop if destination is reached	
            double diffX = Math.abs(x-destX);
            double diffY = Math.abs(y-destY);
            if(diffX<3 && diffY<3)
            { isMoving = false;
                return;
            }
        }    

        // remember old position
        xOld=x; yOld=y; 

        // move one step
        x += Math.cos(alfa)*speed*diffSeconds;
        y += Math.sin(alfa)*speed*diffSeconds;   	  
    }


    // set a point in the world as destination
    public final void setDestination(double dx, double dy)
    {
        isMoving       = true;
        hasDestination = true;
        destX          = dx;
        destY          = dy;

        alfa = Math.atan2(dy-y, dx-x);
    }  


    // set the LOCATION of an object as destination
    public void setDestination(GameObject obj)
    { 
        setDestination(obj.x, obj.y);	  
    }

    // move back to the position BEFORE the move Method was called
    public void moveBack() { x=xOld; y=yOld; }


    public abstract int type();
    public static void setWorld(World w) {world=w;}

    public abstract boolean hasCollision(GameObject b);
    public abstract boolean hasCollision(RectangularGameObject b);
    public abstract boolean hasCollision(CircularGameObject b);
    public abstract void draw(GraphicSystem gs, long tick);

    public abstract GameObject clone();
    public void setClone(GameObject go) {
        go.destX = destX;
        go.destY = destY;
        go.isMoving = isMoving;
        go.hasDestination = hasDestination;
        go.xOld = xOld;
        go.yOld = yOld;
        go.alfa = alfa;
        go.speed = speed;
        go.isLiving = isLiving;
    }
}
