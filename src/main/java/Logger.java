import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    protected int num = 1;
    private static Logger instance = null;

    private Logger() {
    }

    public String log(String msg) {
       return String.format("[Date: %s %3d] -> %s\n", dtf.format(LocalDateTime.now()), num++, msg);
    }

    public static Logger getInstance() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }
}