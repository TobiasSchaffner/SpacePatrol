package com.mygdx.game.shared.position.ship;

/**
 * Interface for ShipDecorator.
 * Created by platypus on 02.06.16.
 */
public interface Ship{

    /**
     * Builds the StringRepresentation of the ShipDecorator.
     *
     * @return A String with Chars representing the Filters.
     */
    String toString();

    //Ship
    int getMaxHealth();

    double getSpeed();

    int getImage();

    //Fire
    double getShotSpeed();

    int getShotDamage();

    int getShotImage();
}
