package projectzelda.game;

import projectzelda.engine.*;

import java.awt.*;


public class Rock extends RectangularGameObject
{

    public HealthBar healthBar;
    public int  radius = 20;
    protected double life = 1.0;
    protected static final Color REDDER = new Color(160, 40, 20);
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

    public Rock(double x, double y, ImageRef imageref)
        {
            super(x, y, 0, 0, 128, 46, null);
            this.isMoving = false;
            this.imageRef = imageref;
            healthBar = new HealthBar(0, 0, 80, 5);
            adjustHealthBarPosition();
        }

    // Invisible
    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.draw(this);
        healthBar.draw(gs, tick);
    }

    public int type() { return Const.Type.ROCK.ordinal(); }

    protected void adjustHealthBarPosition() {
        int healthBarX = (int) (x - healthBar.width /10);
        int healthBarY = (int) (y - radius + 15 * healthBar.height);
        healthBar.x = healthBarX;
        healthBar.y = healthBarY;
    }

    public void hit() {
        // every hit decreases life
        if (((RPGWorld) world).questState == QuestState.BOSS) {
            life -= 0.21;
            healthBar.health = life;
            color = REDDER;
            colorCooldown = COLOR_COOLDOWN;

            // if Goblin is dead, delete it
            if (life <= 0) {
                die();

            }
        } else {
            world.gameState = GameState.DIALOG;
            // if const.type is rock game freezes, using bones or goblin seems to work ok though
            ChatBoxButton chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "It might be bad idea to break this.",this);
            world.chatBoxObjects.add(chatBox);
        }

    }

    public void die() {


        this.isLiving = false;
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

}
