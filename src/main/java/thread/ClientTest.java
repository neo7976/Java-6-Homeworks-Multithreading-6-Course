package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest implements Runnable {

    static Socket socket;

    public ClientTest() {
        try {
            socket = new Socket("127.0.0.1", 1254);
            System.out.println("Клиент подключен");
            Thread.sleep(100);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Клиент подключился to socketClient.");

            System.out.println("\nВведите свое имя для знакомства с сервером!\n" +
                    "После вводите сообщения для отправки пользователям или /end для выхода из канала:");
            String name = Thread.currentThread().getName();
            out.println(name);
            out.flush();
            Thread.sleep(1000);
            ThreadReadMessage send = new ThreadReadMessage();
            send.start();
            for (int i = 1; i <= 5; i++) {
                String msg = "Сообщение - " + i;
                out.println(msg);
                out.flush();
                Thread.sleep(1000);
                if (in.read() > -1) {
                    msgFromServer(in);
                }
            }
            String msg = "/end";
            out.println(msg);
            out.flush();
            Thread.sleep(1000);
            if (msg.equalsIgnoreCase("/end")) {
                System.out.println("Client kill connections");
                send.interrupt();
//                if (in.read() > -1) {
//                    msgFromServer(in);
//                }
            }
            System.out.println("Закрытие канала соединения - ВЫПОЛНЕНО.");
        } catch (
                IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void msgFromServer(BufferedReader in) throws IOException {
        String msgServ = in.readLine();
    }
}


