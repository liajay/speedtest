package lib;
import java.io.*;
import java.net.*;
/*-------------创建线程来接受UDP数据包，可指定端口和单个包长度 */
public class serverUDPThread extends Thread{
    private DatagramSocket serverSocket;
    private DatagramPacket reciDatagramPacket;
    private byte[] reciData;
    private int reciPort;
    static public int maxDelayTime = 0,minDelayTime = 0x7fffffff,avgDelayTime = 0;
    private int lengthPerPacket;
    private long startTime,endTime;
    public static int MAX_UDP_PACKET_SIZE = 65507;
    public int reciPacketNumber = 0;
    public serverUDPThread(int reciPort,int lengthPerPacket){
        try{
            if(lengthPerPacket > MAX_UDP_PACKET_SIZE)
                throw new IllegalArgumentException("lengthPerPacket must be less than "+MAX_UDP_PACKET_SIZE);

            this.reciPort = reciPort;
            this.lengthPerPacket = lengthPerPacket;
            this.serverSocket = new DatagramSocket(reciPort);
            this.serverSocket.setSoTimeout(5000);
            System.out.println("Server UDP thread init \nPort: " + reciPort);
        }catch(Exception e){
            System.out.println("Error: "+e);
        }

        maxDelayTime = 0;
        minDelayTime = 0x7fffffff;
        avgDelayTime = 0;
        
    }

    public void run (){ 
        try{
            System.out.println("Server UDP thread start");
            this.reciData = new byte[lengthPerPacket];
            startTime = System.currentTimeMillis();
            int cnt = 0;
            int delayTime = 0,totalDelayTime = 0;
            
            while (true){
                this.reciDatagramPacket = new DatagramPacket(this.reciData,this.reciData.length);
                this.serverSocket.receive(this.reciDatagramPacket); 
                //System.out.printf("Receive packet count: %d\n", ++cnt);
                /*---------解析信息头--------- */
                byte[] clientInfo = new byte[infoHeader.infoHeaderLength];
                System.arraycopy(this.reciDatagramPacket.getData(), 0, clientInfo, 0, infoHeader.infoHeaderLength);
                infoHeader infoHeader = new infoHeader(clientInfo);

                /*---------长度为0视为结束传输--------- */
                if(infoHeader.getLength() == 0){
                    endTime = System.currentTimeMillis();
                    int TotalTime = (int)(endTime-startTime);
                    long speed = ((long)reciPacketNumber * lengthPerPacket * 1000) / (1024 * 1024 *TotalTime);
                    System.out.println("\n" + "------------------------");
                    System.out.println("Total packet number: "+reciPacketNumber);
                    System.out.println("Total data: "+((long)reciPacketNumber * lengthPerPacket * 1000)+" bytes");
                    System.out.println("Time: "+TotalTime+"ms");
                    System.out.println("Max delay time: "+maxDelayTime+"ms");
                    System.out.println("Min delay time: "+minDelayTime+"ms");
                    System.out.println("Avg delay time: "+avgDelayTime+"ms");
                    System.out.println("Speed: "+ speed +"MB/s");
                    System.out.println("------------------------" + "\n");
                    break;
                }

                delayTime = (int)(System.currentTimeMillis() - infoHeader.getSendTime());
                totalDelayTime += delayTime;

                if(delayTime > maxDelayTime)
                    maxDelayTime = delayTime;
                if(delayTime < minDelayTime)
                    minDelayTime = delayTime;

                avgDelayTime = totalDelayTime / (reciPacketNumber + 1);
                reciPacketNumber++;
            }

            serverSocket.close();

        }catch(Exception e){
            System.out.println("Error: "+e);
            
        }
    }    

    public int getRunTime(){
        return (int)(endTime-startTime);
    }
}
