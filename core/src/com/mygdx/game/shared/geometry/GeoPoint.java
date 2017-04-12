package com.mygdx.game.shared.geometry;

public class GeoPoint extends GeoData{

    /**
     * Constructor. We get everything we need from GeoData.
     *
     * @param x Coordinate
     * @param y Coordinate
     **/
    public GeoPoint(final double x, final double y){
        super(x, y);
    }

    /**
     * Shifts this GeoPoint round the given GeoVector.
     *
     * @param other vector to shift point
     * @return a new shifted GeoPoint
     */
    public GeoPoint add(final GeoVector other){
        assert other != null : "Reference can't be null!";

        return new GeoPoint(this.getX() + other.getX(), this.getY() + other.getY());
    }
}