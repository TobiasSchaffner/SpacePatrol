package com.mygdx.game.shared.orb;

import com.mygdx.game.shared.geometry.GeoPoint;
import com.mygdx.game.shared.orb.colonie.Settlement;

public class Planet implements Orb{

    private final GeoPoint position;
    private final String name;
    private boolean occupied;
    private Settlement settlement;
    private final Integer imageCode;

    public Planet(String name, final GeoPoint position, Integer imageCode){
        assert name != null;
        assert position != null;
        assert imageCode != null;
        this.position = position;
        this.name = name;
        this.occupied = false;
        this.settlement = null;
        this.imageCode = imageCode;
    }

    public Planet(String input){
        assert input != null;
        if(!input.startsWith("[Planet]")) { throw new AssertionError(); }
        final String[] values = input.split(";");
        this.position = new GeoPoint(Double.parseDouble(values[2]), Double.parseDouble(values[3]));
        this.name = values[1];
        this.occupied = false;
        this.settlement = null;
        this.imageCode = Integer.parseInt(values[4]);
    }

    public void occupie(Settlement settlement){
        assert settlement != null;
        this.occupied = true;
        this.settlement = settlement;
    }

    public Settlement getSettlement(){
        return settlement;
    }

    public boolean getOccupied(){
        return occupied;
    }

    public GeoPoint getPosition(){
        return position;
    }

    public String getName(){
        return name;
    }

    public Integer getImage(){
        return imageCode;
    }

    public String toString(){
        return "[Planet];" +
                name + ";" +
                position.getX() + ";" +
                position.getY() + ";" +
                imageCode + ";" +
                "\n";
    }
}
