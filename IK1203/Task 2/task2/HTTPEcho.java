import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho {
    public static void main( String[] args) throws IOException{
        ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]));
        String testString = new String();

        while(true) {
            Socket connection = socket.accept();
            byte[] fromClientBuffer = new byte[10];
            StringBuilder sb = new StringBuilder();
            int fromClientLength = 0;
            boolean emptyLine = false;


            while( emptyLine == false && (fromClientLength = connection.getInputStream().read(fromClientBuffer)) <= fromClientBuffer.length) {

                if(fromClientLength==-1)
                {
                    break;
                }
                testString = new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8);
                sb.append(testString);
                if(sb.toString().contains("\r\n\r\n") || sb.toString().contains("\n\n")) {
                            emptyLine = true;
                }
            }

            String finalString = sb.toString();
            byte[] toClientBuffer = finalString.getBytes(StandardCharsets.UTF_8);

            connection.getOutputStream().write("HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().write(toClientBuffer);

            connection.close();
        }

    }
}

