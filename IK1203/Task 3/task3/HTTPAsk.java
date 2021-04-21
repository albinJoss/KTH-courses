import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import tcpclient.TCPClient;

public class HTTPAsk {
    public static void main( String[] args) throws  IOException{
        ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]));
        String testString = new String();
        while(true) {
            Socket connection = socket.accept();
            //System.out.println("accepted");
            byte[] fromClientBuffer = new byte[100];
            StringBuilder sb = new StringBuilder();
            int fromClientLength = 0;
            boolean emptyLine = false;


            while(emptyLine == false && (fromClientLength = connection.getInputStream().read(fromClientBuffer)) <= fromClientBuffer.length) {
                //System.out.println("while");
                if(fromClientLength != -1) {
                    testString = new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8);
                    sb.append(testString);
                }
                if(testString.contains("\r\n\r\n") || testString.contains("\\r\\n\\r\\n") || testString.contains("\n\n") || fromClientLength == -1 || testString.contains("favicon")) {
                    emptyLine = true;
                }
                //System.out.println(emptyLine);
                //System.out.println(sb.toString());
            }
            //System.out.println(sb.toString());
            if(sb.toString().contains("favicon")) {
                connection.close();
                //System.out.println("closed favicon");
            }
            else {
                String get[] = sb.toString().split("\r\n");
                String split[] = sb.toString().split(" ");
                String splitWithAnd[] = split[1].split("&");
                if(!(get[0].contains("GET") && get[0].contains("HTTP/1.1"))){
                    connection.getOutputStream().write("HTTP/1.1  400 Bad Request\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                    connection.close();
                    //System.out.println("closed 400");
                }
                else if(!((get[0].contains("/ask?hostname=") && get[0].contains("port=") && splitWithAnd.length == 2) || (get[0].contains("/ask?hostname=") && get[0].contains("port=") && get[0].contains("string=") &&splitWithAnd.length == 3))) {
                    connection.getOutputStream().write("HTTP/1.1  404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                    connection.close();
                    //System.out.println("closed 404");
                }
                else{

                    String splitWithEquals[] = new String[2];
                    String hostPortString[] = new String[splitWithAnd.length];
                    for (int i = 0; i < splitWithAnd.length; i++) {
                        splitWithEquals = splitWithAnd[i].split("=");
                        hostPortString[i] = splitWithEquals[1];
                        //System.out.println("row " + i + " contains the string: " + hostPortString[i]);
                    }


                        String output = new String();
                        try {
                            output = hostPortString.length == 2 ?
                                    TCPClient.askServer(hostPortString[0], Integer.parseInt(hostPortString[1])) :
                                    TCPClient.askServer(hostPortString[0], Integer.parseInt(hostPortString[1]), hostPortString[2]);
                            //System.out.println(output);
                        } catch (UnknownHostException e) {
                            output = "Unknown host";
                        }

                        if(!output.isEmpty()) {
                            try {
                                connection.getOutputStream().write("HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8));
                                connection.getOutputStream().write(output.getBytes(StandardCharsets.UTF_8));
                            } catch (SocketException se) {
                                connection.getOutputStream().write("HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8));
                                connection.getOutputStream().write(output.getBytes(StandardCharsets.UTF_8));
                            }
                        }
                        else
                            connection.getOutputStream().write("HTTP/1.1  404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));


                    connection.close();
                    //System.out.println("closed");
                    //System.out.println(sb.toString());
                }
            }

        }

    }
}

