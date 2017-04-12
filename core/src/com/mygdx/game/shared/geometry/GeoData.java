package com.mygdx.game.shared.geometry;

import com.mygdx.game.shared.util.Util;

abstract class GeoData{

    /**
     * X Coordinate of the point or vector.
     */
    private final double xCoordinate;
    /**
     * Y Coordinate of the point or vector.
     */
    private final double yCoordinate;

    /**
     * Constructor. Just written down to have a better overview.
     *
     * @param x Coordinate
     * @param y Coordinate
     **/
    GeoData(final double x, final double y){
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    public double getX(){
        return xCoordinate;
    }

    public double getY(){
        return yCoordinate;
    }

    @Override
    public int hashCode(){
        int result;
        long temp;
        temp = Double.doubleToLongBits(xCoordinate);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yCoordinate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public final boolean equals(final Object obj){
        assert obj != null : "Reference can't be null!";

        if(this == obj){
            return true;
        }else if(this.getClass() != obj.getClass()){
            return false;
        }
        final GeoData other = (GeoData) obj;
        return Util.nearly(xCoordinate, other.xCoordinate) && Util.nearly(yCoordinate, other.yCoordinate);
    }

    /**
     * Computation of the dotProdukt.
     *
     * @param other GeoVector to compute
     * @return a double of the dotproduct
     * @see http://en.wikipedia.org/wiki/Dot_product
     */
    @SuppressWarnings("JavadocReference")
    public double dot(final GeoData other){
        return this.getX() * other.getX() + this.getY() * other.getY();
    }

    /**
     * Computation of a GeoVector out of two Points by substracting them.
     *
     * @param other GeoPoint to subtract
     * @return a GeoVector from one GeoPoint to the other
     * @see http://en.wikipedia.org/wiki/Euclidean_vector
     */
    @SuppressWarnings("JavadocReference")
    public GeoVector minus(final GeoData other){
        return new GeoVector(this.getX() - other.getX(), this.getY() - other.getY());
    }

    @Override
    public String toString(){
        throw new UnsupportedOperationException("not supported");
    }
}