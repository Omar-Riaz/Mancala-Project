package com.example.views;

import com.example.views.concrete.EllipticStyle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Hole extends View implements ChangeListener {

    private ArrayList<Stone> stones;
    private char player;
    private boolean isPit;
    protected int numOfStones;

    int coils = 3;
    int radius = 50;

    double thetaMax = coils * 2 * Math.PI;

    // How far to step away from center for each side.
    double awayStep = radius / thetaMax;

    // distance between points to plot
    double chord = 10;

    double rotation = 5;

    @Override
    public void draw(Graphics2D g2) {
        this.setSize(new Dimension(getStyle().getWidth(), getStyle().getHeight()));
        Shape shape = getStyle().getShape();
        g2.setColor(getStyle().getColor());
        g2.draw(shape);
        initStones2(g2);
    }

    public Hole(char newPlayer, boolean _isPit, Style newStyle) {
        super(newStyle);
        player = newPlayer;
        isPit = _isPit;
    }

    public Hole(char newPlayer, boolean _isPit, Style newStyle, int _numOfStones) {
        super(newStyle);
        player = newPlayer;
        isPit = _isPit;
        numOfStones = _numOfStones;
//        initStones(numOfStones);
    }

    public void initStones2(Graphics2D g2) {

        Stone stone;

        int x = getWidth() / 2 - 8;


        int y = getHeight() / 2 - 8;


        int radiusStep = 10;


//        int diameter = 0; // diameter of the arc


        int arc = 180; // amount and direction of arc to sweep

        ArrayList<Stone> stonesDrawn = new ArrayList<>();
        int stoneDist = 8;
        for (int i = 0; i < numOfStones; i++ ) {

//            if (i % 2 == 1) // move the x position every other repetition
//                x -= 2 * radiusStep;
//
//
//            y -= radiusStep; //
//            double r = getWidth() / 2;
            int stoneRad = 8;
//            double randAngle = Math.random() * 2 * Math.PI;
//            double xRand = Math.sin(randAngle) * ((Math.random() * r) - stoneRad);
//            double yRand = Math.cos(randAngle) * ((Math.random() * r) - stoneRad);
//            x = getWidth() / 2 + (int) xRand;
//            y = getHeight() / 2 + (int) yRand;
            int mod = i % 4;
            if(mod == 0){
                x = x + (stoneDist);
                y = y + (stoneDist);
            }
            else if(mod == 1){
                x = x - (stoneDist);
                y = y + (stoneDist);
            }
            else if(mod == 2){
                x = x - (stoneDist);
                y = y - (stoneDist);
            }
            else{
                x = x + (stoneDist);
                y = y - (stoneDist);
                stoneDist = stoneDist + stoneRad;
            }
            stone = new Stone(x, y, new EllipticStyle(Color.BLACK, stoneRad, stoneRad));

            //measure to prevent overlapping stones from drawing
//            boolean draw = true;
//            for(Stone s: stonesDrawn){
////                System.out.println(s.getX() + ", " + x);
//                if(Math.abs(s.getX() - x) < 20 && Math.abs(s.getY() - y) < 20){
//                    i--;
//                    draw = false;
//                    break;
//                }
//            }
//            if(draw){
////                g2.fill(stone.getStyle().makeshape(x, y, stoneRad, stoneRad));
//                stonesDrawn.add(stone);
//            }
            g2.fill(stone.getStyle().makeshape(x, y, stoneRad, stoneRad));

//
//            g2.drawArc(x, y, diameter, diameter, 0, arc);
// draw the arc


            arc = -arc; // reverse the direction of the arc
        }

    }

//    public void initStones(Graphics2D g2) {
//
//        int centerX = getWidth() / 2;
//        int centerY = getHeight() / 2;
//
//        Stone stone;
//
//        for ( double theta = chord / awayStep; theta <= numOfStones; ) {
//
//
//            // How far away from center
//            double away = awayStep * theta;
//            //
//            // How far around the center.
//            double around = theta + rotation;
//            //
//            // Convert 'around' and 'away' to X and Y.
//            double x = centerX + Math.cos ( around ) * away;
//            double y = centerY + Math.sin ( around ) * away;
//            //
//            // Now that you know it, do it.
//
//            // to a first approximation, the points are on a circle
//            // so the angle between them is chord/radius
//            theta += chord / away;
//
//            stone = new Stone( (int) x, (int) y, new EllipticStyle(Color.RED, 10, 10));
////            Shape shape = new EllipticStyle(Color.RED, 10, 10).makeshape((int) x, (int) y, 10, 10);
//            Shape shape = new EllipticStyle(Color.RED, 10, 10).getShape();
////            add(stone);
//            g2.setColor(Color.RED);
//            g2.fill(shape);
////            g2.draw(shape);
//        }
//    }

    //when the state is changed by the model, redraw
    @Override
    public void stateChanged(ChangeEvent e) {

    }

    //using a spiral function, locate points and use them to draw stones
    public void drawStone(){

    }

    public void addStone() {
//        stones.add(new Stone());
        numOfStones++;
    }

    public void removeStone() {
//        stones.remove(0);
        numOfStones--;
    }

    public char getPlayer() {
        return player;
    }

    public boolean isPit() {
        return isPit;
    }

    public int getStones() {
        return numOfStones;
    }

    public int takeStones() {

        int temp = numOfStones;
        numOfStones = 0;
        return temp;
    }

    public void setNumberOfStones(int _numOfStones) {

        numOfStones = _numOfStones;
        repaint();
    }
}
