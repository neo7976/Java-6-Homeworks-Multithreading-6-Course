package thread;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ThreadReadMessage extends Thread {
    private int count = -1;

    @Override
    public void run() {
        try {
            BufferedReader it = new BufferedReader(new FileReader("src/main/resources/myLog.log"));
            int innerCount = 0;
            String line;
            while (!isInterrupted()) {
                while ((line = it.readLine()) != null) {
                    innerCount++;
                    //если читали и надо продолжить
                    if (count > -1) {
                        if (innerCount > count) {
                            System.out.println(line);
                        }
                    } else {
                        //читаем первый раз
                        System.out.println(line);
                    }
                }
                count = innerCount;
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

