package com.mygdx.game.server.server.Bots;

import com.mygdx.game.server.server.Bots.botSequence.BotSequence;
import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.geometry.GeoPoint;
import com.mygdx.game.shared.geometry.GeoVector;
import com.mygdx.game.shared.position.Player;
import com.mygdx.game.shared.position.Shot;
import com.mygdx.game.shared.space.Space;

/**
 * Created by platypus on 03.06.16.
 */
public class Bot extends Player implements Runnable{
    private static final GeoVector NO_MOVEMENT_VECTOR = new GeoVector(0, 0);
    private static final GeoVector NORTH_VECTOR = new GeoVector(0, 1);
    private static final int DEGREES_CIRCLE = 360;

    /**
     * The Sequence of Points the Bot is traveling to.
     */
    private BotSequence sequence;

    /**
     * The Space the Bot is in.
     */
    private Space space;

    /**
     * The Position the Bot is at.
     */
    private GeoPoint position;

    /**
     * The relative GeoPoint the Bot is heading at.
     */
    private GeoVector movement;

    /**
     * The distance to the GeoPoint the Bot is heading at.
     */
    private double distance;

    /**
     * The last time the position was calculated.
     */
    private long lastMovementChange;

    /**
     * Initialization of a new Bot.
     *
     * @param space    The Space the Bot is in.
     * @param sequence The Sequence of Points the Bot is traveling to.
     */
    public Bot(Space space, BotSequence sequence){
        super("Bot", "F", GameSettings.START_HEALTH, GameSettings.SPACE_WIDTH_MID, GameSettings.SPACE_HEIGHT_MID, 0, 0, "null");
        assert space != null;
        assert sequence != null;

        space.getContainer().getPositions().put(this.getName(), this);
        this.space = space;
        this.sequence = sequence;
        this.position = new GeoPoint(GameSettings.SPACE_WIDTH_MID, GameSettings.SPACE_HEIGHT_MID);
        this.lastMovementChange = System.currentTimeMillis();
        this.movement = NORTH_VECTOR;
    }

    @Override
    public void run(){
        while(getHealth() > 0){
            setPosition();
            attack();
            if(!isMoving()){
                setMovement(sequence.getMovementVector().minus(position));
            }
            this.setPosition(getShip().toString(), getHealth(), (int) position.getX(), (int) position.getY(), getAngle(), 1, "null");
            try{
                Thread.sleep(GameSettings.SLEEP_TIME);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if theres an other player around. If so, stop moving, turn in direction of the player and shoot.
     */
    private void attack(){
        GeoVector movementBackup = movement;
        movement = NO_MOVEMENT_VECTOR;
        Player attacker = null;
        for(Player player : space.getContainer().getPositions().values()){
            if(getHealth() > 0 && !player.getName().equals(getName()) && Math.abs(player.getAdjustedX() - getAdjustedX()) < GameSettings.AGRESSION_RADIUS && Math.abs(player.getAdjustedY() - getAdjustedY()) < GameSettings.AGRESSION_RADIUS){
                attacker = player;
            }
        }
        while(getHealth() > 0 && attacker != null && attacker.getHealth() > 0 && Math.abs(attacker.getAdjustedX() - getAdjustedX()) < GameSettings.AGRESSION_RADIUS && Math.abs(attacker.getAdjustedY() - getAdjustedY()) < GameSettings.AGRESSION_RADIUS){
            adjustMovementAngle(new GeoVector(attacker.getAdjustedX(), attacker.getAdjustedY()).minus(position));
            this.setPosition(getShip().toString(), getHealth(), getX(), getY(), getAngle(), isMoving() ? 1 : 0, getName() + "#" + System.currentTimeMillis());
            space.getContainer().addShot(this.getShotId(), new Shot(getShotId(), this.getX(), this.getY(), getAngle(), getShip().getShotSpeed(), getShip().getShotImage(), this));
            try{
                Thread.sleep(GameSettings.SLEEP_TIME);
            }catch(InterruptedException e){
                throw new AssertionError("Bot was interrupted!");
            }
            lastMovementChange = System.currentTimeMillis();
        }
        movement = movementBackup;
    }


    /**
     * Adjusts the exact position aswell as lastmovementchange, angle and movement-flag
     *
     * @param newMovement The new GeoPoint the to head at.
     */
    @SuppressWarnings("Duplicates")
    private void setMovement(GeoVector newMovement){
        if(newMovement != null){
            setPosition();
            movement = newMovement.normalized();
            distance = Math.sqrt(newMovement.dot(newMovement));
            lastMovementChange = System.currentTimeMillis();
        }
    }

    /**
     * Adjusts the exact position as well as lastmovementchange, angle and movement-flag.
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
     * @param movementVector The new GeoPoint the to head at.
     */
    private void adjustMovementAngle(GeoVector movementVector){
        assert movementVector != null;
        GeoVector directionVector = movementVector.normalized();
        setMovementAngle((int) (Math.toDegrees(Math.atan2(directionVector.getY(), directionVector.getX()) - Math.atan2(NORTH_VECTOR.getY(), NORTH_VECTOR.getX()))));
        // y do we get negative values in some cases?
        if(getMovementAngle() < 0) { setMovementAngle(getMovementAngle() + DEGREES_CIRCLE); }
    }

    @Override
    public boolean isMoving(){
        return !movement.equals(NO_MOVEMENT_VECTOR);
    }

}
