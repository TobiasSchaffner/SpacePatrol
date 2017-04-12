package com.mygdx.game.shared.orb.colonie.filter;

import com.mygdx.game.shared.orb.colonie.Settlement;

/**
 * Created by platypus on 02.06.16.
 */
abstract class SettlementFilter implements Settlement{
    private final Settlement settlement;
    private final String sequenz;

    SettlementFilter(Settlement ship, char id){
        assert ship != null;
        assert id != '\0';

        this.settlement = ship;
        sequenz = settlement.toString() + id;
    }

    @Override
    public String toString(){
        return sequenz;
    }
}
