package lib;
import java.net.*;

public class speedTestSocket {
    private DatagramSocket clientSocket,serverSocket;
    private DatagramPacket senDatagramPacket,reciDatagramPacket;
    private byte[] sendData,reciData;
    private InetAddress IPAddress;
    private int sendPort,reciPort;
    private long startTime,endTime;
    public static int MAX_UDP_PACKET_SIZE = 65507;
    public speedTestSocket(String ip,int sendPort,int reciPort){
        try{
            this.sendPort = sendPort;
            this.reciPort = reciPort;
            this.IPAddress = InetAddress.getByName(ip);
            this.clientSocket = new DatagramSocket();
            this.serverSocket = new DatagramSocket(reciPort);
        }catch(Exception e){
            System.out.println("Error: "+e);
        }
    }

    private void send(int length){
        try{
            this.sendData = new byte[length];
            this.senDatagramPacket = new DatagramPacket(this.sendData,this.sendData.length,this.IPAddress,this.sendPort);
            this.clientSocket.send(this.senDatagramPacket);
            System.out.println("Send: "+length+" bytes");
        }catch(Exception e){
            System.out.println("Error: "+e);
        }
    }

    public void run (){ 
        try{
            this.reciData = new byte[1024];
            startTime = System.currentTimeMillis();
            send(1024);
            this.reciDatagramPacket = new DatagramPacket(this.reciData,this.reciData.length);
            this.serverSocket.receive(this.reciDatagramPacket);
            endTime = System.currentTimeMillis();
            System.out.println("Time: "+(endTime-startTime)+"ms");
        }catch(Exception e){
            System.out.println("Error: "+e);
        }

    }
}


