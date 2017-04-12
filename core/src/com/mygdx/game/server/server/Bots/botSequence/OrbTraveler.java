package com.mygdx.game.server.server.Bots.botSequence;

import com.mygdx.game.shared.geometry.GeoVector;
import com.mygdx.game.shared.orb.Orb;
import com.mygdx.game.shared.space.Space;

import java.util.ListIterator;

/**
 * Created by platypus on 03.06.16.
 */
public class OrbTraveler implements BotSequence{

    private ListIterator<Orb> orbs;
    private final Space space;

    public OrbTraveler(Space space){
        assert space != null;

        this.space = space;
        orbs = space.getOrbs().listIterator();
    }

    public GeoVector getMovementVector(){
        Orb orb;
        if(orbs.hasNext()){
            orb = orbs.next();
        }else{
            orbs = space.getOrbs().listIterator();
            orb = orbs.next();
        }
        return new GeoVector(orb.getPosition().getX(), orb.getPosition().getY());
    }
}
