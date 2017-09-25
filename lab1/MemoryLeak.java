import java.util.*;

public class MemoryLeak {
    Vector longLivedObjects = new Vector();    
    
    void createShortLivedObjects() {        
        HeapObject ho = new HeapObject();        
    }
    
    void createLongLivedObjects() {
        HeapObject ho = new HeapObject();
        longLivedObjects.add(ho);        
    }
    
    class HeapObject {
        byte[] b;
        public HeapObject() {
            b = new byte[100];
        }
    }
    
    public static void main(String args[]) {
        MemoryLeak leak = new MemoryLeak();
        
       
        while(true) {
            try {
                leak.createShortLivedObjects();
                leak.createLongLivedObjects();
                Thread.sleep(1000);
            } catch (Exception e) {}
        }
        
    }
}