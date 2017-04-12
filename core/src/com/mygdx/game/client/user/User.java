package com.mygdx.game.client.user;

import com.mygdx.game.shared.geometry.GeoPoint;
import com.mygdx.game.shared.geometry.GeoVector;
import com.mygdx.game.shared.position.Player;

/**
 * The user is the single Object of each client representing the player played by the person in front of the screen.
 */
public class User extends Player{
    private static final GeoVector NO_MOVEMENT_VECTOR = new GeoVector(0, 0);
    private static final GeoVector NORTH_VECTOR = new GeoVector(0, 1);
    private static final int CIRCLE_DEGREES = 360;

    /**
     * The exact Position of the User.
     */
    private GeoPoint position;

    /**
     * The GeoVector to the position the Player is heading to.
     */
    private GeoVector movement;

    /**
     * The Password of the player. Will be used to identify the player and to encrypt the session.
     */
    private String password;

    /**
     * The distance from the actual point to the end of the movement vector.
     */
    private double distance;

    /**
     * The last time the position was calculated.
     */
    private long lastMovementChange;

    /**
     * States if the user triggered a shot by touching the center of the screen.
     */
    private boolean hasShot;

    /**
     * Initialization with no Movement.
     *
     * @param id       The Name of the Player.
     * @param password The Password of the Player.
     * @param sequence The States the kind and the additions to the ship.
     * @param health   How many Healthpoints the User has atm.
     * @param x        The Position on the X-Axis.
     * @param y        The Position on the Y-Axis.
     * @param angle    The Angle between the Moving GeoVector and the North-GeoVector.
     * @param moving   States if the Player is moving to enable/disable predictions of the position.
     */
    public User(String id, String password, String sequence, int health, int x, int y, int angle, int moving){
        super(id, sequence, health, x, y, angle, moving, null); // TODO speed != is moving
        assert password != null;
        this.password = password;
        this.position = new GeoPoint(x, y);
        this.lastMovementChange = System.currentTimeMillis();
        this.movement = NO_MOVEMENT_VECTOR;
        this.hasShot = false;
    }

    /**
     * Calculates the new exact position based on speed and time before returning the position.
     *
     * @return The new Position of the User.
     */
    public GeoPoint getPosition(){
        setPosition();
        return position;
    }

    /**
     * Adjusts the exact position aswell as lastmovementchange, angle and movement-flag.
     */
    @SuppressWarnings("Duplicates")
    public void setMovement(GeoVector newMovement){
        if(newMovement != null){
            setPosition();
            movement = newMovement.normalized();
            distance = Math.sqrt(newMovement.dot(newMovement));
            lastMovementChange = System.currentTimeMillis();
        }
    }

    /**
     * Adjusts the exact position aswell as lastmovementchange, angle and movement-flag.
     */
    @SuppressWarnings("Duplicates")
    private void setPosition(){
        final double passedWay = (System.currentTimeMillis() - lastMovementChange) * getShip().getSpeed();
        if(isMoving() && passedWay < distance){
            position = position.add(movement.mult(passedWay));
            distance = distance - passedWay;
            lastMovementChange = System.currentTimeMillis();
            adjustMovementAngle(movement);
        }
        if(isMoving() && passedWay >= distance){
            position = position.add(movement.mult(distance));
            movement = NO_MOVEMENT_VECTOR;
            distance = 0;
            lastMovementChange = System.currentTimeMillis();
            adjustMovementAngle(movement);
        }

    }

    /**
     * Calculates the Angle between the Movement GeoVector and the NorthVector.
     *
     * @param movementVector The new Position the Player is heading to.
     */
    private void adjustMovementAngle(GeoVector movementVector){
        GeoVector directionVector = movementVector.normalized();
        if(isMoving()){
            setMovementAngle((int) (Math.toDegrees(Math.atan2(directionVector.getY(), directionVector.getX()) - Math.atan2(NORTH_VECTOR.getY(), NORTH_VECTOR.getX()))));
            // y do we get negative values in some cases?
            if(getMovementAngle() < 0){
                setMovementAngle(getMovementAngle() + CIRCLE_DEGREES);
            }
        }
    }

    /**
     * Builds a NetworkPaket for a Player.
     *
     * @return A Packet string that can be send to the Server.
     */
    public String toString(){
        int x = (int) getPosition().getX();
        int y = (int) getPosition().getY();
        int moving = 0;
        String shot = "null";

        if(isMoving()){
            moving = 1;
        }

        if(getHasShot()){
            shot = getName() + "#" + System.currentTimeMillis();
            setHasShot(false);
        }

        return "[Player];" + getName() + ";" + x + ";" + y + ";" + getMovementAngle() + ";" + moving + ";" + shot + "\n";
    }

    @Override
    public boolean isMoving(){
        return !movement.equals(NO_MOVEMENT_VECTOR);
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){ // TODO useful?
        assert password != null;
        this.password = password;
    }

    private boolean getHasShot(){
        return hasShot;
    }

    public void setHasShot(boolean hasShot){
        this.hasShot = hasShot;
    }


}
