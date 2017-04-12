package com.mygdx.game.server;


import com.mygdx.game.server.server.Server;
import com.mygdx.game.server.server.WebStatsServer;

/**
 * Created by platypus on 09.04.16.
 */
public final class SpacePatrolServer{

    public static void main(String[] args){
        final Server server = new Server();
        server.start();
        final WebStatsServer webStatsServer = new WebStatsServer(server);
        webStatsServer.start();
    }
}
