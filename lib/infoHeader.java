package lib;
import java.nio.ByteBuffer;

/* infoHeader是数据包的最基本的信息头
 * 包含：发送时间，帧序号，长度
 */

public class infoHeader{
    static public final int typeNumber = 0;
    private long sendTime;
    private int frameNumber;
    private short length;
    public static int infoHeaderLength = 14;

    public infoHeader(long sendTime,int frameNumber,short length){
        this.sendTime = sendTime;
        this.frameNumber = frameNumber;
        this.length = length;
    }

    public infoHeader(byte[] data){
        this(parseData(data));
    }

    public infoHeader(infoHeader other){
    this(other.getSendTime(), other.getFrameNumber(), other.getLength());
    }
    
    private static infoHeader parseData(byte[] data){
        if (data.length != infoHeaderLength){
            throw new IllegalArgumentException("Data length must be "+infoHeaderLength);
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return new infoHeader(buffer.getLong(), buffer.getInt(), buffer.getShort());
    }

    /*-----------------转换为字节流---------------- */
    public byte[] toBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(infoHeaderLength);
        buffer.putLong(this.sendTime);
        buffer.putInt(this.frameNumber);
        buffer.putShort(this.length);
        return buffer.array();
    }
    
    public long getSendTime(){
        return this.sendTime;
    }

    public int getFrameNumber(){
        return this.frameNumber;
    }

    public short getLength(){
        return this.length;
    }

    public void setFrameNumber(int frameNumber){
        this.frameNumber = frameNumber;
    }

    public void setLength(short length){
        this.length = length;
    }

    public void setSendTime(long sendTime){
        this.sendTime = sendTime;
    }
    
    public void showInfo(){
        System.out.println("sendTime: "+this.sendTime);
        System.out.println("frameNumber: "+this.frameNumber);
        System.out.println("length: "+this.length);
    }
}




