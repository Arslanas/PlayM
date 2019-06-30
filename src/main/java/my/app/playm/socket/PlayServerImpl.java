package my.app.playm.socket;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Log4j
@Component
public class PlayServerImpl implements PlayServer {
    @Autowired
    private ServerSocket server;
    @Autowired
    private RequestProcessor rangeProcessor;
    private BufferedWriter out;
    private BufferedReader in;
    private Thread socketThread;
    private boolean running;

    @Override
    public void start() {
        socketThread = new Thread(() -> startListening());
        socketThread.start();
    }

    private void startListening() {
        log.debug("PlayM started listening on port - " + server.getLocalPort());

        running = true;
        while (running) {
            log.debug("Ready to accept request");

            Socket client = acceptClient();
            if (client == null) break;

            processRequest(client);

            makeResponse(client);
        }
    }

    private Socket acceptClient() {
        Socket client = null;
        try {
            client = server.accept();
        } catch (IOException e) {
            if (e.getMessage().equals("socket closed")) {
                log.debug("Server closed");
                return null;
            }
            e.printStackTrace();
        }
        return client;
    }

    private void processRequest(Socket client) {
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            rangeProcessor.process(in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException occured on request");
        }
    }

    private void makeResponse(Socket client) {

        try {
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException occured on response");
        }
    }


    @Override
    public void stop() {
        running = false;
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can not stop socketServer!");
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
