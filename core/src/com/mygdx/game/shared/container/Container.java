package com.mygdx.game.shared.container;

import com.mygdx.game.shared.position.Player;
import com.mygdx.game.shared.position.Shot;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ContainerClass holding all relevant PlayerInformation of a space.
 * Created by platypus on 09.04.16.
 */
public class Container{

    /**
     * All Playeres in the space.
     */
    private final ConcurrentHashMap<String, Player> id2player = new ConcurrentHashMap<>();

    /**
     * All shots in the space.
     */
    private final ConcurrentHashMap<String, Shot> id2shot = new ConcurrentHashMap<>();

    /**
     * Builds a packet of all Players in the space that can be sent to a client.
     *
     * @return The packet.
     */
    public String buildPlayerPacket(){
        final StringBuilder packetString = new StringBuilder(78);
        for(final Player player : id2player.values()){
            packetString.append(player.toString());
        }
        packetString.append("-1\n");
        return packetString.toString();
    }

    /**
     * Adds a Shot to the shotlist.
     *
     * @param shotId The id/name of the Shot.
     * @param shot   The shot object.
     */
    public void addShot(String shotId, Shot shot){
        assert shotId != null;
        assert shot != null;

        id2shot.put(shotId, shot);
    }

    /**
     * Adds a Player to the list of players in this space.
     *
     * @param A packet String received.
     */
    public void addPosition(String receivedString){

        String[] received = receivedString.split(";");

        String id = received[1];
        String sequence = received[2];
        int health = Integer.parseInt(received[3]);
        int x = Integer.parseInt(received[4]);
        int y = Integer.parseInt(received[5]);
        int angle = Integer.parseInt(received[6]);
        int isMoving = Integer.parseInt(received[7]);
        String shotId = received[8];

        if(id2player.containsKey(id)){
            id2player.get(id).setPosition(sequence, health, x, y, angle, isMoving, shotId);
        }else{
            id2player.put(id, new Player(id, sequence, health, x, y, angle, isMoving, shotId));
        }

        if(!shotId.equals("null") && !id2shot.containsKey(shotId)){
            id2shot.put(shotId, new Shot(shotId, x, y, angle, id2player.get(id).getShip().getShotSpeed(), id2player.get(id).getShip().getShotImage(), id2player.get(id)));
        }

        id2player.get(id).setIsNew(true);
    }

    public ConcurrentHashMap<String, Player> getPositions(){
        return id2player;
    }

    public ConcurrentHashMap<String, Shot> getShots(){
        return id2shot;
    }
}
