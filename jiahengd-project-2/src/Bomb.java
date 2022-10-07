import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Bomb extends Weapon{
    private final int shootRange = 50;
    private boolean hit;

    /**
     * this constructor creates a bomb object from Bomb class
     */
    public Bomb(){
        super(new Image("res/level-1/bomb.png"));
        super.shootRange = shootRange;
        hit = false;
    }

    @Override
    public boolean hitTarget(PipeSet pipe) {
        if(pipe instanceof plasticPipe){
            return super.hitTarget(pipe);
        }else if(pipe instanceof steelPipe){
            return super.hitTarget(pipe);
        }
        return false;
    }

    /**
     * this method gets whether the bomb hit some pipe
     * @return boolean This returns whether the bomb hit some pipe
     */
    public boolean getHit(){
        return this.hit;
    }

    /**
     * this method sets whether the bomb hit some pipe
     * @param hit This is whether the bomb hit something
     */
    public void setHit(boolean hit){
         this.hit = hit;
    }

}


