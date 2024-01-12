import java.io.*;
import java.net.*;
import lib.serverUDPThread;
import lib.sendInfoHeader;
import lib.infoHeader;
public class server {
    public static void main(String[] args){
        try{
           
            while(true){
                /*------------创建线程与客户端连接-------------- */
                serverTCPThread serverTCPThread = new serverTCPThread();
                serverTCPThread.start();
                serverTCPThread.join();
                
            }
            
            
        }catch(Exception e){                                                                                                      
            System.out.println("Error: "+e);
        }

        

    }
}

/*---------------------创建一个线程与一个客户端连接------------------ */
class serverTCPThread extends Thread{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    static public final int PORT = 1234;
    private int lengthPerPacket = 65507;
    private final static int maxAvailablePort = 1234, minAvailablePort = 1024;
    private static boolean[] availablePort = new boolean[maxAvailablePort - minAvailablePort];

    public serverTCPThread(){
        System.out.println("serverTCPThread Start : get port " + PORT);
    }

    public static int getAnAvailablePort(){
        for (int i = minAvailablePort; i < maxAvailablePort; i++) {
            if(availablePort[i - minAvailablePort] == false){
                availablePort[i - minAvailablePort] = true;
                return i;
            }
        }

        throw new RuntimeException("serverTCPThread: no available port");
    }


    private static void freePort(int port){
        availablePort[port - minAvailablePort] = false;
    }

    public void run(){
        try{
            System.out.println("waiting for connection...");
            this.serverSocket = new ServerSocket(PORT);
            this.clientSocket = serverSocket.accept();
            System.out.println("connect form "+clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
            System.out.println("Local Port: "+PORT);

            /*------------连接成功后初始化输入输出流-------------- */
            DataInputStream recvFromClient = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream sendToClient = new DataOutputStream(clientSocket.getOutputStream());
            sendToClient.writeUTF("connect success");

            /*------------接受文件头-------------- */
            byte[] infoHeaderBytes = new byte[sendInfoHeader.infoHeaderLength];
            recvFromClient.read(infoHeaderBytes);
            sendInfoHeader infoHeader = new sendInfoHeader(infoHeaderBytes);
            infoHeader.showInfo();

            /*------------创建一个UDP线程开始接受-------------- */
            System.out.println("start receive");

            int port = getAnAvailablePort(); //随机获取一个可用端口
            sendToClient.writeInt(port);
            serverUDPThread serverUDPThread = new serverUDPThread(port,infoHeader.getLengthPerPacket());

            serverUDPThread.start();
            serverUDPThread.join();

            freePort(port);

            clientSocket.close();
            serverSocket.close();

        }catch(Exception e){
            System.out.println("Error: "+e);
        }
    }
}
