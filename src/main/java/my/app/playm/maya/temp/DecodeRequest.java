package my.app.playm.maya.temp;

import my.app.playm.controller.Data;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class DecodeRequest {
    Socket client;
    PrintWriter out;
    ObjectInputStream in;

    public static void main(String[] args) throws Exception {

        new Thread(()-> {
            try {
                Process exec = Runtime.getRuntime().exec("java -jar target/PlayM-1.0-SNAPSHOT.jar");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        DecodeRequest decodeRequest = new DecodeRequest();
        decodeRequest.startConnection();
        decodeRequest.makeRequest(Data.videoSource);

        while (true){}
    }

    public void startConnection() throws Exception {
        client = new Socket("127.0.0.1", 6666);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new ObjectInputStream(client.getInputStream());
    }

    public void makeRequest(String videoSource)throws Exception {
        out.println(videoSource);
    }
}
