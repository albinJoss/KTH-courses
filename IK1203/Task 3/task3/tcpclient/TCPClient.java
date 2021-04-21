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
        clientSocket.setSoTimeout(6000);
        int count = 0;
        StringBuilder builtString = new StringBuilder();

        clientSocket.getOutputStream().write(encodedString);
        try {
            while ((count = clientSocket.getInputStream().read(fromServerBuffer)) > 0) {
                decodedString = new String(fromServerBuffer, 0, count, StandardCharsets.UTF_8);
                builtString.append(decodedString);
                //System.out.println(builtString.toString());
            }
        }
        catch (SocketTimeoutException ex){}
        if(!builtString.toString().contains("\r\n\r\n"))
            builtString.append("\r\n\r\n");

        //System.out.println("out");
        clientSocket.close();

        return builtString.toString();
    }

    public static String askServer(String hostname, int port) throws IOException {
        byte[] fromServerBuffer = new byte[1024];
        String decodedString = new String();
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(5080);

        int count = 0;
        StringBuilder builtString = new StringBuilder();
        try {
            while ((count = clientSocket.getInputStream().read(fromServerBuffer)) > 0) {
                decodedString = new String(fromServerBuffer, 0, count, StandardCharsets.UTF_8);
                builtString.append(decodedString);
            }
        }
        catch(SocketTimeoutException ex) {}
        clientSocket.close();

        return builtString.toString();
    }
}



