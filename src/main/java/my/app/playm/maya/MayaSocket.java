package my.app.playm.maya;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MayaSocket {
    private ServerSocket server;
    private BufferedWriter out;
    private BufferedReader in;

    public static void main(String[] args)throws Exception {
        MayaSocket mayaSocket = new MayaSocket();
        mayaSocket.start(6666);
    }
    public void start(int port) throws Exception{
        server = new ServerSocket(port);
        System.out.println(server.toString());
        Socket client = server.accept();
        out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String request = in.readLine();
        System.out.println("Request from Maya - " + request);
    }
}
