package com.mygdx.game.server.server;

import com.mygdx.game.shared.position.Player;
import com.mygdx.game.shared.util.Util;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static com.mygdx.game.shared.util.Util.report;

/**
 * Created by hainzi on 03.06.16.
 */
public class WebStatsServer{
    /**
     * Server instance for this web-stat server
     */
    private final Server server;
    /**
     * http server.
     */
    private final HttpServer httpServer;

    public WebStatsServer(Server server){
        assert server != null;

        this.server = server;
        HttpServer ss = null;
        try{
            ss = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
            ss.createContext("/", new HH());
        }catch(IOException e){
            report(Util.Message.SERVER_ERROR, "unable to build WebStatsServer: " + e.getMessage());
        }
        httpServer = ss;
    }

    public void start(){
        httpServer.start();
    }

    @SuppressWarnings("unused")
    public void stop(){
        httpServer.stop(0);
    }

    private class HH implements HttpHandler{
        @Override
        public void handle(HttpExchange httpExchange) throws IOException{
            final StringBuilder sb = new StringBuilder(100);
            sb.append("currently playing users: ").append(server.getConnections().size()).append('\n');
            sb.append("health \t X \t Y \t player\n");
            for(final Connection con : server.getConnections()){
                final Player player = con.getPlayer();
                sb.append(String.format("   %3d", player.getHealth())).append('\t').append(player.getX()).append('\t').append(player.getY()).append('\t').append(con.getName()).append("\n");
            }
            final String response = sb.toString();
            httpExchange.sendResponseHeaders(200, response.length());
            final OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
