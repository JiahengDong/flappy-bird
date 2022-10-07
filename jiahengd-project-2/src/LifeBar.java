import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.lwjgl.system.CallbackI;

import java.lang.Math;

public class LifeBar {
    private final Image FullHeart = new Image("res/level/fullLife.png");
    private final Image EmptyHeart = new Image("res/level/noLife.png");
    private final int TopLeftX = 100;
    private final int TopLeftY = 15;
    private final int Gap = 50;
    private int levelLife;
    private int lives;
    private boolean hasSetLives;

    /**
     * this constructor creates a life bar of the bird
     */
    public LifeBar(){
        levelLife = 3;
        lives = levelLife;
        hasSetLives = false;
    }

    /**
     * this method decrease the life of the life bar
     */
    public void loseLife(){
        this.lives--;
    }

    /**
     * this method draw the current life bar situation
     */
    public void renderLifeBar(){
        int i = 0;
        double X = TopLeftX;
        while(i < this.lives){
            FullHeart.drawFromTopLeft(TopLeftX + Gap*i, TopLeftY);
            i++;
        }
        X = TopLeftX + Gap*i;
        i = 0;
        while(i < levelLife - this.lives){
            EmptyHeart.drawFromTopLeft(X + Gap*i,TopLeftY);
            i++;
        }
    }

    /**
     * this method get the current life of the bird
     * @return int This return the current number of full heart of the life bar
     */
    public int getLife(){
        return this.lives;
    }

    /**
     * this method sets the initial lives of each level
     * @param levelUp This is the level situation of the game
     */
    public void setLevelLife(boolean levelUp){
        if(levelUp){
            this.levelLife = 6;
            if(!hasSetLives){
                this.lives = 6;
            }
        }
    }

    /**
     * this method sets the whether the computer has set lives
     * @param has This is whether the live has been set
     */
    public void setHasSetLives(boolean has){
        hasSetLives = has;
    }

}
