package log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServLogger {
    public final String ANSI_RESET = "\u001B[0m";
    public String ANSI_BLACK = "\u001B[30m";
    public String ANSI_RED = "\u001B[31m";
    public String ANSI_GREEN = "\u001B[32m";
    public String ANSI_YELLOW = "\u001B[33m";
    public String ANSI_BLUE = "\u001B[34m";
    public String ANSI_PURPLE = "\u001B[35m";
    public String ANSI_CYAN = "\u001B[36m";
    public String ANSI_WHITE = "\u001B[37m";
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    protected AtomicInteger num = new AtomicInteger(0);
    private final ConcurrentHashMap<String, Integer> frag = new ConcurrentHashMap<>();
    private static volatile ServLogger INSTANCE = null;

    private ServLogger() {
    }

    public String log(String level, String color, String msg) {
        frag.put(level, frag.getOrDefault(level, 0) + 1);
        String s = String.format("[%s %3d]\n%s (%d) : %s\n",
                dtf.format(LocalDateTime.now()),
                num.incrementAndGet(),
                level.toUpperCase(),
                frag.get(level),
                msg);
        System.out.println(color + s + ANSI_RESET);
        writeLog(s);
        return s;
    }

    public static ServLogger getInstance() {
        if (INSTANCE == null) {
            synchronized (ServLogger.class) {
                if (INSTANCE == null)
                    INSTANCE = new ServLogger();
            }
        }
        return INSTANCE;
    }

    public void writeLog(String s) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter("src/main/resources/servLog.log", true));
            bf.write(s);
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAnsiRed() {
        return ANSI_RED;
    }

    public String getAnsiBlack() {
        return ANSI_BLACK;
    }

    public String getAnsiGreen() {
        return ANSI_GREEN;
    }

    public String getAnsiYellow() {
        return ANSI_YELLOW;
    }

    public String getAnsiBlue() {
        return ANSI_BLUE;
    }

    public String getAnsiPurple() {
        return ANSI_PURPLE;
    }

    public String getAnsiCyan() {
        return ANSI_CYAN;
    }

    public String getAnsiWhite() {
        return ANSI_WHITE;
    }
}

