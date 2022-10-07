import bagel.*;
import bagel.util.Rectangle;
import org.lwjgl.system.CallbackI;
import java.util.ArrayList;

/**
 * @author jiaheng dong 1166436
 *
 * changes to make game more playable:
 * 1. change pipe initial speed to 5 pixels
 * 2. Flames are rendered 10 pixels from the steel pipe opening
 *  (10 pixels down from top pipe and 10 pixels up from bottom pipe)
 * 3. make pipe image cover part of the flame image, but the centre of the flame is
 * as required (in demo, part of the flame overlap the pipe)
 * 4. make speed change maximum to 5(if too fast, the player cannot play),
 * minimum to 1(initial speed),and every time speed up, increase by 50%,
 * every time decrease, decrease by 50%
 * 5. Level 0 final screen lasts 150 frames before Level 1 initial screen
 * (with 'PRESS 'S' TO SHOOT') is rendered
 * 6. Flame shoots from steel pipes every 20 frames
 * 7. Flame lasts for 30 frames every time it is shot
 *
 */
class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_IMAGE_LEVEL0 = new Image("res/level-0/background.png");
    private final Image BACKGROUND_IMAGE_LEVEL1 = new Image("res/level-1/background.png");
    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final String LEVEL_UP = "LEVEL-UP!";
    private final String SHOOT_MSG = "PRESS 'S' TO SHOOT";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private Bird bird;
    private ArrayList<plasticPipe> plasticPipes;
    private ArrayList<Weapon> weapons;
    private int score;
    private boolean gameOn;
    private boolean win;
    private int pipeFrameCount;
    private int levelUpFrameCount;
    private boolean levelUp;
    private ArrayList<PipeSet> pipes;
    private final int MIN_SCALE = 1;
    private final int MAX_SCALE = 5;
    private int timeScale;
    private int pipeAppearGap;

    /**
     * constructor creates a new shadow flap object
     */
    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird();
        score = 0;
        gameOn = false;
        win = false;
        plasticPipes = new ArrayList<plasticPipe>();
        weapons = new ArrayList<Weapon>();
        pipeFrameCount = 0;
        levelUpFrameCount = 0;
        levelUp = false;
        pipes = new ArrayList<PipeSet>();
        timeScale = 1;
        pipeAppearGap = 100;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if(score < 4 && !levelUp){
            BACKGROUND_IMAGE_LEVEL0.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            pipeFrameCount++;
            gameSet(input);

            // game is active
            if (gameOn && bird.getLifeBar().getLife()!=0) {
                updateTimeScale(input);
                renderScore();
                bird.update(input, levelUp);
                bird.getLifeBar().setLevelLife(levelUp);
                bird.getLifeBar().renderLifeBar();

                //add plastic pipes for level 0
                addPlasticPipes(plasticPipes, pipeFrameCount, timeScale);

                //update each plastic pipe
                for(plasticPipe pipe: plasticPipes){
                    updatePlasticPipes(pipe,bird,timeScale);
                }
                renderScore();
                bird.getLifeBar().renderLifeBar();
            }


        }else if(score == 4 && levelUpFrameCount < 150){
            BACKGROUND_IMAGE_LEVEL0.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            pipeFrameCount = 0;
            levelUpFrameCount++;
            renderLevelUp();
            gameOn = false;
        }else{
            BACKGROUND_IMAGE_LEVEL1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            pipeFrameCount++;
            if(!levelUp){
                score = 0;
                timeScale = 1;
                levelUp = true;
                bird.reset();
            }

            gameSet(input);

            if (gameOn && bird.getLifeBar().getLife()!=0 && !win) {
                updateTimeScale(input);
                renderScore();

                bird.update(input, levelUp);
                bird.getLifeBar().setLevelLife(levelUp);
                bird.getLifeBar().setHasSetLives(true);
                bird.getLifeBar().renderLifeBar();

                //add different pipes and weapons
                addDiffPipes(pipes, pipeFrameCount, timeScale);
                addDiffWeapons(weapons, pipeFrameCount, timeScale);

                //update each pipe
                for(PipeSet pipe: pipes){
                    if(pipe instanceof plasticPipe){
                        updatePlasticPipes(pipe,bird, timeScale);
                    }else if(pipe instanceof steelPipe){
                        updateSteelPipes(pipe, bird, timeScale);
                    }
                }

                //update each weapon
                for(Weapon w: weapons){

                    if(w instanceof Rock){
                        findRockTargetHit(pipes, (Rock) w);
                        w.update(input, bird, ((Rock) w).getHit(), w.shootRange, timeScale);
                    }else if(w instanceof Bomb){
                        findBombTargetHit(pipes, (Bomb) w);
                        w.update(input, bird, ((Bomb) w).getHit(), w.shootRange, timeScale);
                    }
                }

                renderScore();
                bird.getLifeBar().renderLifeBar();
            }




        }

    }

    /**
     * this method detect whether the rock has attacked a plasitc pipe
     * @param pipes This is the list of the pipes
     * @param w This is the rock
     */
    public void findRockTargetHit(ArrayList<PipeSet> pipes, Rock w){
        for(PipeSet pipe: pipes){
            if(pipe instanceof plasticPipe && !w.getHit() && !((plasticPipe) pipe).getDestroyed() && !((plasticPipe) pipe).getCollision()){
                w.setHit(w.rockHitTarget(pipe));
                if(w.getHit()){
                    updateScore(pipe,bird);
                }
                ((plasticPipe) pipe).setDestroyed(w.rockHitTarget(pipe));
            }
        }

    }

    /**
     * this method detects whether the bomb attacked any pipe
     * @param pipes This is the list of pipe
     * @param w This is the bomb
     */
    public void findBombTargetHit(ArrayList<PipeSet> pipes, Bomb w){
        for(PipeSet pipe: pipes){
            if(pipe instanceof plasticPipe && !w.getHit() && !((plasticPipe) pipe).getDestroyed() && !((plasticPipe) pipe).getCollision()){
                w.setHit(w.hitTarget(pipe));
                if(w.getHit()){
                    updateScore(pipe,bird);
                }
                ((plasticPipe) pipe).setDestroyed(w.hitTarget(pipe));
            }else if(pipe instanceof steelPipe && !w.getHit() && !((steelPipe) pipe).getDestroyed() && !((steelPipe) pipe).getCollision()){
                w.setHit(w.hitTarget(pipe));
                if(w.getHit()){
                    updateScore(pipe,bird);
                }
                ((steelPipe) pipe).setDestroyed(w.hitTarget(pipe));
            }
        }

    }


    /**
     * this method draws the instruction screen of level 0, and detect the player press space or not
     * @param input The press button
     */
    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }

    /**
     * this method draw the game over screen for both level
     */
    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * this method draw the win screen for level 1
     */
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * this method draw the level up screen
     */
    public void renderLevelUp(){
        FONT.drawString(LEVEL_UP, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        FONT.drawString(SHOOT_MSG, (Window.getWidth()/2.0-(FONT.getWidth(SHOOT_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * this method detects the collision of pipes and bird
     * @param birdBox This is the bird bounding box
     * @param topPipeBox This is the top pipe bounding box
     * @param bottomPipeBox This is the bottom pipe bounding box
     * @return boolean This returns the collision situation
     */
    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }

    /**
     * this method draw the scores
     */
    public void renderScore(){
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);
    }

    /**
     * this method updates scores, gain point by destroying pipe or passing pipe
     * @param pipe This is the pipe
     * @param bird This is the bird
     */
    public void updateScore(PipeSet pipe, Bird bird) {
        if (((bird.getX() > pipe.getTopBox().right()) && !pipe.getGainPointFromCross())){
            score += 1;
            pipe.setGainPointFromCross(true);
            //pipe.gainPointFromDestroy = true;
        }
        if(pipe instanceof plasticPipe){
            if(((plasticPipe) pipe).getDestroyed() && !pipe.getGainPointFromDestroy()){
                score+=1;
                pipe.setGainPointFromDestroy(true);
            }
        }else if(pipe instanceof steelPipe){
            if(((steelPipe) pipe).getDestroyed() && !pipe.getGainPointFromDestroy()){
                score++;
                pipe.setGainPointFromDestroy(true);
            }
        }
        if(score == 30){
            win = true;
        }

    }

    /**
     * this method sets the basic game logic
     * @param input This is the press button
     */
    public void gameSet(Input input){
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            renderInstructionScreen(input);
        }

        // game over
        if (bird.getLifeBar().getLife() == 0){
            renderGameOverScreen();
        }

        if(win){
            renderWinScreen();
        }

        //out-of-bound
        if(bird.birdOutOfBound()){
            bird.getLifeBar().loseLife();
            //bird.lives.update(life0);
            bird.reset();
        }
    }

    /**
     * this method add plastic pipe based on adding speed
     * @param plasticPipes This is the list of plastic pipe
     * @param pipeFrameCount This is the count number of the frames
     * @param timeScale This is the speed change period
     */
    public void addPlasticPipes(ArrayList<plasticPipe> plasticPipes, int pipeFrameCount, int timeScale){
        pipeAppearGap = 100;

        for(int i=1; i<timeScale; i++){
            pipeAppearGap = (int)Math.round(pipeAppearGap / 1.5);
        }

        if(pipeFrameCount % pipeAppearGap == 0){
            plasticPipes.add(new plasticPipe());
        }
    }

    /**
     * this method updates plastic pipe's collision condition, destroy condition and position
     * @param pipe This is the pipe
     * @param bird This is the bird
     * @param timeScale This is the time change period
     */
    public void updatePlasticPipes(PipeSet pipe, Bird bird, int timeScale){
        if(!((plasticPipe) pipe).getCollision() && !((plasticPipe) pipe).getDestroyed()){
            pipe.update(timeScale);
            Rectangle topPipeBox = pipe.getTopBox();
            Rectangle bottomPipeBox = pipe.getBottomBox();
            ((plasticPipe) pipe).setCollision(detectCollision(bird.getBox(), topPipeBox, bottomPipeBox));
            if(((plasticPipe) pipe).getCollision()){
                bird.getLifeBar().loseLife();
            }else{
                updateScore(pipe, bird);
            }
        }else if(((plasticPipe) pipe).getDestroyed()){
            updateScore(pipe, bird);
        }
    }

    /**
     * this method updates steel pipe's collision condition, destroy condition and position
     * @param pipe This is the pipe
     * @param bird This is the bird
     * @param timeScale This is the time change period
     */
    public void updateSteelPipes(PipeSet pipe, Bird bird, int timeScale){
        if(!((steelPipe) pipe).getCollision() && !((steelPipe) pipe).getDestroyed()){
            pipe.update(timeScale);
            Rectangle topPipeBox = pipe.getTopBox();
            Rectangle bottomPipeBox = pipe.getBottomBox();
            ((steelPipe) pipe).setCollision(detectCollision(bird.getBox(), topPipeBox, bottomPipeBox) || ((steelPipe) pipe).detectFlame(bird.getBox()));
            if(((steelPipe) pipe).getCollision()){
                bird.getLifeBar().loseLife();
            }else{
                updateScore(pipe, bird);
            }
        }else if(((steelPipe) pipe).getDestroyed()){
            updateScore(pipe, bird);
        }
    }

    /**
     * this method add different pipes randomly in the pipe list, based on changed adding speed
     * @param pipes This is the pipe
     * @param pipeFrameCount This is the number of count of frames
     * @param timeScale This is the time change period
     */
    public void addDiffPipes(ArrayList<PipeSet> pipes, int pipeFrameCount, int timeScale){
        pipeAppearGap = 100;

        for(int i=1; i<timeScale; i++){
            pipeAppearGap = (int)Math.round(pipeAppearGap / 1.5);
        }

        if(pipeFrameCount % pipeAppearGap == 0){
            if(Math.random()>0.5){
                pipes.add(new steelPipe());
            }else{
                pipes.add(new plasticPipe());
            }

        }
    }

    /**
     * this method add different weapons randomly
     * @param weapon This is the weapon list
     * @param pipeFrameCount This is the number of frame count
     * @param timeScale This is the speed change period
     */
    public void addDiffWeapons(ArrayList<Weapon> weapon, int pipeFrameCount, int timeScale){
        pipeAppearGap = 100;

        for(int i=1; i<timeScale; i++){
            pipeAppearGap = (int)Math.round(pipeAppearGap / 1.5);
        }

        if(pipeFrameCount > pipeAppearGap && (pipeFrameCount-pipeAppearGap/2) % pipeAppearGap == 0){
            if(Math.random()>0.5){
                weapons.add(new Rock());
            }else{
                weapons.add(new Bomb());
            }
        }
    }

    /**
     * this method update speed changing period based on Input button
     * @param input This is the input button
     */
    public void updateTimeScale(Input input){
        if(input.wasPressed(Keys.K) && timeScale > MIN_SCALE){
            timeScale--;
        }
        if(input.wasPressed(Keys.L) && timeScale < MAX_SCALE){
            timeScale++;
        }
    }


}
