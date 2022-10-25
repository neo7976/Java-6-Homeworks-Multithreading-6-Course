import thread.ClientTest;
import thread.ThreadReadMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Проверяем несколько пользователей на использование сервера
 */
public class Main {

    public static void main(String[] args) {
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 10; i++) {
            ex.execute(new ClientTest());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ex.shutdown();
    }
}
