package com.mygdx.game.shared.container;

import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.position.Player;
import com.mygdx.game.shared.position.Shot;
import com.mygdx.game.shared.util.Util;

import static com.mygdx.game.shared.util.Util.report;

/**
 * Created by platypus on 12.04.16.
 */
public class CollisionDetector implements Runnable{

    /**
     * The position object storing all relevant player information of the space.
     */
    private final Container positions;

    /**
     * True if Server, false if client.
     */
    private final boolean adjustHealth;

    public CollisionDetector(final Container container, boolean enableHealthAdjustment){
        assert container != null;
        this.positions = container;
        this.adjustHealth = enableHealthAdjustment;
    }

    public void run(){
        Thread.currentThread().setName("CollisionDetector");
        report(Util.Message.CLIENT_MESSAGE, "CollisionDetector is running now!");
        //noinspection InfiniteLoopStatement
        while(true){
            for(final Player player : positions.getPositions().values()){
                if(player.getHealth() > 0){
                    for(final Shot shot : positions.getShots().values()){
                        if(GameSettings.SHOT_LIFE_TIME <= shot.getTimeSinceUpdate()){
                            positions.getShots().remove(shot.getName());
                        }else{
                            report(Util.Message.CLIENT_DEBUG, "CollisionDetector: DeltaX: " + Math.abs(shot.getAdjustedX() - player.getAdjustedX()) +
                                    " DeltaY: " + Math.abs(shot.getAdjustedY() - player.getAdjustedY()) +
                                    " Name equal: " + !player.getName().equals(shot.getName().split("#")[0]) +
                                    " player Health > 0:" + (player.getHealth() > 0));
                            // TODO Funktioniert so mit größeren oder kleineren Assets nicht. Müssen an das bild rankommen.
                            if(Math.abs(shot.getAdjustedX() - player.getAdjustedX()) < 48
                                    && Math.abs(shot.getAdjustedY() - player.getAdjustedY()) < 48
                                    && player != shot.getPlayer()
                                    && player.getHealth() > 0){
                                if(adjustHealth){
                                    player.setHealth(player.getHealth() - shot.getPlayer().getShip().getShotDamage());
                                    //noinspection StatementWithEmptyBody
                                    if(player.getHealth() <= 0){
                                        // positions.getPositions().remove(player.getName());
                                        // TODO max level? DISABLED LVL UP! Should be done with factory!
                                        // shot.getPlayer().levelUp();
                                        report(Util.Message.SERVER_ERROR,"You got a cookie!");
                                    }
                                }
                                positions.getShots().remove(shot.getName());
                                report(Util.Message.CLIENT_DEBUG, "Shot removed!");
                                break;
                            }
                        }
                    }
                }else if(player.getTimeSinceUpdate() > GameSettings.DROP_TIMEOUT){
                    positions.getPositions().remove(player.getName());
                }
            }
            try{
                Thread.sleep(GameSettings.SLEEP_TIME);
            }catch(InterruptedException e){
                throw new AssertionError("CollisionDetector was interrupted!");
            }
        }
    }
}
