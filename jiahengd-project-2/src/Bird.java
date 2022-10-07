import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.lwjgl.system.CallbackI;

import java.lang.Math;

public class Bird {
    private Image WING_DOWN_IMAGE;
    private Image WING_UP_IMAGE;
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private int frameCount;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;
    private LifeBar lives;
    private double length;
    private boolean weaponHold;

    /**
     * this constructor is used to create Bird class object
     */

    public Bird(){
        this.WING_DOWN_IMAGE = new Image("res/level-0/birdWingDown.png");
        this.WING_UP_IMAGE = new Image("res/level-0/birdWingUp.png");
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
        lives = new LifeBar();
        length = WING_DOWN_IMAGE.getWidth();
        frameCount = 0;
        weaponHold = false;
    }

    /**
     * this method is getting the length of the birdImage
     * @return double This returns length of the bird Image
     */
    public double getBridLength(){
        return this.length;
    }

    /**
     * this method is updating the bird object's position and the wings
     * @param input This is the player's instruction
     * @param levelUp This is the level of game
     * @return Rectangle This returns the bird's rectangle box
     */
    public Rectangle update(Input input, boolean levelUp) {
        if(levelUp){
            this.WING_DOWN_IMAGE = new Image("res/level-1/birdWingDown.png");
            this.WING_UP_IMAGE = new Image("res/level-1/birdWingUp.png");
        }

        frameCount += 1;
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            WING_DOWN_IMAGE.draw(X, y);
        }
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            if (frameCount % SWITCH_FRAME == 0) {
                WING_UP_IMAGE.draw(X, y);
                boundingBox = WING_UP_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
            else {
                WING_DOWN_IMAGE.draw(X, y);
                boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;

        return boundingBox;
    }

    /**
     * this method is getting the current y position of the bird
     * @return double This returns the current y position of the bird
     */
    public double getY() {
        return y;
    }

    /**
     * this method gets the current x position of the bird
     * @return double This returns the current x position fo the bird
     */
    public double getX() {
        return X;
    }

    /**
     * this method gets the bird's rectangle box,
     * which can be used on detect collision and pick up weapons
     * @return Rectangle This return the bird Image rectangle box
     */
    public Rectangle getBox() {
        return boundingBox;
    }

    /**
     * this method reset the bird's position and the count of the frame
     */
    public void reset(){
        this.frameCount = 0;
        this.y = this.INITIAL_Y;
    }

    /**
     * this method detects whether the bird fly out of the bound
     * @return boolean This returns whether the bird out of bound
     */
    public boolean birdOutOfBound() {
        return (y > Window.getHeight()) || (y < 0);
    }

    /**
     * this method gives the access of life bar of the bird
     * @return LifeBar This return the bird's current life bar
     */
    public LifeBar getLifeBar(){
        return lives;
    }

    /**
     * this method detects whether the bird hold a weapon or not
     * @return boolean This returns the bird's weapon hold situation
     */
    public boolean getWeaponHold(){
        return this.weaponHold;
    }

    /**
     * this method sets the bird's weapon hold situation
     * @param hold This parameter is the current bird holds weapon or not
     */
    public void setWeaponHold(boolean hold){
        this.weaponHold = hold;
    }
}