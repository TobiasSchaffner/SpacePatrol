package com.mygdx.game.shared.position.ship;

/**
 * Created by platypus on 02.06.16.
 */
public class BaseShip implements Ship{

    /**
     * The char sequence representing the filters of the ShipDecorator.
     */
    private static final String SEQUENZ = "";

    /**
     * The Maximum Health of the Ship.
     */
    private static final int MAX_HEALTH = 100;

    /**
     * The Speed of the Ship.
     */
    private static final double SPEED = 0.2;

    /**
     * The Code of the Asset that is shown for the ship.
     */
    private static final int IMAGE = 1;

    /**
     * The speed of a shot fired by that ship.
     */
    private static final double SHOT_SPEED = 0.5;

    /**
     * The damage one shot inflicts.
     */
    private static final int SHOT_DAMAGE = 1;

    /**
     * The Code of the Asset that is shown for one shot fired by that ship.
     */
    private static final int SHOT_IMAGE = 1;

    //Name
    public String toString(){
        return SEQUENZ;
    }

    //Ship
    public int getMaxHealth(){
        return MAX_HEALTH;
    }

    public double getSpeed(){
        return SPEED;
    }

    public int getImage(){
        return IMAGE;
    }

    //Fire
    public double getShotSpeed(){
        return SHOT_SPEED;
    }

    public int getShotDamage(){
        return SHOT_DAMAGE;
    }

    public int getShotImage(){
        return SHOT_IMAGE;
    }
}
