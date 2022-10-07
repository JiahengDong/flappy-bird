import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.lwjgl.system.CallbackI;

import java.lang.Math;

public class Weapon {
    private final Image WEAPON_IMAGE;
    private final int weaponTopY = 100;
    private final int weaponBottomY = 500;
    private double weaponX = Window.getWidth();
    private double shootingSpeed = 5;
    private double movingSpeed = 5;
    protected int shootRange;
    private double weaponY;
    protected boolean pickedUp = false;
    private int frameCount = 0;
    private boolean isShoot = false;

    /**
     * this constructor creates a new weapon with given images
     * @param WEAPON_IMAGE This is the image of the weapon
     */
    public Weapon(Image WEAPON_IMAGE) {
        this.WEAPON_IMAGE = WEAPON_IMAGE;
        weaponY  = Math.random() * (weaponBottomY - weaponTopY) + weaponTopY;
    }

    /**
     * this method draws the weapon
     */
    public void renderWeapons(){
        WEAPON_IMAGE.draw(weaponX,weaponY);
    }

    /**
     * this method updates the weapon position, moving speed, and moving direction
     * @param input This is the parameter to check whether the 's' has been pressed
     * @param bird This is the bird object
     * @param hit This is whether the weapon has hit something
     * @param shootRange This is the shooting range of this weapon
     * @param timeScale This is the accelerating period
     */
    public void update(Input input, Bird bird, boolean hit, int shootRange, int timeScale){
        shootingSpeed = 5;
        movingSpeed = 5;
        for(int i=1; i<timeScale; i++){
            shootingSpeed = shootingSpeed * 1.5;
            movingSpeed = movingSpeed * 1.5;
        }
        if(input.wasPressed(Keys.S) && pickedUp){
            isShoot = true;
        }

        if(!pickedUp && !hit){
            renderWeapons();
            weaponX-=movingSpeed;
            pickUp(bird);
        }else if(pickedUp && !isShoot){
            if(!hit){
                followBird(bird);
                renderWeapons();
            }else{
                bird.setWeaponHold(false);
                pickedUp = false;
            }

        }else if(isShoot && frameCount*shootingSpeed < shootRange){
            frameCount++;
            bird.setWeaponHold(false);
            if(!hit){
                renderWeapons();
                weaponX+=shootingSpeed;
            }else{
                frameCount = 0;
                isShoot = false;
                pickedUp = false;
            }
        }

    }

    /**
     * this method gets the bounding box of the weapon
     * @return Rectangle This returns the rectangle box of the weapon
     */
    public Rectangle getWeaponBoc(){
        return WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
    }

    /**
     * this method detects whether the bird has collided with the weapon
     * @param bird This is the bird
     */
    public void pickUp(Bird bird){
        if (bird.getBox().intersects(getWeaponBoc()) && !bird.getWeaponHold()){
            pickedUp = true;
            bird.setWeaponHold(true);
        }

    }

    /**
     * this method combines the weapon with bird if the bird has picked up the weapon
     * @param bird This is the bird
     */
    public void followBird(Bird bird){
        weaponX = bird.getX() + bird.getBridLength()/2 + 5;
        weaponY = bird.getY();
    }

    /**
     * this method detect whether the weapon hit the target pipe
     * @param pipe This is the pipe
     * @return boolean This returns whether the weapon hit the target pipe
     */
     public boolean hitTarget(PipeSet pipe){
        return getWeaponBoc().intersects(pipe.getTopBox())
                || getWeaponBoc().intersects(pipe.getBottomBox());
     }

}


