import java.net.*;
import lib.clientUDPThread;
import lib.sendInfoHeader;
import lib.infoHeader;
import java.io.*;

public class client {
    public static void main(String[] args) {
        try {
            if(args[0] == null){
                throw new IllegalArgumentException("serverIp must be specified");
            }

            if(args[1] == null){
                throw new IllegalArgumentException("serverPort must be specified");
            }

            if(args[2] == null){
                throw new IllegalArgumentException("fileName must be specified");
            }

            args[2] = "test.exe";

            String serverIp = args[0];
            int serverPort = Integer.parseInt(args[1]);

            Socket serverSocket = new Socket(serverIp, serverPort);
            DataInputStream recvFromServer = new DataInputStream(serverSocket.getInputStream());
            DataOutputStream sendToServer = new DataOutputStream(serverSocket.getOutputStream());
            serverSocket.setSoTimeout(60000);


            /*------------接受连接成功信息以及发送传输文件头-------------- */
            String connetInfo =recvFromServer.readUTF();

            if (connetInfo.equals("connect success")) {
                System.out.println("connect success");
            } else {
                serverSocket.close();
                throw new Exception("connect failed");
            }

            FileInputStream sendFileData = new FileInputStream(args[2]);

            int lengthPerPacket = 1024;
            int sizeOfData = sendFileData.available();
            int sendSize = sizeOfData / (lengthPerPacket - infoHeader.infoHeaderLength) + 1;
            sendInfoHeader senfInfo = new sendInfoHeader(System.currentTimeMillis(), 1, (short)sendInfoHeader.infoHeaderLength, sendSize, 1234, lengthPerPacket);
            sendToServer.write(senfInfo.toBytes());

            /*------------开始传输-------------- */
            Thread.sleep(1000);
            System.out.println("start send");
            int port = recvFromServer.readInt();
            clientUDPThread clientUDPThread = new clientUDPThread(serverIp, port, lengthPerPacket, sendFileData);
            clientUDPThread.start();
            clientUDPThread.join();
            

            
                    
            /*------------传输结束-------------- */

            serverSocket.close();
            sendFileData.close();
        } catch (Exception e) {
            
            System.out.println("Error: " + e);
        }
    }
    
}
