import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static int CORES = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        System.out.println("Обнаружено ядер: " + CORES);

        String srcFolder = "img/src";
        String dstFolder = "img/dst";

        File folder = new File(dstFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File srcDir = new File(srcFolder);
        File[] files = srcDir.listFiles();
        int newWidth = 300;
        int filesLength = files.length;
        int threadLength;
        int srcPos = 0;

//        Thread[] threads = new Thread[CORES];

        ExecutorService executor = Executors.newFixedThreadPool(CORES);

        for (int i = CORES; i > 0; i--) {
            threadLength = filesLength / i;
            File[] filesInThread = new File[threadLength];
            System.arraycopy(files, srcPos, filesInThread, 0, threadLength);
            ImageResizer resizer = new ImageResizer(filesInThread, dstFolder, newWidth);

/*            // added files in threads dimension
            threads[Math.abs(i - CORES)] = new Thread(resizer, "Thread-" + String.valueOf(Math.abs(i - CORES) + 1));*/

            executor.execute(resizer);

            filesLength = filesLength - threadLength;
            srcPos = srcPos + threadLength;
        }

        executor.shutdown();
        try {
            System.out.println(executor.awaitTermination(1, TimeUnit.MINUTES));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

/*        long start = System.currentTimeMillis();

        for (Thread thread: threads) {
            thread.start();
            System.out.println(thread.getName() + " start");
        }
        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread.getName() + " join");
        }

        System.out.println("All Threads finished in " + (System.currentTimeMillis() - start) + " ms");*/
    }
}
