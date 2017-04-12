package com.mygdx.game.shared.space;

import com.mygdx.game.shared.container.Container;
import com.mygdx.game.shared.orb.Orb;

import java.util.concurrent.CopyOnWriteArrayList;

public class Space{

    /**
     * The Container Object for that space holding all Players and Shots in the space.
     */
    private final Container container = new Container();

    /**
     * Boundary size of the space on the x-axis.
     */
    private final int sizeX;

    /**
     * Boundary size of the space on the Y-axis.
     */
    private final int sizeY;

    /**
     * The orbs in the space.
     */
    private final CopyOnWriteArrayList<Orb> orbs = new CopyOnWriteArrayList<>();

    public Space(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getSizeX(){
        return sizeX;
    }


    public int getSizeY(){
        return sizeY;
    }


    /**
     * Adds an orb to the space.
     *
     * @param orb the orb to add.
     */
    public void putOrb(final Orb orb){
        assert orb != null;
        orbs.add(orb);
    }

    public CopyOnWriteArrayList<Orb> getOrbs(){
        return orbs;
    }

    public Container getContainer(){
        return container;
    }

}
