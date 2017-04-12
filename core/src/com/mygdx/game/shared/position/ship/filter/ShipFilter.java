package com.mygdx.game.shared.position.ship.filter;

import com.mygdx.game.shared.position.ship.Ship;

/**
 * Created by platypus on 02.06.16.
 */
abstract class ShipFilter implements Ship{

    /**
     * The underlying ship.
     */
    private final Ship ship;

    /**
     * The Sequence of Filters.
     */
    private final String sequenz;

    /**
     * Creates a new ShipFilter.
     * @param ship The underlying ship.
     * @param id The Char identifying the filter.
     */
    ShipFilter(Ship ship, char id){
        assert ship != null;
        assert id != '\0';

        this.ship = ship;
        sequenz = ship.toString() + id;
    }

    @Override
    public double getShotSpeed(){
        return ship.getShotSpeed();
    }

    @Override
    public int getShotImage(){
        return ship.getShotImage();
    }

    @Override
    public int getShotDamage(){
        return ship.getShotDamage();
    }

    @Override
    public int getMaxHealth(){
        return ship.getMaxHealth();
    }

    @Override
    public double getSpeed(){
        return ship.getSpeed();
    }

    @Override
    public int getImage(){
        return ship.getImage();
    }

    @Override
    public String toString(){
        return sequenz;
    }

}
