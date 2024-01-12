package lib;
import java.nio.ByteBuffer;
public interface command {
    public static final String cmdName = null;
    public void run();

}

/*----------该类将会对各种文件头进行分析----------- */
class analysis implements command {
    public static final String cmdName = "analysis";
    private byte[] data;
    public analysis(byte[] data) {
        this.data = data;
    }
    public void run() {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int typeNumber = buffer.getInt();
        switch (typeNumber) {
            case infoHeader.typeNumber:
                
                break;
            case sendInfoHeader.typeNumber:
                
                break;
            default:
                break;
        }
    }

}
