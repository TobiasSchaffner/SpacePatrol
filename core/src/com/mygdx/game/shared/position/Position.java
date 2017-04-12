package com.mygdx.game.shared.position;

/**
 * Created by platypus on 09.04.l16.
 */
abstract class Position{

    /**
     * Name of the Object.
     */
    private final String name;

    /**
     * Position on the X-Axis
     */
    private int x;

    /**
     * Position on the Y-Axis
     */
    private int y;

    /**
     * The Angle between the Northvector and the GeoVector the Object is heading.
     */
    private int angle;

    /**
     * A boolean that states if the Data was processed since last update.
     */
    private boolean isNew;

    /**
     * The time of the last update
     */
    private long lastMovementChange;

    Position(String id, int x, int y, int angle){
        assert id != null;
        this.name = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        isNew = true;
        this.lastMovementChange = System.currentTimeMillis();
    }

    /**
     * Setter for the new Position of an Object.
     *
     * @param x     Position on the X-Axis
     * @param y     Position on the Y-Axis
     * @param angle The Angle between the Northvector and the GeoVector the Object is heading.
     */
    void setPosition(int x, int y, int angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.lastMovementChange = System.currentTimeMillis();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @SuppressWarnings("WeakerAccess")
    public void setMovementAngle(int angle){
        this.angle = angle;
    }

    public int getAngle(){
        return angle;
    }

    public String getName(){
        return name;
    }

    public long getTimeSinceUpdate(){
        return System.currentTimeMillis() - lastMovementChange;
    }

    public int getMovementAngle(){
        return angle;
    }

    public void setIsNew(boolean value){
        isNew = value;
    }

    public boolean isNew(){
        return isNew;
    }


}
