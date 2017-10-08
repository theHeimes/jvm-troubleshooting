import java.lang.ref.WeakReference;
import java.util.*;
import java.util.LinkedList;

public class JavaHeapMemoryLeak {

    private static final Random rndGenerator = new Random();
    private LinkedList arrObjects = new LinkedList();
    private LinkedList<WeakReference> arrSoftRefs = new LinkedList();

    public static void main(String[] args) {
        int allocFrequency = DEFAULT_FREQUENCY;
        long minSize = DEFAULT_MIN_SIZE,
                maxSize = DEFAULT_MAX_SIZE;

        new JavaHeapMemoryLeak().allocateObjects(minSize, maxSize, allocFrequency);
    }

    void allocateObjects(long minSize, long maxSize, int allocFrequency) {
        long counter = 0;
        long multiplier = maxSize - minSize;

        while (true) {
                // Keep every nth object in arrObjects
                if (counter % allocFrequency != 0) {
                    long allocationSize = ((int) (rndGenerator.nextDouble() * multiplier))
                            + minSize;

                    arrObjects.add((new byte[(int) allocationSize]));

                    // The added byte[] instances are not removed from arrObjects anywhere in the program
                    // and that can lead to OOME for the java heap space.
                    // For such situations in your applications, consider either of the following two solutions:
                    // 1. Determine where and how these objects should be removed from the collection objects
                    // e.g.
                    // int size = arrObjects.size();
                    // if (size >= 100) {
                    //   for (int i=0; i<size; i++) {
                    //    arrObjects.remove();
                    //   }
                    // }
                    // 2. Look at the application design and see if the stored objects can be stored as soft/weak
                    // references. e.g.
                    //arrObjects.add(new SoftReference( new byte[(int)allocationSize]));  // store soft reference
                    //arrObjects.add(new WeakReference( new byte[(int)allocationSize]));  // store weak reference
                } else {
                    arrSoftRefs.add(new WeakReference(new Object()));
                }

                counter++;
                if (counter == Long.MAX_VALUE) {
                    counter = 0;
                }
            }
    }

    private static final long DEFAULT_MIN_SIZE = 512;
    private static final long DEFAULT_MAX_SIZE = 1024;
    private static final int DEFAULT_FREQUENCY = 4;
}