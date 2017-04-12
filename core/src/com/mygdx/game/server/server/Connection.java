package com.mygdx.game.server.server;

import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.orb.Orb;
import com.mygdx.game.shared.position.Player;
import com.mygdx.game.shared.position.Shot;
import com.mygdx.game.shared.space.Space;
import com.mygdx.game.shared.util.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import static com.mygdx.game.shared.util.Util.report;


/**
 * The Connection to a Client.
 * Created by platypus on 09.04.16.
 */
class Connection extends Thread{

    /**
     * The server object to access other connections.
     */
    private final Server server;

    /**
     * The socket for the connection to the client.
     */
    private final Socket socket;

    /**
     * The space that will be sent to the client.
     */
    private Space space;

    /**
     * States if the client sent data that is not protocol conform.
     */
    private boolean protocolViolation;

    /**
     * States if the connection was lost.
     */
    private boolean connectionLost;

    /**
     * The name of the Client.
     */
    private String clientName = null;

    /**
     * The Password of the Client to identify the client and to encrypt the session.
     */
    private String password = null;

    /**
     * The Player-Object that stores all relevant information
     */
    private Player player;

    /**
     * Initialisation of the connection.
     *
     * @param server The server object to access other connections.
     * @param socket The socket for the connection to the client.
     * @param space  The space that will be sent to the client.
     */
    Connection(Server server, Socket socket, Space space){
        assert server != null;
        assert socket != null;
        assert space != null;
        assert !socket.isClosed();

        this.server = server;
        this.socket = socket;
        this.space = space;
        setName("Connection" + socket.getRemoteSocketAddress());
    }

    @Override
    public void run(){
        try(BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream())){
            socket.setSoTimeout(10_000);

            if(!verifyConnection(inFromClient, outToClient)){
                return;
            }

            final String message = inFromClient.readLine();
            if(message == null){
                connectionLost = true;
                return;
            }else if("[Space]".regionMatches(0, message, 0, 7)){
                for(final Orb orb : space.getOrbs()){
                    outToClient.writeBytes(orb.toString());
                }
                outToClient.writeBytes("-1\n");
                outToClient.flush();
            }

            player = new Player(clientName, GameSettings.SPACE_WIDTH_MID, GameSettings.SPACE_HEIGHT_MID);
            space.getContainer().getPositions().put(clientName, player);

            while(!protocolViolation && !connectionLost){
                loop(inFromClient, outToClient);
            }
        }catch(SocketTimeoutException | SocketException ignored){
            connectionLost = true;
        }catch(NumberFormatException e){
            protocolViolation = true;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(protocolViolation){
                report(Util.Message.SERVER_ERROR, this.getName() + " forcefully terminated");
            }else if(connectionLost){
                report(Util.Message.SERVER_ERROR, (clientName != null ? clientName : this.getName()) + " lost connection");
            }else if(clientName != null){ // log out
                report(Util.Message.SERVER_MESSAGE, clientName + " logged out");
            }else{// connection closed without receiving a "line of data"
                report(Util.Message.SERVER_ERROR, this.getName() + " closed");
            }
            if(clientName != null){
                space.getContainer().getPositions().remove(clientName);
            }
            server.removeConnection(this);
        }
    }

    /**
     * Verification of the Handshake. The Client has to send a [SYN] Packet with the user credentials.
     * If we are ok with that user to connect we will send a [ACK] Packet.
     *
     * @param inFromClient The InputStreamReader.
     * @param outToClient  The OutputStreamWriter.
     * @return A boolean stating if the Handshake was successful.
     * @throws IOException
     */
    private boolean verifyConnection(BufferedReader inFromClient, DataOutputStream outToClient) throws IOException{
        assert inFromClient != null;
        assert outToClient != null;

        final String receivedString = inFromClient.readLine();
        if(receivedString == null){
            connectionLost = true;
            return false;
        }
        final String clientSyn[] = receivedString.split(";");
        if(clientSyn.length != 3 || !clientSyn[0].equals("[SYN]")){
            outToClient.writeBytes("Invalid SYN-packet, required: \"[SYN];username;password\"");
            protocolViolation = true;
            return false;
        }
        clientName = clientSyn[1];
        password = clientSyn[2]; // TODO implement
        if("Bot".regionMatches(0, clientName, 0, 3)){
            outToClient.writeBytes("Invalid Username!");
            protocolViolation = true;
            return false;
        }else if(space.getContainer().getPositions().get(clientName) != null){
            outToClient.writeBytes("Username already in use!");
            protocolViolation = true;
            return false;
        }

        report(Util.Message.SERVER_MESSAGE, "Client " + clientName + " connected!");

        outToClient.writeBytes("[ACK]\n");
        outToClient.flush();
        return true;
    }


    /**
     * @param inFromClient The InputStreamReader.
     * @param outToClient  The OutputStreamWriter.
     * @throws IOException
     * @throws InterruptedException
     */
    private void loop(BufferedReader inFromClient, DataOutputStream outToClient) throws IOException, InterruptedException{
        assert inFromClient != null;
        assert outToClient != null;

        final String receivedString = inFromClient.readLine();
        if(receivedString == null){
            connectionLost = true;
            return;
        }
        report(Util.Message.SERVER_DEBUG, "Server received: " + receivedString);

        final String[] received = receivedString.split(";");
        if(received.length != 7 || !"[Player]".equals(received[0])){
            protocolViolation = true;
            return;
        }
        final String id = received[1];
        final int x = Integer.parseInt(received[2]);
        final int y = Integer.parseInt(received[3]);
        final int angle = Integer.parseInt(received[4]);
        final int moving = Integer.parseInt(received[5]);
        final String shotId = received[6];

        if(!clientName.equals(id)){
            outToClient.writeBytes("Manipulation attempt detected! False Player"); // false player
            protocolViolation = true;
            return;
        }else if(player.isMoving() && (100 * player.getShip().getSpeed() + 10 < Math.abs(player.getX() - x) || 100 * player.getShip().getSpeed() + 10 < Math.abs(player.getY() - y))){
            outToClient.writeBytes("Manipulation attempt detected! Deviation from position is too much");
        }else if(player.isMoving() && (100 * player.getShip().getSpeed() + 10 < Math.abs(player.getX() - x) || 100 * player.getShip().getSpeed() + 10 < Math.abs(player.getY() - y))){
            outToClient.writeBytes("Manipulation attempt detected!"); // deviation from position is too much
            protocolViolation = true;
            return;
        }

        if(player.getHealth() > 0){
            player.setPosition(player.getShip().toString(), player.getHealth(), x, y, angle, moving, shotId);

            if(!"null".equals(shotId)){
                space.getContainer().addShot(shotId, new Shot(shotId, x, y, angle, player.getShip().getShotSpeed(), player.getShip().getShotImage(), player));
            }
        }

        final String playerPacket = space.getContainer().buildPlayerPacket();

        report(Util.Message.SERVER_DEBUG, "Server sent: \n" + playerPacket);
        outToClient.writeBytes(playerPacket); // for every update in every client, really?
        outToClient.flush();
        Thread.sleep(90);
    }

    @Override
    public boolean equals(Object obj){
        return socket != null && getClass() == obj.getClass() && ((Connection) obj).socket.equals(socket);
    }

    Player getPlayer(){
        return player;
    }
}
