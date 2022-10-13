import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLogger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    protected int num = 1;
    private static MyLogger instance = null;

    private MyLogger() {
    }

    public String log(String msg) {
       return String.format("[Date: %s %3d] -> %s\n", dtf.format(LocalDateTime.now()), num++, msg);
    }

    public static MyLogger getInstance() {
        if (instance == null)
            instance = new MyLogger();
        return instance;
    }
}