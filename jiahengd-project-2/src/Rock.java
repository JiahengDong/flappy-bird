import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;

public class Rock extends Weapon {
    private final int shootRange = 25;
    private boolean hit;

    /**
     * this constructor creates a rock object
     */
    public Rock() {
        super(new Image("res/level-1/rock.png"));
        super.shootRange = shootRange;
        hit = false;
    }

    /**
     * this method detect whether the rock hits its own target
     * @param pipe This is the coming up pipe
     * @return boolean This returns whether the rock hit its own target
     */
    public boolean rockHitTarget(PipeSet pipe){
        if (pipe instanceof plasticPipe){
            return super.hitTarget(pipe);
        }else{
            return false;
        }
    }

    /**
     * this method gets whether the rock has hit something
     * @return boolean This returns the hit situation
     */
    public boolean getHit(){
        return this.hit;
    }

    /**
     * this method sets the hit situation
     * @param hit This is whether the rock has hit something
     */
    public void setHit(boolean hit){
            this.hit = hit;
    }
}


