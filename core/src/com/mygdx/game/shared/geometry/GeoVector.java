package com.mygdx.game.shared.geometry;

public class GeoVector extends GeoData{

    /**
     * Constructor. We get everything we need from GeoData.
     *
     * @param x Coordinate
     * @param y Coordinate
     **/
    public GeoVector(final double x, final double y){
        super(x, y);
    }

    /**
     * Computation of the vector - length with good my good ol friend Pythagoras.
     *
     * @return a double of the length of the vector
     * @see http://en.wikipedia.org/wiki/Euclidean_vector
     */
    @SuppressWarnings({"JavadocReference", "WeakerAccess"})
    public double length(){
        return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
    }

    /**
     * Computation of the normalization.
     *
     * @return the normalized vector
     * @see http://en.wikipedia.org/wiki/Unit_vector
     */
    @SuppressWarnings("JavadocReference")
    public GeoVector normalized(){
        final double length = this.length();
        return new GeoVector(this.getX() / length, this.getY() / length);
    }

    /**
     * Multiplication with a Scalar.
     *
     * @param factor the Scalar
     * @return shifted vector
     */
    public GeoVector mult(final double factor){
        return new GeoVector(this.getX() * factor, this.getY() * factor);
    }
}
