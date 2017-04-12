package com.mygdx.game.shared.position;

import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.position.ship.Ship;
import com.mygdx.game.shared.position.ship.ShipFactory;

/**
 * Created by platypus on 11.04.16.
 */
public class Player extends Position{


    private Ship ship;
    private int health;
    private String shotId;
    private boolean isMoving;

    public Player(String id, String sequence, int health, int x, int y, int angle, int moving, String shotId){
        super(id, x, y, angle);
        this.shotId = shotId;
        this.health = health;
        isMoving = moving == 1;
        this.ship = ShipFactory.createShip(sequence);
    }

    //New Player
    public Player(String id, int x, int y){
        this(id, "", GameSettings.START_HEALTH, x, y, GameSettings.START_ANGLE, 0, "null");
    }

    public void setPosition(String sequence, int health, int x, int y, int angle, int moving, String shotId){
        super.setPosition(x, y, angle);
        this.shotId = shotId;
        this.health = health;
        isMoving = moving == 1;
        ship = ShipFactory.extendShip(ship, sequence);
    }

    public int getAdjustedX(){
        if(isMoving){
            return (int) (getX() + getTimeSinceUpdate() * ship.getSpeed() * -1 * Math.sin(Math.toRadians(getAngle())));
        }else{
            return getX();
        }
    }

    public int getAdjustedY(){
        if(isMoving){
            return (int) (getY() + getTimeSinceUpdate() * ship.getSpeed() * Math.cos(Math.toRadians(getAngle())));
        }else{
            return getY();
        }
    }

    public String toString(){
        final String shotId = getHealth() <= 0 ? "null" : getShotId();

        return "[Player];" +
                getName() + ';' +
                getShip().toString() + ';' +
                getHealth() + ';' +
                getX() + ';' +
                getY() + ';' +
                getAngle() + ";" +
                (isMoving() ? 1 : 0) + ';' +
                shotId + '\n';
    }

    public Ship getShip(){
        return ship;
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int value){
        this.health = value;
    }

    public String getShotId(){
        return shotId;
    }

    public boolean isMoving(){
        return isMoving;
    }
}
