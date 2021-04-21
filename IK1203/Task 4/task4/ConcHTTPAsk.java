import tcpclient.TCPClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException {
	    try {
            ThreadPoolExecutor service = (ThreadPoolExecutor)Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

            while (true) {
                Socket connectedSocket = serverSocket.accept();
                //System.out.println("accepted");
                service.submit(new MyRunnable(connectedSocket));
            }
        }
	    catch (IOException ie) {
            //System.out.println("main");
        }
    }

    static class MyRunnable implements Runnable {
        Socket connectedSocket;

        @Override
        public void run() {
                String request = ConcHTTPAsk.InnerWhile(connectedSocket);
                if (ConcHTTPAsk.isOutputOkay(connectedSocket, request)) {
                    sendingOutput(connectedSocket, request);
                }

        }

        public MyRunnable(Socket connectedSocket){
            this.connectedSocket = connectedSocket;
        }
    }

    public static String InnerWhile(Socket connectedSocket){
                StringBuilder sb = new StringBuilder();
                int fromClientLength = 0;
                boolean emptyLine = false;
                String testString = new String();
                byte[] fromClientBuffer = new byte[100];

                try {
                    while (!emptyLine && (fromClientLength = connectedSocket.getInputStream().read(fromClientBuffer)) <= fromClientBuffer.length) {
                        //System.out.println("while");
                        if (fromClientLength != -1) {
                            testString = new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8);
                            sb.append(testString);
                        }
                        if (testString.contains("\r\n\r\n") || testString.contains("\\r\\n\\r\\n") || testString.contains("\n") || fromClientLength == -1 || testString.contains("favicon")) {
                            emptyLine = true;
                        }
                        //System.out.println(emptyLine);
                        //System.out.println(sb.toString());
                    }
                }
                catch(IOException ie) {
                    //System.out.println("Innerwhile");
                }
                return sb.toString();
            }

            public static boolean isOutputOkay(Socket connectedSocket, String request) {
                try {
                    if (request.contains("favicon")) {
                        connectedSocket.close();
                        //System.out.println("closed favicon");
                        return false;
                    } else {
                        String get[] = request.split("\r\n");
                        String split[] = request.split(" ");
                        String splitWithAnd[] = split[1].split("&");
                        if (!(get[0].contains("GET") && get[0].contains("HTTP/1.1"))) {
                            connectedSocket.getOutputStream().write("HTTP/1.1  400 Bad Request\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                            connectedSocket.close();
                            //System.out.println("closed 400");
                            return false;
                        } else if (!((get[0].contains("/ask?hostname=") && get[0].contains("port=") && splitWithAnd.length == 2) || (get[0].contains("/ask?hostname=") && get[0].contains("port=") && get[0].contains("string=") && splitWithAnd.length == 3))) {
                            connectedSocket.getOutputStream().write("HTTP/1.1  404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                            connectedSocket.close();
                            //System.out.println("closed 404");
                            return false;
                        }
                        return true;
                    }
                }
                catch(IOException ie) {
                    //System.out.println("isOutputOkay");
                }
                return false;
            }


            public static void sendingOutput(Socket connectedSocket, String request){
                try {
                    String split[] = request.split(" ");
                    String splitWithAnd[] = split[1].split("&");
                    String splitWithEquals[] = new String[2];
                    String hostPortString[] = new String[splitWithAnd.length];


                    for (int i = 0; i < splitWithAnd.length; i++) {
                        splitWithEquals = splitWithAnd[i].split("=");
                        hostPortString[i] = splitWithEquals[1];
                        //System.out.println("row " + i + " contains the string: " + hostPortString[i]);
                    }

                    String output = sendToTCPClient(hostPortString);

                    if(!output.isEmpty()) {
                        try {
                            connectedSocket.getOutputStream().write("HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8));
                            connectedSocket.getOutputStream().write(output.getBytes(StandardCharsets.UTF_8));
                        } catch (SocketException se) {
                            connectedSocket.getOutputStream().write("HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8));
                            connectedSocket.getOutputStream().write(output.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                    else
                        connectedSocket.getOutputStream().write("HTTP/1.1  404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));


                    connectedSocket.close();
                    //System.out.println("closed");
                    //return true;

                }
                catch (IOException ie) {
                    //return false;
                    //System.out.println("sendingOutput");
                }
            }

            public static String sendToTCPClient(String[] hostPortString){
                String output = new String();
                try {
                    output = hostPortString.length == 2 ?
                            TCPClient.askServer(hostPortString[0], Integer.parseInt(hostPortString[1])) :
                            TCPClient.askServer(hostPortString[0], Integer.parseInt(hostPortString[1]), hostPortString[2]);
                    //System.out.println(output);
                } catch (UnknownHostException e) {
                    output = "Unknown host";
                    //System.out.println("sendToTCPClient, unknownhost");
                }
                catch(IOException ie) {
                    //System.out.println("sendToTCPClient, IO");
                }

                return output;
            }

}
