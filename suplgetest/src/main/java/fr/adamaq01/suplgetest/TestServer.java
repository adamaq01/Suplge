package fr.adamaq01.suplgetest;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import java.io.IOException;

/**
 * Created by Adamaq01 on 08/05/2017.
 */
public class TestServer extends SocketIOServer {

    public TestServer(Configuration configuration) {
        super(configuration);
    }

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        // configuration.setHostname("localhost");
        configuration.setPort(2626);
        TestServer server = new TestServer(configuration);

        server.addConnectListener(client -> System.out.println("Somebody connected with id: " + client.getSessionId()));

        server.addEventListener("frame", Object.class, (client, data, ackSender) -> {
            System.out.println("Received FramePacket !");
            for(SocketIOClient cons : server.getAllClients()) {
                if(!client.getSessionId().equals(cons.getSessionId())) {
                    cons.sendEvent("display", data);
                }
            }
        });

        server.addEventListener("input", Object.class, (client, data, ackSender) -> {
            System.out.println("Received InputPacket !");
            for(SocketIOClient cons : server.getAllClients()) {
                if(!client.getSessionId().equals(cons.getSessionId())) {
                    cons.sendEvent("inputs", data);
                }
            }
        });

        server.start();

        System.out.println("Server Started !");
    }
}
