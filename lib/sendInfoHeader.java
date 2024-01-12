package lib;
import java.nio.ByteBuffer;

/* sendinfoHeader是客户端发送给服务器的信息头
 * 继承自infoHeader
 * sendInfoHeader: 发送信息头，包含发送时间，帧序号，长度，总包数，请求端口，每个包的长度
 * 
 */

public class sendInfoHeader extends infoHeader{
    int totalPacketNumber; //将要接受的总包数
    int requestPort; //请求的端口
    int lengthPerPacket; //每个包的长度
    public static int infoHeaderLength = 26;
    static public final int typeNumber = 1;
    /*-----------------三种初始化方式---------------- */
    public sendInfoHeader(long sendTime,int frameNumber,short length,int packetNumber, int requestPort, int lengthPerPacket){
        super(sendTime,frameNumber,length);
        this.totalPacketNumber = packetNumber;
        this.requestPort = requestPort;
        this.lengthPerPacket = lengthPerPacket;
    }
    public sendInfoHeader(byte[] data){
        this(parseData(data));
    }

    public sendInfoHeader(sendInfoHeader other){
    super(other.getSendTime(), other.getFrameNumber(), other.getLength());
    this.totalPacketNumber = other.getTotalPacketNumber();
    this.requestPort = other.getRequestPort();
    this.lengthPerPacket = other.getLengthPerPacket();
    }
    /*-----------------静态辅助构造函数---------------- */
    private static sendInfoHeader parseData(byte[] data){
        if (data.length != infoHeaderLength){
            throw new IllegalArgumentException("Data length must be "+infoHeaderLength);
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return new sendInfoHeader(buffer.getLong(), buffer.getInt(), buffer.getShort(), buffer.getInt(), buffer.getInt(), buffer.getInt());
    }

    /*-----------------转换为字节流---------------- */
    public byte[] toBytes (){
        ByteBuffer buffer = ByteBuffer.allocate(infoHeaderLength);
        buffer.putLong(this.getSendTime());
        buffer.putInt(this.getFrameNumber());
        buffer.putShort(this.getLength());
        buffer.putInt(this.totalPacketNumber);
        buffer.putInt(this.requestPort);
        buffer.putInt(this.lengthPerPacket);
        return buffer.array();
    }

    public int getTotalPacketNumber(){
        return this.totalPacketNumber;
    }

    public int getRequestPort(){
        return this.requestPort;
    }

    public int getLengthPerPacket(){
        return this.lengthPerPacket;
    }

    public void showInfo(){
        System.out.println("----------------sendInfoHeader----------------");
        super.showInfo();
        System.out.println("totalPacketNumber: "+this.totalPacketNumber);
        System.out.println("requestPort: "+this.requestPort);
        System.out.println("lengthPerPacket: "+this.lengthPerPacket);
        System.out.println("----------------------------------------------" + "\n");
    }
    
}