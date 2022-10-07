import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;
import java.util.Random;

public class plasticPipe extends PipeSet{
    private final int HIGH_GAP = 100;
    private final int MID_GAP = 300;
    private final int LOW_GAP = 500;
    private final Image PIPE_IMAGE = new Image("res/level/plasticPipe.png");
    private double[] TOP_PIPE_Y;
    private double length;
    private int index;
    private boolean collision;
    private boolean destroyed;

    /**
     * this constructor creates plastic pipe from plasticPipe class
     */
    public plasticPipe(){
        super(new Image("res/level/plasticPipe.png"));
        length = PIPE_IMAGE.getHeight() / 2;
        TOP_PIPE_Y = new double[]{HIGH_GAP - length, MID_GAP - length, LOW_GAP - length};
        Random random = new Random();
        index = random.nextInt(TOP_PIPE_Y.length);
        super.setTOP_PIPE_Y(TOP_PIPE_Y[index]);
        super.setBOTTOM_PIPE_Y(TOP_PIPE_Y[index] + 2*length + super.getPIPE_GAP());
        collision = false;
        destroyed = false;
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
