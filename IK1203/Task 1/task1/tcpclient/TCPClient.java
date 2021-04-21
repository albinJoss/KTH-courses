package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient {

    public static String askServer(String hostname, int port, String ToServer) throws IOException {
        byte[] fromServerBuffer = new byte[1024];
        String decodedString = new String();
        byte[] encodedString = ToServer.getBytes();
        Socket clientSocket = new Socket(hostname, port);
        int count = 0;
        StringBuilder builtString = new StringBuilder();

        clientSocket.getOutputStream().write(encodedString);

        while((count = clientSocket.getInputStream().read(fromServerBuffer)) > 0) {
            decodedString = new String(fromServerBuffer, 0, count, StandardCharsets.UTF_8);
            builtString.append(decodedString);
        }

        clientSocket.close();

        return builtString.toString();
    }

    public static String askServer(String hostname, int port) throws IOException {
        byte[] fromServerBuffer = new byte[1024];
        String decodedString = new String();
        Socket clientSocket = new Socket(hostname, port);
        int count = 0;
        StringBuilder builtString = new StringBuilder();

        while((count = clientSocket.getInputStream().read(fromServerBuffer)) > 0) {
            decodedString = new String(fromServerBuffer, 0, count, StandardCharsets.UTF_8);
            builtString.append(decodedString);
        }

        clientSocket.close();

        return builtString.toString();
    }
}



