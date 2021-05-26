
// (c) Thorsten Hasbargen


package projectzelda.engine;

import projectzelda.*;
import java.awt.Color;

public abstract class GameObject 
{
    // yes, public  :(
    //
    public double  x,y;
    public double  alfa  = 0;
    public double  speed = 0;
    public int     radius = 7;
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
    public GameObject(double x_, double y_, 
            double a_, double s_, 
            int radius_, Color color_)
    { 
        x=x_;    y=y_; 
        xOld=x;  yOld=y;
        alfa=a_; speed=s_;
        radius=radius_;
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


    // test and reflect on Window Borders
    public void reflectOnBorders()
    {
        double rad = radius;
        double PI  = Math.PI;

        if(x<rad && (alfa>PI/2 && alfa<PI*3/2)) { 
            alfa = Math.PI-alfa;
        }
        if(y<rad && alfa>PI) { 
            alfa = PI*2-alfa; 
        }
        if(x > Const.WORLD_WIDTH-rad) { 
            alfa = Math.PI-alfa;
        }
        if(y > Const.WORLD_HEIGHT-rad) { 
            alfa = PI*2-alfa;
        }


        if(alfa<0)    alfa += PI*2;
        if(alfa>PI*2) alfa -= PI*2;	
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
    { setDestination(obj.x, obj.y);	  
    }


    // move back to the position BEFORE the move Method was called
    public void moveBack() { x=xOld; y=yOld; }


    public abstract int type();
    public static void setWorld(World w) {world=w;}

}
