package com.mygdx.game.shared.position;

/**
 * Created by platypus on 11.04.16.
 */
public class Shot extends Position{

    /**
     * The image code shown for the Shot.
     */
    private final int image;

    /**
     * The speed of the shot.
     */
    private final double speed;

    /**
     * The player that fired the shot.
     */
    private final Player player;

    public Shot(String id, int x, int y, int angle, double speed, int image, Player player){
        super(id, x, y, angle);
        this.player = player;
        this.image = image;
        this.speed = speed;
    }

    /**
     * Calculates the Position out of speed time fired and direction.
     *
     * @return the actual position on the x-axis of the shot.
     */
    public int getAdjustedX(){
        return (int) (getX() + getTimeSinceUpdate() * getSpeed() * -1 * Math.sin(Math.toRadians(getAngle())));
    }

    /**
     * Calculates the Position out of speed time fired and direction.
     *
     * @return the actual position on the x-axis of the shot.
     */
    public int getAdjustedY(){
        return (int) (getY() + getTimeSinceUpdate() * getSpeed() * Math.cos(Math.toRadians(getAngle())));
    }

    public Player getPlayer(){
        return player;
    }

    public int getImage(){
        return image;
    }

    @SuppressWarnings("WeakerAccess")
    public double getSpeed(){
        return speed;
    }

}
