import java.nio.ByteBuffer;
import lib.sendInfoHeader;
import java.io.*;
public class main {
    static public void main(String[] args){
        try{
            FileInputStream test = new FileInputStream("test.exe");
            System.out.println(test.available());
            byte[] data = test.readNBytes(1024);
            System.out.println(test.available());
        }
        catch(Exception e){
            System.out.println("Error: "+e);
        }
        
    }    
}
