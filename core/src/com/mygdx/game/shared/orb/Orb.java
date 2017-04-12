package com.mygdx.game.shared.orb;

import com.mygdx.game.shared.geometry.GeoPoint;
import com.mygdx.game.shared.orb.colonie.Settlement;

/**
 * Created by platypus on 12.04.16.
 */
public interface Orb{

    Settlement getSettlement();

    boolean getOccupied();

    GeoPoint getPosition();

    String getName();

    Integer getImage();

}
