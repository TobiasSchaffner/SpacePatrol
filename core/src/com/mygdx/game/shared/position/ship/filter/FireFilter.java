package com.mygdx.game.shared.position.ship.filter;

import com.mygdx.game.shared.position.ship.Ship;

/**
 * Created by platypus on 02.06.16.
 */
public class FireFilter extends ShipFilter{
    public FireFilter(Ship ship){
        super(ship, 'F');
    }

    @Override
    public int getImage(){
        return 2;
    }

    @Override
    public int getShotImage(){
        return 2;
    }
}
