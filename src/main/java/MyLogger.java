import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MyLogger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    protected AtomicInteger num = new AtomicInteger(0);
    private ConcurrentHashMap<String, Integer> frag = new ConcurrentHashMap<>();
    private static volatile MyLogger INSTANCE = null;

    private MyLogger() {
    }

    public String log(String userName, String msg) {
        frag.put(userName, frag.getOrDefault(userName, 0) + 1);
        String s = String.format("[Date: %s %3d]\n -> %s%s\n", dtf.format(LocalDateTime.now()), num.incrementAndGet(), userName, msg);
        writeLog(s);
        return s;
    }

    public static MyLogger getInstance() {
        if (INSTANCE == null) {
            synchronized (MyLogger.class) {
                if (INSTANCE == null)
                    INSTANCE = new MyLogger();
            }
        }
        return INSTANCE;
    }

    public void writeLog(String s) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter("src/main/resources/myLog.log", true));
            bf.write(s);
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}