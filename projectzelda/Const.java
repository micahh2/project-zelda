
// (c) Thorsten Hasbargen

package projectzelda;

public final class Const 
{
  // size of the world
  public static final int WORLD_WIDTH      = 5000;
  public static final int WORLD_HEIGHT     = 4000;
  // size of the displayed part of the world
  public static final int WORLDPART_WIDTH  = 1920;
  public static final int WORLDPART_HEIGHT = 1080;
  // border: when to scroll
  public static final int SCROLL_BOUNDS    =  300;
	  
  public static final double SPAWN_INTERVAL = 0.2;
  public static final double SPAWN_GRENADE  = 10.0;
  public static final double LIFE_GRENADE   = 15.0;
  
  public static final int TYPE_AVATAR  = 1;
  public static final int TYPE_TEXT    = 2;
  public static final int TYPE_TREE    = 3;
  public static final int TYPE_ZOMBIE  = 4;
  public static final int TYPE_SHOT    = 5;
  public static final int TYPE_GRENADE = 6;
}
