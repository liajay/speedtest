package lib;
import java.net.*;
import java.io.*;

/*
 * 用于传输UDP数据包的线程
 * 需要指定目标地址、目标端口、单个数据包长度、文件名
 */

public class clientUDPThread extends Thread{
    private String address;
    private int port;
    private int lengthPerPacket;
    private String fileName;
    private FileInputStream sendFileData;
    public static int MAX_UDP_PACKET_SIZE = 65507;
    public clientUDPThread(String address,int port,int lengthPerPacket,FileInputStream sendFileData){
        try{
            if(lengthPerPacket > MAX_UDP_PACKET_SIZE)
                throw new IllegalArgumentException("lengthPerPacket must be less than "+MAX_UDP_PACKET_SIZE);
            if(port < 1024 || port > 65535)
                throw new IllegalArgumentException("port must be between 1024 and 65535");
            if(lengthPerPacket < infoHeader.infoHeaderLength)
                throw new IllegalArgumentException("lengthPerPacket must be greater than "+infoHeader.infoHeaderLength);

            this.address = address;
            this.port = port;
            this.lengthPerPacket = lengthPerPacket;
            this.sendFileData = sendFileData;

            System.out.println("\nClient UDP thread init \nAddress: " + address + "\nPort: " + port + "\nlengthPerPacket: " + lengthPerPacket + "\nfileName: " + fileName);
        }
        catch(Exception e){
            System.out.println("Error: "+e);
        }
        
    }

    public void run(){
        try{
            

            int sizeOfData = sendFileData.available();
            InetAddress serverAddress = InetAddress.getByName(address);
            int sendSize = sizeOfData / (lengthPerPacket - infoHeader.infoHeaderLength) + 1;//计算需要发送的数据包数量
            infoHeader info = new infoHeader(System.currentTimeMillis(), 1, (short)infoHeader.infoHeaderLength);
            byte[] data = new byte[lengthPerPacket];
            DatagramSocket clientSocket = new DatagramSocket();

            while (sendSize-- > 0) {
                
                //System.out.println(sendSize)  ;
                

                if(sendFileData.available() > lengthPerPacket - infoHeader.infoHeaderLength) {
                    byte [] infoBytes = info.toBytes();
                    System.arraycopy(infoBytes, 0, data, 0, infoBytes.length);
                    System.arraycopy(sendFileData.readNBytes(lengthPerPacket - infoBytes.length), 0, data, infoBytes.length, lengthPerPacket - infoBytes.length);
                } else {
                    continue;
                }

                DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, port);
                clientSocket.send(sendPacket); 

                info.setFrameNumber(info.getFrameNumber() + 1);
                info.setSendTime(System.currentTimeMillis());
            }

            /*----------最后一个数据包，作为结束标志----------- */
            byte[] lastData = new infoHeader((long)0, 0, (short)0).toBytes();
            DatagramPacket sendPacket = new DatagramPacket(lastData, lastData.length, serverAddress, port);
            clientSocket.send(sendPacket);

            System.out.println("send finish, remain:" + sendSize);
   
            clientSocket.close();
        }catch(Exception e){
            System.out.println("Error: "+e);
        }

        
    }
}