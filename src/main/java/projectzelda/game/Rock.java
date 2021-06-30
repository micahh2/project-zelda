package projectzelda.game;

import projectzelda.engine.*;

import java.awt.*;


public class Rock extends RectangularGameObject
{

    protected double life = 1.0;
    protected static final double COLOR_COOLDOWN = 0.2;
    protected double colorCooldown = 0;
    private int posXChatBox = world.worldInfo.getPartWidth() / 2 - 300;
    private int posYChatBox = world.worldInfo.getPartHeight() - 100;

    private String[] rockText = {
            "Adlez: I wonder what's behind there."
    };
    private String[] rockQuestText = {
            "Adlez: I think I can see their king..",
            "Adlez: ..I must break this down now..",
            "Adlez: .. and save this town!"
    };

    public Rock(double x, double y, ImageRef imageRef)
    {
        super(x, y, 0, 0, 128, 46, null);
        this.isMoving = true;
        this.imageRef = imageRef;
    }


    @Override
    public void move(double diffSeconds) {
        if(colorCooldown > 0) { colorCooldown -= diffSeconds; }
    }

    // Invisible
    @Override
    public void draw(GraphicSystem gs, long tick) {
        int shake = 0;
        if (colorCooldown > 0 && tick % 3 == 0) {
            shake = (int)(Math.random()*2)+1;
        }
        int x2 = (int)x + width+shake;
        int y2 = (int)y + height+shake;
        gs.drawImage(imageRef, (int)x+shake, (int)y+shake, x2, y2);
    }

    public int type() { return Const.Type.ROCK.ordinal(); }

    public void hit() {
        // every hit decreases life
        if (((RPGWorld) world).questState == QuestState.BOSS) {
            life -= 0.21;
            colorCooldown = COLOR_COOLDOWN;

            // if Goblin is dead, delete it
            if (life <= 0) {
                die();

            }
        } else {
            world.gameState = GameState.DIALOG;
            // if const.type is rock game freezes, using bones or goblin seems to work ok though
            ((RPGWorld)world).addChatBox("It might be bad idea to break this.", this);
        }

    }

    public void die() {
        this.isLiving = false;
        ((RPGWorld)world).removeMountain();
    }

    public boolean isDead () {
        if (life <= 0) {
            return true;
        }
        return false;
    }

    public String[] getRockText() { return rockText; }

    public String getRockText(int index) { return rockText[index]; }

    public void setRockText(String[] rockText) { this.rockText = rockText; }

    public String[] getRockQuestText() { return rockQuestText; }

    public GameObject clone() {
        Rock r = new Rock(x, y, imageRef);
        return r;
    }
}
