package com.mygdx.game.server.server;

import com.mygdx.game.server.server.Bots.Bot;
import com.mygdx.game.server.server.Bots.botSequence.OrbTraveler;
import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.container.CollisionDetector;
import com.mygdx.game.shared.geometry.GeoPoint;
import com.mygdx.game.shared.orb.Planet;
import com.mygdx.game.shared.space.Space;
import com.mygdx.game.shared.util.Util;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.shared.util.Util.report;

/**
 * Created by platypus on 11.04.16.
 */
public class Server extends Thread{
    /**
     * List of currently connected players.
     */
    private final List<Connection> connections = new ArrayList<>();

    @Override
    public void run(){
        setName("Server");
        try{
            final Space space = createSpace();
            new Thread(new CollisionDetector(space.getContainer(), true)).start();
            new Thread(new Bot(space, new OrbTraveler(space))).start();

            try(ServerSocket serverSocket = new ServerSocket(GameSettings.SERVER_PORT)){
                //noinspection InfiniteLoopStatement
                while(true){
                    final Socket socket = serverSocket.accept();
                    if(50 <= connections.size()){
                        try(DataOutputStream toClient = new DataOutputStream(socket.getOutputStream())){
                            toClient.writeBytes("Server at capacity limit, please try it later again");
                            toClient.flush();
                        }catch(Exception e){
                            report(Util.Message.SERVER_MESSAGE, "Server was unable to deny client connection");
                        }
                    }else{
                        final Connection connection = new Connection(this, socket, space);
                        synchronized(connections){
                            connections.add(connection);
                        }
                        connection.start();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            report(Util.Message.SERVER_MESSAGE, "Server was shut down");
        }
    }

    List<Connection> getConnections(){
        return connections;
    }

    void removeConnection(Connection connection){
        assert connection != null;

        synchronized(connections){
            connections.remove(connection);
        }
    }

    private static Space createSpace(){
        // Create Space
        final Space space = new Space(GameSettings.SPACE_WIDTH, GameSettings.SPACE_HEIGHT);
        space.putOrb(new Planet("Mars", new GeoPoint(1500, 10), 1));
        space.putOrb(new Planet("Earth", new GeoPoint(600, 500), 2));
        space.putOrb(new Planet("Venus", new GeoPoint(100, 1300), 3));
        space.putOrb(new Planet("Uranus", new GeoPoint(1400, 100), 4));
        space.putOrb(new Planet("Neptun", new GeoPoint(600, 1200), 5));
        space.putOrb(new Planet("Jupiter", new GeoPoint(200, 300), 6));
        space.putOrb(new Planet("Luna", new GeoPoint(1000, 1800), 7));
        space.putOrb(new Planet("Nigera", new GeoPoint(600, 700), 8));
        space.putOrb(new Planet("Mepeta", new GeoPoint(700, 50), 9));
        space.putOrb(new Planet("Sonata", new GeoPoint(500, 1400), 10));
        space.putOrb(new Planet("Calnera", new GeoPoint(600, 200), 1));
        space.putOrb(new Planet("Juno", new GeoPoint(1300, 1100), 2));
        return space;
    }
}
