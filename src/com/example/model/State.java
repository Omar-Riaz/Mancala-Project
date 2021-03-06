package com.example.model;

import com.example.model.Hole;
import com.example.views.Pit;
import com.example.views.View;
import javafx.scene.control.TextFormatter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

/**
 * Model of the program
 */
public class State implements Cloneable {
    private ArrayList<Hole> holes;
    private char playerTurn;
    private State previousState;
    private int scoreA;
    private int scoreB;
    private ArrayList<ChangeListener> changeListeners;
    private boolean gameOver;

    private int undoCount;


    /**
     * State constructor, initializes holes, playerTurn, prev state and change listeners
     */
    public State(){
        holes = new ArrayList<>();
        playerTurn = 'A';
        previousState = (State) this.clone();
        changeListeners = new ArrayList<>();
    }

    public State(ArrayList<Hole> _holes) {
        holes = _holes;
        playerTurn = 'A';
        previousState = (State) this.clone();
    }

    /**
     * Attaches views to model
     * @param view is a view to be hold and updated in state
     */
    //attach a view to the model
    public void attach(ChangeListener view){
        changeListeners.add(view);
    }

    /**
     * Update all of the view attached to the model
     */
    public void update(){
        for(ChangeListener c: changeListeners){
            c.stateChanged(new ChangeEvent(this));      //pass the state as the changeEvent
        }
    }

    /**
     * Used for undo
     */
    public void updateAllHoles(){
        for(Hole hole: holes){
            hole.update();
        }
    }

    /**
     * Implements "move stones to mancala from opposite pit" logic
     * @param stones is amount of stones to be moved
     * @param player indicates whose mancala should be appended
     */

    public void moveToMancala(int stones, char player) {
        Hole hole;
        if(player == 'A') {
            hole = holes.get(7);
        } else {
            hole = holes.get(0);
        }

        while(stones > 0) {
            hole.addStone();
            stones--;
        }
    }

    /**
     * Indicated player whose turn is at this point in program
     * @return player label
     */
    public char getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Switches indicator of current player
     */
    public void changeTurn() {
        if(playerTurn == 'A')
            playerTurn = 'B';
        else
            playerTurn = 'A';
    }

    /**
     * Set amount of stones in pit
     * @param numOfStones to set
     */
    public void setNumberOfStones(int numOfStones) {
        for(Hole hole: holes) {
            if(hole.isPit()) {
                hole.setNumberOfStones(numOfStones);
            }
        }
    }

    public void increaseUndoCount() {undoCount++;}
    public void decreaseUndoCount() {undoCount--;}

    public void setUndoCount(int undoCount){
        this.undoCount = undoCount;
    }

    public void incrementUndoCount(){
        this.undoCount++;
    }

    public State getPreviousState(){
        return previousState;
    }

    /**
     * Updates previous state
     * @param state is current state
     */
    public void setPreviousState(State state){
        previousState = state;
    }

    /**
     * Implements Clonable
     * @return this instance
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Implements undo button logic
     */
    public void undo() {
        System.out.println(undoCount);
        //TODO Implement undo logic here
        if(undoCount >= 3) {
            return;
        } else {
            undoCount++;
//            this = (State) previousState.clone();
            this.holes = previousState.getHoles();
            this.scoreA = previousState.scoreA;
            this.scoreB = previousState.scoreB;
            this.playerTurn = previousState.playerTurn;
            update();
            updateAllHoles();
        }
    }

    /**
     * Implements single move logic
     * @param selectedPit by user
     * @return
     */
    public int move(int selectedPit) {

        selectedPit %= 14;
        ArrayList<Hole> holes = this.getHoles();

//<<<<<<< HEAD
//        long start;
//
//        char player = state.getPlayerTurn();
//=======
        char player = this.getPlayerTurn();
        int numOfStones = holes.get(selectedPit).takeStones();
        holes.get(selectedPit).update();
        while (numOfStones > 0) {
            selectedPit++;
            selectedPit %= holes.size();
            Hole hole = holes.get(selectedPit);
            if (((hole.getPlayer() == player && !hole.isPit())) || hole.isPit()) {
                holes.get(selectedPit).addStone();
                numOfStones--;
                hole.update();
//                hand.takeFromHand();
//                repaint();
//                start = System.currentTimeMillis();
//                while( start + 300 >  System.currentTimeMillis() );
            }
        }

        // Calculate opposite pit formula n + (7 - n) * 2 = k
        int oppositePit = selectedPit + (7 - selectedPit) * 2;

        if ((player == 'A' && selectedPit == 7) || (player == 'B' && selectedPit == 0))
            return -2;

        if (holes.get(selectedPit).getPlayer() == player && holes.get(oppositePit).getStones() >= 1 &&
                holes.get(selectedPit).getStones() == 1) {
            int stones = holes.get(oppositePit).takeStones();
            holes.get(oppositePit).update();
            stones += holes.get(selectedPit).takeStones();
            this.moveToMancala(stones, player);
            System.out.println("Transfer opposite stones to your mancala");
            return -1;
//<<<<<<< HEAD
//
//        } else if(holes.get(selectedPit).getStones() > 1) {
//            System.out.println("Still " + state.getPlayerTurn() + "'s turn!");
//            return selectedPit;
//        }
//        else{
//            System.out.println("Player " +  player + ", please select your pits");
//=======
//>>>>>>> 61dae2aa5008e0d9813c3f3c32c745dde8fa7624
        }

//        repaint();
        return -1;
    }

    /**
     * Manages multiple moves if any
     * @param startingPit is an index of user-selected pit
     */
    public void turn(int startingPit) {

        if (this.getPlayerTurn() != this.getHoles().get(startingPit).getPlayer() ||
                startingPit > this.getHoles().size())
            return;
//        hand.addToHand(this.getHoles().get(startingPit).getStones());

        startingPit = move(startingPit);
//        repaint();

        scoreCount();

        if(checkPits(true) || checkPits(false)) {

            System.out.println("Game over");
            System.out.println("Winner is player " + this.getWinningPlayer() + " with score of " + this.getMaxScore());
            gameOver = true;
        }

        if (startingPit == -1) {
            this.changeTurn();
//            displayTurnPopUp(this.getPlayerTurn());
            resetUndoCounter();
            setPreviousState(this);
//            displayTurnPopUp(state.getPlayerTurn());
            System.out.println("Now it's " + this.getPlayerTurn() + "'s turn!");
        }
        update();
    }

    /**
     * Identifies if one's side of pits is empty
     * @param first for player A
     * @return
     */
    public boolean checkPits(boolean first) {

        int c = 1;

        if(!first) {
            c += 7;
        }

        int limit = c + 6;

        ArrayList<Hole> holes = this.getHoles();
        for(; c < limit; c++) {
            if( holes.get(c).getStones() != 0 )
                return false;
        }

        return true;
    }

    /**
     * Updates player scores
     */
    //Keep score
    public void scoreCount(){
        if(this.getPlayerTurn() == 'A')
//            scoreA.setText("Score A: " + Integer.toString(this.getHoles().get(7).getStones()));
            scoreA = this.getHoles().get(0).getStones();
        else if (this.getPlayerTurn() == 'B')
//            scoreB.setText("Score B: " + Integer.toString(this.getHoles().get(0).getStones()));
            scoreB = this.getHoles().get(7).getStones();
        update();
    }

    /**
     * Holes accessor
     * @return holes
     */
    public ArrayList<Hole> getHoles(){
        return holes;
    }

    /**
     * Identifies maximum score in the game
     * @return maximum score
     */
    public int getMaxScore() {
        return Math.max(this.getHoles().get(0).getStones(), this.getHoles().get(7).getStones());
    }

    /**
     * Returns winning player label
     * @return player label
     */
    public char getWinningPlayer() {
        return this.getHoles().get(0).getStones() == getMaxScore() ? 'B' : 'A';
    }

    /**
     * Resets undo counter
     */
    public void resetUndoCounter() {
        undoCount = 0;
        previousState = (State) this.clone();
    }

    /**
     * @return A's score
     */
    public int getScoreA(){
        return scoreA;
    }

    /**
     * @return B's score
     */
    public int getScoreB(){
        return scoreB;
    }

    /**
     * Hole array mutator
     * @param hole
     */
    public void addHole(Hole hole){
        holes.add(hole);
    }

    /**
     * Hole array mutator for specific space
     * @param index
     * @param hole
     */
    public void addHole(int index, Hole hole){
        holes.add(index, hole);
    }

    /**
     * Identifies if game is over
     * @return true if over
     */
    public boolean isGameOver() {
        return gameOver;
    }
}