package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.GraphicSystem;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class BossNMonster extends RectangularGameObject
{
    public HealthBar healthBar;
    public BossNMonster(double x, double y, int width, int height, ImageRef imageRef)
    {
        super(x, y, 0, 0, width, height, null);
        this.isMoving = false;
        healthBar = new HealthBar(0, 0, 50, 5);
        adjustHealthBarPosition();
        this.imageRef = imageRef;
    }

    // Invisible
    @Override
    //public void draw(GraphicSystem gs) { return; }

    public int type() { return Const.TYPE_TREE; }

    private void adjustHealthBarPosition() {
        int healthBarX = (int) (x - healthBar.width / 2);
        int healthBarY = (int) (y - 2 * healthBar.height);
        healthBar.x = healthBarX;
        healthBar.y = healthBarY;
    }
}
