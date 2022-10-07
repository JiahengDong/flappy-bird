import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.lwjgl.system.CallbackI;

import java.lang.Math;
import java.util.Random;

public class steelPipe extends PipeSet{
    private final Image Flame = new Image("res/level-1/flame.png");
    private final double TopY = 100;
    private final double BottomY = 500;
    private int flameGapCount = 0;
    private int flameLastCount = 0;
    private final Image steelPipe = new Image("res/level-1/steelPipe.png");
    private final int flameGap = 10;
    private double topFlameY;
    private double bottomFlameY;
    private boolean flameExist = false;
    private boolean collision = false;
    private boolean destroyed = false;

    /**
     * this constructor creates a new steel pipe
     */
    public steelPipe(){
        super(new Image("res/level-1/steelPipe.png"));
        super.getRandom(TopY, BottomY);
        topFlameY = super.getTopPipeY()+steelPipe.getHeight() / 2 + flameGap;
        bottomFlameY = super.getBOTTOM_PIPE_Y() - steelPipe.getHeight() / 2 - flameGap;
    }

    /**
     * this method draws the flames
     */
    public void renderFlame(){
        Flame.draw(super.getPipeX(), topFlameY);
        Flame.draw(super.getPipeX(), bottomFlameY, super.getRotator());
    }

    /**
     * this method overrides the super class update method,
     * added the updates of the flames
     * @param timescale This is the accelerating period
     */
    @Override
    public void update(int timescale) {
        flameExist = false;
        if(flameLastCount <= 30){
            renderFlame();
            flameLastCount++;
            flameExist = true;
        }else if(flameGapCount++ == 20){
            flameLastCount = 0;
            flameGapCount = 0;
        }
        super.update(timescale);

    }

    /**
     * this method detects whether the bird collides with the flame
     * @param birdBox This is the bounding box of the bird
     * @return boolean This returns the collision situation
     */
    public boolean detectFlame(Rectangle birdBox){
        return flameExist && (birdBox.intersects(getTopFlameBox()) || birdBox.intersects(getBottomFlameBox()));
    }

    /**
     * this gets the rectangle box of the flame on the top pipe
     * @return Rectangle This returns the flame box
     */
    public Rectangle getTopFlameBox() {
        return Flame.getBoundingBoxAt(new Point(super.getPipeX(), topFlameY));

    }

    /**
     * this gets the rectangle box of the flame on the bottom pipe
     * @return Rectangle This returns the flame box
     */
    public Rectangle getBottomFlameBox() {
        return Flame.getBoundingBoxAt(new Point(getPipeX(), bottomFlameY));

    }

    /**
     * this method gets the collision situation
     * @return boolean This returns the collision situation of this pipe
     */
    public boolean getCollision(){
        return this.collision;
    }

    /**
     * this method gets the destroyed situation
     * @return boolean This returns the destroyed situation of this pipe
     */
    public boolean getDestroyed(){
        return this.destroyed;
    }

    /**
     * this method sets the collision situation
     * @param collision This is the collision situation
     */
    public void setCollision(boolean collision){
        this.collision = collision;
    }

    /**
     * this method sets the destroyed situation
     * @param destroyed This is whether the pipe has been destroyed
     */
    public void setDestroyed(boolean destroyed){
        this.destroyed = destroyed;
    }
}
