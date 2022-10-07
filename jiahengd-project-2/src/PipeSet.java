import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipeSet {
    private Image PIPE_IMAGE;
    private final int PIPE_GAP = 168;
    private double PIPE_SPEED;
    private double TOP_PIPE_Y;
    private double BOTTOM_PIPE_Y;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX;
    private boolean gainPointFromCross;
    private boolean gainPointFromDestroy;

    /**
     * this constructor creates a new pipe based on parameter
     * @param PIPE_IMAGE This is the Image of the new pipe
     */
    public PipeSet(Image PIPE_IMAGE){
        this.PIPE_IMAGE = PIPE_IMAGE;
        PIPE_SPEED = 5;
        pipeX = Window.getWidth();
        gainPointFromCross = false;
        gainPointFromDestroy = false;
    }

    /**
     * this method gets the rotation situation
     * @return DraOptions This returns the rotator of the super class
     */
    public DrawOptions getRotator(){
        return this.ROTATOR;
    }

    /**
     * this method gets the gap of the pipe
     * @return int This returns the gap of the pipe
     */
    public int getPIPE_GAP(){
        return this.PIPE_GAP;
    }

    /**
     * this method gets the y position of the top pipe
     * @return double This returns the y position of the top pipe
     */
    public double getTopPipeY(){
        return this.TOP_PIPE_Y;
    }

    /**
     * this method sets the y position of the top pipe
     * @param top_pipe_y This is the current y position of the top pipe
     */
    public void setTOP_PIPE_Y(double top_pipe_y){
        this.TOP_PIPE_Y = top_pipe_y;
    }

    /**
     * this method gets the y position of the bottom pipe
     * @return double This returns the y position of the bottom pipe
     */
    public double getBOTTOM_PIPE_Y(){
        return this.BOTTOM_PIPE_Y;
    }

    /**
     * this method gets the x position of the pipe
     * @return
     */
    public double getPipeX(){
        return this.pipeX;
    }

    /**
     * this method gets whether the point has gained from passing the pipe
     * @return boolean This returns the result of gain points of passing the pipe
     */
    public boolean getGainPointFromCross(){
        return gainPointFromCross;
    }

    /**
     * this method sets whether the point has gained from passing the pipe
     * @param gain This is the result of gain points of passing the pipe
     */
    public void setGainPointFromCross(boolean gain){
        this.gainPointFromCross = gain;
    }

    /**
     * this method gets whether the point has gained from destroying the pipe
     * @return boolean This returns the result of gain points of destroying the pipe
     */
    public boolean getGainPointFromDestroy(){
        return gainPointFromDestroy;
    }

    /**
     * this method sets whether the point has gained from destroying the pipe
     * @param gain This is the result of gain points of destroying the pipe
     */
    public void setGainPointFromDestroy(boolean gain){
        this.gainPointFromDestroy = gain;
    }

    /**
     * this method sets y position of the bottom pipe
     * @param bottom_pipe_y This is the current y position of the bottom pipe
     */
    public void setBOTTOM_PIPE_Y(double bottom_pipe_y){
        this.BOTTOM_PIPE_Y = bottom_pipe_y;
    }

    /**
     * this method draws the top and bottom pipe image
     */
    public void renderPipeSet() {
        PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
        PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
    }

    /**
     * this method updates the pipe's position and moving speed
     * @param timeScale This is the accelerating period
     */
    public void update(int timeScale) {
        renderPipeSet();
        PIPE_SPEED = 5;
        for(int i=1; i<timeScale; i++){
            PIPE_SPEED = PIPE_SPEED * 1.5;
        }
        pipeX -= PIPE_SPEED;

    }

    /**
     * this method gets the rectangle box of the top pipe
     * @return Rectangle This returns the box of the top pipe
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));

    }

    /**
     * this method gets the rectangle box of the bottom pipe
     * @return Rectangle This returns the box of the bottom pipe
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));

    }

    /**
     * this method sets the random y position of top pipe and bottom pipe in a range
     * @param min This is the lower bound of the range
     * @param max This is the upper bound of the range
     */
    public void getRandom(double min, double max){
        double length =  PIPE_IMAGE.getHeight();
        TOP_PIPE_Y = Math.random() * (max - min) + min -length / 2;
        BOTTOM_PIPE_Y = length + TOP_PIPE_Y + PIPE_GAP;
    }



}
