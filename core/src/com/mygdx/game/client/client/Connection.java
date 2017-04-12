package com.mygdx.game.client.client;

import com.mygdx.game.client.user.User;
import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.container.CollisionDetector;
import com.mygdx.game.shared.orb.Planet;
import com.mygdx.game.shared.space.Space;
import com.mygdx.game.shared.util.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.mygdx.game.shared.util.Util.report;

/**
 * Sends the Data to the Server.
 * Created by platypus on 09.04.16.
 */
public class Connection implements Runnable{

    private final String serverAddress;
    private final int port;
    private final Space space;
    private final User user;


    /**
     * Definition of the Server to connect to and the essential client objects.
     *
     * @param serverAddress The Ip Adress of the Server.
     * @param port          The Port the Server is listening on.
     * @param user          The User object of the player.
     * @param space         The space object to fill the space information in.
     */
    public Connection(String serverAddress, int port, User user, Space space){
        assert serverAddress != null;
        assert Util.validate(serverAddress);
        assert Util.validate(port);
        assert user != null;
        assert space != null;

        this.serverAddress = serverAddress;
        this.port = port;
        this.user = user;
        this.space = space;

        final CollisionDetector collisionDetector = new CollisionDetector(space.getContainer(), false);
        new Thread(collisionDetector).start();
    }

    /**
     * The connection to the Server.
     * Handshake at the beginning, followed by the transmission of the space and then exchanging the position data in a endless loop.
     */
    public void run(){
        try(Socket clientSocket = new Socket(serverAddress, port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"))){

            // Initial Handshake checking for Username and Password
            String message = "[SYN];" + user.getName() + ";" + user.getPassword() + "\n";

            report(Util.Message.CLIENT_DEBUG, "Client sent: " + message);
            outToServer.writeBytes(message);
            outToServer.flush();
            message = inFromServer.readLine();

            report(Util.Message.CLIENT_DEBUG, "Client received: " + message);

            if(message == null || !message.equals("[ACK]")){
                clientSocket.close();
                return;
            }

            // Request the space and add the answer
            message = "[Space]\n";
            outToServer.writeBytes(message);
            outToServer.flush();
            while((message = inFromServer.readLine()) != null && !message.equals("-1")){

                report(Util.Message.CLIENT_DEBUG, "Client received: " + message);
                if(message.startsWith("[Orb]")){
                    throw new AssertionError("Not implemented. No orbs yet.");
                }else if(message.startsWith("[Planet]")){
                    space.putOrb(new Planet(message));
                }
            }

            String position;
            // Start with the positon exchange routine
            //noinspection InfiniteLoopStatement
            while(true){
                outToServer.writeBytes(user.toString());

                report(Util.Message.CLIENT_DEBUG, "Client sent: " + user.toString());

                outToServer.flush();
                while((position = inFromServer.readLine()) != null && !position.equals("-1")){

                    report(Util.Message.CLIENT_DEBUG, "Client received: " + position);

                    if(position.startsWith("[Player]")) { space.getContainer().addPosition(position); }
                }
                Thread.sleep(GameSettings.SLEEP_TIME);
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            throw new AssertionError("ClientConnection got interrupted!");
        }
    }
}
